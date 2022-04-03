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
				Types type = Types.valueOf(propertyDescriptor.getPropertyType().getSimpleName().toUpperCase());
				builder.append(formatString(objValue, makeText, type));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		return builder.toString();
	}
	
	
	public static String formatString(Object value, MakeText makeText, Types type) {
		String valueFormat = "";
		int lengthTrelling = makeText.length() - valueFormat.length();
		String valueFormatTrelling = makeText.trelling().repeat(lengthTrelling);
		try {
			valueFormat = type.parse(value, makeText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(makeText.align().equals(LEFT)) {
			return valueFormat.concat(valueFormatTrelling);			
		} else {
			return valueFormatTrelling.concat(valueFormat);
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
		
		BIGDECIMAL {
			public String parse(Object value, MakeText makeText) {
				BigDecimal bg = (BigDecimal) value;
				if (!TextHelperUtils.isNull(makeText.pattern())) {
					return decimalFormat(bg.toString(), makeText.pattern());
				} else {
					return TextHelperUtils.removeNonNumeric(bg.toString());
				}
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
				
		public abstract String parse(Object value, MakeText makeText);
		
	}
	
	private static String decimalFormat(String value, String pattern) {		
		if(pattern.contains(".")) {
			return value;
		}
		if(pattern.contains(",")) {
			return value.replace(".", ",");
		}		
		return value;
	}


}
