package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;

import br.com.texthelper.annotations.MakeText;

public class BigDecimalParser implements TypeParser {

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
