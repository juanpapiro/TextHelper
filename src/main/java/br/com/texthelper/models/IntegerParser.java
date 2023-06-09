package br.com.texthelper.models;

import java.beans.PropertyDescriptor;
import java.util.Objects;

import br.com.texthelper.annotations.MakeText;

public class IntegerParser implements TypeParser {
	
	@Override
	public Object parse(String text, Object obj) {
		if(Objects.nonNull(obj) && obj instanceof Integer) {
			return Integer.valueOf(text);
		}
		return null;
	}

	@Override
	public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
		return (hasType(descriptor) && descriptor.getPropertyType().equals(Integer.class)
				|| descriptor.getPropertyType().equals(int.class)) ?
			Integer.valueOf(text) : null;
	}

}
