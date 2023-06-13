package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;

import org.springframework.stereotype.Component;

import br.com.texthelper.annotations.MakeText;

@Component
public class IntegerTypeParser implements TypeParser {

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
