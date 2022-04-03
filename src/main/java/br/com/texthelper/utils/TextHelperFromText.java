package br.com.texthelper.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import br.com.texthelper.annotations.MakeText;

public class TextHelperFromText {
	
	
	public static Object toObject(Object obj, String message) {
				
		StringBuilder builder = new StringBuilder(message);			
		List<Field> fields = TextHelperUtils.builderOrderFields(obj);
		
		fields.forEach(field -> {
			MakeText makeText = field.getAnnotation(MakeText.class);
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), obj.getClass());
				Method setter = propertyDescriptor.getWriteMethod();
				Object objValue = Types.valueOf(propertyDescriptor.getPropertyType().getSimpleName().toUpperCase())
						.parse(builder.substring(0, makeText.length()).toString(), makeText);
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
		
		INTEGER {
			public Object parse(String value, MakeText makeText) {
				return Integer.valueOf(TextHelperUtils.removeNonNumeric(value.trim()));
			}
		},
		
		BIGDECIMAL {
			public Object parse(String value, MakeText makeText) {
				return TextHelperUtils.isNull(value) ? null : 
					new BigDecimal(TextHelperUtils.removeNonNumeric(value.trim())).movePointLeft(2).setScale(2);
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
		
		private static final EnumSet<Types> enumSet = EnumSet.allOf(Types.class);
		
		public static Types getParser(String typeParam) {
			if(typeParam == null) {
				return DEFAULT;
			}
			List<Types> types = enumSet.stream()
					.filter(type -> type.name().equals(typeParam.toUpperCase()))
					.collect(Collectors.toList());
			return types.isEmpty() ? DEFAULT : types.get(0);
		}
				
		public abstract Object parse(String value, MakeText makeText);
		
	}
	
}
