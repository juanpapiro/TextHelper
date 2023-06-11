package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;

import br.com.texthelper.annotations.MakeText;

public class FloatTypeParser implements TypeParser {

	@Override
	public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
		return toBigDecimal(text, makeText).floatValue(); 
	}

	@Override
	public String parse(Object obj, MakeText makeText) {
		Float floatVaue = (float) obj;
		return bigDecimalToText(BigDecimal.valueOf(floatVaue), makeText);
	}

	@Override
	public boolean checkType(Class<?> type) {
		return type != null && (type.equals(Float.class) || type.equals(float.class));
	}

}
