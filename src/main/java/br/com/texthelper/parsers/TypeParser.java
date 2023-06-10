package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.utils.TextHelperUtils;

public interface TypeParser {
	
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
	
	
	
	public static TypeParser find(List<Object> parsers, Class<?> type) {
		return (TypeParser) parsers.stream()
				.filter(parserClass -> ((TypeParser) parserClass).checkType(type))
				.findFirst()
				.orElseThrow(() -> new RuntimeException(
						String.format("Tipo %s sem classe de conversão.",
								type.getName())));
	}

}
