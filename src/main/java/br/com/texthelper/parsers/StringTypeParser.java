package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;

import br.com.texthelper.annotations.MakeText;

public class StringTypeParser implements TypeParser {


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
