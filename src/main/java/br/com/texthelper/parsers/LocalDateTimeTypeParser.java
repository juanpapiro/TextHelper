package br.com.texthelper.parsers;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.utils.TextHelperUtils;

public class LocalDateTimeTypeParser implements TypeParser {

	@Override
	public Object parse(String text, MakeText makeText, PropertyDescriptor descriptor) {
		String patternDefault = "ddMMyyyyHHmmss";
		patternDefault = TextHelperUtils.isNull(makeText.pattern()) ? patternDefault : makeText.pattern();
		try {
			return isNull(text) ? null : 
				LocalDateTime.parse(text.trim(), DateTimeFormatter.ofPattern(patternDefault));					
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public String parse(Object obj, MakeText makeText) {
		String patternDefault = "ddMMyyyyHHmmss";
		patternDefault = TextHelperUtils.isNull(makeText.pattern()) ? patternDefault : makeText.pattern();
		LocalDateTime dt = (LocalDateTime) obj;
		try {
			return dt == null ?  null : dt.format(DateTimeFormatter.ofPattern(patternDefault));					
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public boolean checkType(Class<?> type) {
		return type != null && type.equals(LocalDateTime.class);
	}

}
