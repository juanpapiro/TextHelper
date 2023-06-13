package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.utils.TextHelperUtils;

public interface TypeConverter {

	Object parse(String text, MakeText makeText, PropertyDescriptor descriptor);
	String parse(Object obj, MakeText makeText);
	boolean checkType(Class<?> type);
	
	
	default BigDecimal toBigDecimal(String text, MakeText makeText) {
		text = removeNonNumeric(text);
		return isNull(text) ? null : new BigDecimal(text)
				.movePointLeft(makeText.decimalMovePoint())
				.setScale(makeText.decimalPrecision(), RoundingMode.HALF_EVEN);
	}
	
	default String removeNonNumeric(String value) {
		value = value != null ? value : "";
		return value.replaceAll("[^ 0-9]", "");
	}
	
	default boolean isNull(String value) {
		return value == null || value.trim().isEmpty();
	}
	
	default String bigDecimalToText(Object value, MakeText makeText) {
		BigDecimal bg = (BigDecimal) value;
		bg = bg.setScale(makeText.decimalPrecision(), RoundingMode.HALF_EVEN);
		if (!TextHelperUtils.isNull(makeText.decimalSeparator())) {
			return decimalSeparatorFormat(bg.toString(), makeText);
		} else {
			return TextHelperUtils.removeNonNumeric(bg.toString());
		}
	}
	
	default String decimalSeparatorFormat(String value, MakeText makeText) {		
		if(makeText.decimalSeparator().contains(".")) {
			return value;
		}
		if(makeText.decimalSeparator().contains(",")) {
			return value.replace(".", ",");
		}		
		return value;
	}
	
	
	
	public static TypeConverter find(Class<?> type) {
		return Stream.of(TypeConverter.class.getClasses())
					 .map(c -> {
						 try {
							 return (TypeConverter) c.getConstructor().newInstance();							 
						 } catch(Exception e) {
							 return null;
						 }
					 })
					 .filter(converter -> converter.checkType(type))
					 .findFirst()
					 .orElseThrow(() -> new RuntimeException(String.format(
							 "Tipo %s sem classe de conversão.",type.getName())));
	}
	
	
	
	
	public class StringConverter implements TypeConverter {
		@Override
		public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
			return String.valueOf(text).trim();
		}
		@Override
		public String parse(Object obj, MakeText makeText) {
			return String.valueOf(obj);
		}
		@Override
		public boolean checkType(Class<?> type) {
			return (type != null && type.equals(String.class));
		}
	}
	
	public class DoubleConverter implements TypeConverter {
		@Override
		public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
			return toBigDecimal(text, makeText).doubleValue();
		}
		@Override
		public String parse(Object obj, MakeText makeText) {
			Double doubleVaue = (double) obj;
			return bigDecimalToText(BigDecimal.valueOf(doubleVaue), makeText);
		}
		@Override
		public boolean checkType(Class<?> type) {
			return type != null && (type.equals(Double.class) || type.equals(double.class));
		}
	}
	
	public class BigDecimalConverter implements TypeConverter {
		@Override
		public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
			return toBigDecimal(text, makeText);
		}
		@Override
		public String parse(Object obj, MakeText makeText) {
			return bigDecimalToText(obj, makeText);
		}
		@Override
		public boolean checkType(Class<?> type) {
			return type != null && type.equals(BigDecimal.class);
		}
	}
	
	public class FloatConverter implements TypeConverter {
		@Override
		public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
			return toBigDecimal(text, makeText).floatValue(); 
		}
		@Override
		public String parse(Object obj, MakeText makeText) {
			Float floatVaue = (float) obj;
			return bigDecimalToText(BigDecimal.valueOf(floatVaue), makeText);
		}
		@Override
		public boolean checkType(Class<?> type) {
			return type != null && (type.equals(Float.class) || type.equals(float.class));
		}
	}
	
	public class IntegerConverter implements TypeConverter {
		@Override
		public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
			return Integer.valueOf(text);
		}
		@Override
		public String parse(Object obj, MakeText makeText) {
			return String.valueOf(obj);
		}
		@Override
		public boolean checkType(Class<?> type) {
			return type != null && (type.equals(Integer.class) || type.equals(int.class));
		}
	}
	
	public class LongConverter implements TypeConverter {
		@Override
		public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
			return Long.valueOf(text);
		}
		@Override
		public String parse(Object obj, MakeText makeText) {
			return String.valueOf(obj);
		}
		@Override
		public boolean checkType(Class<?> type) {
			return type != null && (type.equals(Long.class) || type.equals(long.class));
		}
	}
	
	public class LocalDateTimeConverter implements TypeConverter {
		@Override
		public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
			String patternDefault = "ddMMyyyyHHmmss";
			patternDefault = TextHelperUtils.isNull(makeText.pattern()) ? patternDefault : makeText.pattern();
			try {
				return isNull(text) ? null : 
					LocalDateTime.parse(text.trim(), DateTimeFormatter.ofPattern(patternDefault));					
			} catch(Exception e) {
				return null;
			}
		}
		@Override
		public String parse(Object obj, MakeText makeText) {
			String patternDefault = "ddMMyyyyHHmmss";
			patternDefault = TextHelperUtils.isNull(makeText.pattern()) ? patternDefault : makeText.pattern();
			LocalDateTime dt = (LocalDateTime) obj;
			try {
				return dt == null ?  null : dt.format(DateTimeFormatter.ofPattern(patternDefault));					
			} catch(Exception e) {
				return null;
			}
		}
		@Override
		public boolean checkType(Class<?> type) {
			return type != null && type.equals(LocalDateTime.class);
		}
	}
}
