package br.com.texthelper.models;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;

import br.com.texthelper.annotations.MakeText;

public class BigDecimalParse implements TypeParser {

	@Override
	public <T> Object parse(String text, T obj) {
		return null;
	}

	@Override
	public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
		return (hasType(descriptor) && descriptor.getPropertyType().equals(BigDecimal.class)) ? 
			toBigDecimal(text, makeText) : null;
	}

}
