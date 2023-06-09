package br.com.texthelper.models;

import java.beans.PropertyDescriptor;
import java.util.Objects;

import br.com.texthelper.annotations.MakeText;

public class LongParser implements TypeParser {

	@Override
	public <T> Object parse(String text, T obj) {
		if(Objects.nonNull(obj) && obj instanceof Long) {
			return Long.valueOf(text);
		}
		return null;
	}

	@Override
	public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
		return (hasType(descriptor) && descriptor.getPropertyType().equals(Long.class)
				|| descriptor.getPropertyType().equals(long.class)) ?
			Long.valueOf(text) : null;
	}

}
