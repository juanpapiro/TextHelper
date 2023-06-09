package br.com.texthelper.models;

import java.beans.PropertyDescriptor;

import br.com.texthelper.annotations.MakeText;

public class DoubleParse implements TypeParser {

	@Override
	public <T> Object parse(String text, T obj) {
		return null;
	}

	@Override
	public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
		return (hasType(descriptor) && descriptor.getPropertyType().equals(Double.class)
				|| descriptor.getPropertyType().equals(double.class)) ? 
				toBigDecimal(text, makeText).doubleValue() : null;
	}

}
