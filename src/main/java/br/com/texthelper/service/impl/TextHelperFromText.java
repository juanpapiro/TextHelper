package br.com.texthelper.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.service.ToObject;
import br.com.texthelper.utils.TextHelperUtils;

public class TextHelperFromText implements ToObject {
	
	@Override
	public Object toObject(String message, Object obj) {	
		StringBuilder builder = new StringBuilder(message);			
		List<Field> fields = TextHelperUtils.builderOrderFields(obj);	
		fields.forEach(field -> {
			MakeText makeText = field.getAnnotation(MakeText.class);
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), obj.getClass());
				Method setter = propertyDescriptor.getWriteMethod();
				Object objValue = Types.getType(propertyDescriptor)
						.parse(builder.substring(0, makeText.length()), makeText);
				setter.invoke(obj, objValue);
				builder.delete(0, makeText.length());
			} catch (Exception e) {
				e.printStackTrace();
			}		
		});
		return obj;
	}

	
	public enum Types { 
		
		STRING {
			public Object parse(String value, MakeText makeText) {
				return String.valueOf(value.trim());
			}
		},
		
		LONG {
			public Object parse(String value, MakeText makeText) {
				return Long.valueOf(TextHelperUtils.removeNonNumeric(value));
			}
		},
		
		INTEGER {
			public Object parse(String value, MakeText makeText) {
				return Integer.valueOf(TextHelperUtils.removeNonNumeric(value));
			}
		},
		
		FLOAT {
			public Object parse(String value, MakeText makeText) {
				return toBigDecimal(value, makeText).floatValue();
			}
		},
		
		DOUBLE {
			public Object parse(String value, MakeText makeText) {
				return toBigDecimal(value, makeText).doubleValue();
			}
		},
		
		BIGDECIMAL {
			public Object parse(String value, MakeText makeText) {
				return toBigDecimal(value, makeText);
			}
		},
		
		LOCALDATETIME {
			public Object parse(String value, MakeText makeText) {
				String patternDefault = "ddMMyyyyHHmmss";
				patternDefault = TextHelperUtils.isNull(makeText.pattern()) ? patternDefault : makeText.pattern();
				try {
					return TextHelperUtils.isNull(value) ? null : 
						LocalDateTime.parse(value.trim(), DateTimeFormatter.ofPattern(patternDefault));					
				} catch(Exception e) {
					return null;
				}
			}
		},
		
		DEFAULT {
			public Object parse(String value, MakeText makeText) {
				return null;
			}			
		};
				
		public abstract Object parse(String value, MakeText makeText);
		
		public static Types getType(PropertyDescriptor propertyDescriptor) {
			Types type = Types.DEFAULT;
			String typeSimpleName = "";
			try {
				typeSimpleName = propertyDescriptor.getPropertyType().getSimpleName().toUpperCase();
				type = Types.valueOf(typeSimpleName);
			} catch (Exception e) {
				System.out.println(String.format("Tipo %s inexistente em Types", typeSimpleName));
			}
			return type;
		}
		
	}
	
	
	private static BigDecimal toBigDecimal(String value, MakeText makeText) {
		value = TextHelperUtils.removeNonNumeric(value);
		return TextHelperUtils.isNull(value) ? null : 
			new BigDecimal(value)
				.movePointLeft(makeText.decimalMovePoint())
				.setScale(makeText.decimalPrecision(), RoundingMode.HALF_EVEN);
	}
	
	
}
