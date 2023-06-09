package br.com.texthelper.models;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.utils.TextHelperUtils;

public class LocalDateTimeParse implements TypeParser {

	@Override
	public <T> Object parse(String text, T obj) {
		return null;
	}

	@Override
	public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
		if(!hasType(descriptor) || !descriptor.getPropertyType().equals(LocalDateTime.class)) {
			return null;
		}
		String patternDefault = "ddMMyyyyHHmmss";
		patternDefault = TextHelperUtils.isNull(makeText.pattern()) ? patternDefault : makeText.pattern();
		try {
			return isNull(text) ? null : 
				LocalDateTime.parse(text.trim(), DateTimeFormatter.ofPattern(patternDefault));					
		} catch(Exception e) {
			return null;
		}
	}

}
