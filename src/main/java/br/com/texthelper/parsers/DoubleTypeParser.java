package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;

import br.com.texthelper.annotations.MakeText;

public class DoubleTypeParser implements TypeParser {

	@Override
	public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
		return toBigDecimal(text, makeText).doubleValue();
	}

	@Override
	public String parse(Object obj, MakeText makeText) {
		Double doubleVaue = (double) obj;
		return bigDecimalToText(BigDecimal.valueOf(doubleVaue), makeText);
	}

	@Override
	public boolean checkType(Class<?> type) {
		return type != null && (type.equals(Double.class) || type.equals(double.class));
	}

}
