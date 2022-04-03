package br.com.texthelper.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.texthelper.annotations.MakeText;

public class TextHelperUtils {
	
	public static boolean isNull(String value) {
		return value.trim() == null || value.trim().isEmpty();
	}
	
	public static String removeNonNumeric(String value) {
		return value.replaceAll("[^ 0-9]", "");
	}
	
	public static List<Field> builderOrderFields(Object obj) {
		List<Field> fields = Arrays.asList(obj.getClass().getDeclaredFields());
		return fields.stream()
					.sorted((field1, field2) -> Integer.valueOf(field1.getAnnotation(MakeText.class).order())
					.compareTo(Integer.valueOf(field2.getAnnotation(MakeText.class).order())))
					.collect(Collectors.toList());
	}

}
