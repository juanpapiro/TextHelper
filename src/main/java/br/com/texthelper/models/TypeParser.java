package br.com.texthelper.models;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import br.com.texthelper.annotations.MakeText;

public interface TypeParser {
	
	String PACKAGE = "br.com.texthelper.models";
	String PACKAGE_PATH = "br/com/texthelper/models";
	
	public abstract <T> Object parse(String text, T obj);
	public abstract Object parse(String text, MakeText makeText, PropertyDescriptor descriptor);
	
	default boolean hasType(PropertyDescriptor descriptor) {
		return Objects.nonNull(descriptor) && Objects.nonNull(descriptor.getPropertyType());
	}
	
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
	

}
