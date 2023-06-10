package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;

import br.com.texthelper.annotations.MakeText;

public class LongParser implements TypeParser {

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
