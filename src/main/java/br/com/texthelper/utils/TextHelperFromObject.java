package br.com.texthelper.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.texthelper.annotations.MakeText;

public class TextHelperFromObject {
	
	private static final String LEFT = "L";
	
	public static String toText(Object obj) {
		
		StringBuilder builder = new StringBuilder("");
		List<Field> fields = TextHelperUtils.builderOrderFields(obj);	
				
		fields.forEach(field -> {
			MakeText makeText = field.getAnnotation(MakeText.class);
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), obj.getClass());
				Method getter = propertyDescriptor.getReadMethod();
				Object objValue = getter.invoke(obj);
				Types type = Types.getType(propertyDescriptor);
				builder.append(formatString(objValue, makeText, type));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		return builder.toString();
	}
	
	
	public static String formatString(Object value, MakeText makeText, Types type) {
		StringBuilder sb = new StringBuilder("");
		int lengthTrelling;
		try {
			sb.append(type.parse(value, makeText));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		lengthTrelling = makeText.length() - sb.length();
		if(makeText.align().equals(LEFT)) {
			return sb.append(makeText.trelling().repeat(lengthTrelling)).toString();			
		} else {
			return sb.insert(0, makeText.trelling().repeat(lengthTrelling)).toString();
		}
	}
	
	
	public enum Types { 
		
		STRING {
			public String parse(Object value, MakeText makeText) {
				return String.valueOf(value);
			}
		},
		
		INTEGER {
			public String parse(Object value, MakeText makeText) {
				return String.valueOf(value);
			}
		},
		
		DOUBLE {
			public String parse(Object value, MakeText makeText) {
				Double doubleVaue = (double) value;
				return bigDecimalToText(BigDecimal.valueOf(doubleVaue), makeText);
			}
		},
		
		FLOAT {
			public String parse(Object value, MakeText makeText) {
				float floatValue = (float) value;
				return bigDecimalToText(BigDecimal.valueOf(floatValue), makeText);
			}
		},
		
		BIGDECIMAL {
			public String parse(Object value, MakeText makeText) {
				return bigDecimalToText(value, makeText);
			}
		},
		
		LOCALDATETIME {
			public String parse(Object value, MakeText makeText) {
				String patternDefault = "ddMMyyyyHHmmss";
				patternDefault = TextHelperUtils.isNull(makeText.pattern()) ? patternDefault : makeText.pattern();
				LocalDateTime dt = (LocalDateTime) value;
				try {
					return dt == null ?  null : dt.format(DateTimeFormatter.ofPattern(patternDefault));					
				} catch(Exception e) {
					return null;
				}
			}
		},
		
		DEFAULT {
			public String parse(Object value, MakeText makeText) {
				return null;
			}			
		};
		
//		private static final EnumSet<Types> enumSet = EnumSet.allOf(Types.class);
				
		public abstract String parse(Object value, MakeText makeText);
		
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
	
	
	
	private static String bigDecimalToText(Object value, MakeText makeText) {
		BigDecimal bg = (BigDecimal) value;
		bg = bg.setScale(makeText.decimalPrecision(), RoundingMode.HALF_EVEN);
		if (!TextHelperUtils.isNull(makeText.decimalSeparator())) {
			return decimalSeparatorFormat(bg.toString(), makeText);
		} else {
			return TextHelperUtils.removeNonNumeric(bg.toString());
		}
	}
	
	private static String decimalSeparatorFormat(String value, MakeText makeText) {		
		if(makeText.decimalSeparator().contains(".")) {
			return value;
		}
		if(makeText.decimalSeparator().contains(",")) {
			return value.replace(".", ",");
		}		
		return value;
	}


}
