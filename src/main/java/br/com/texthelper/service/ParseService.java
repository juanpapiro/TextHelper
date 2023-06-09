package br.com.texthelper.service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.models.TypeParser;
import br.com.texthelper.utils.ListClassUtils;
import br.com.texthelper.utils.TextHelperUtils;


public class ParseService implements ToObject {
	
	@Override
	public Object toObject(String message, Object obj) {		
		StringBuilder builder = new StringBuilder(message);			
		List<Field> fields = TextHelperUtils.builderOrderFields(obj);
		String packageName = TypeParser.class.getPackage().getName();
		List<Object> classParsers = ListClassUtils.innerPackageWithType(packageName, TypeParser.class);
		fields.forEach(field -> {
			MakeText makeText = field.getAnnotation(MakeText.class);
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), obj.getClass());
				Method setter = propertyDescriptor.getWriteMethod();
				Object objValue = parse(classParsers, builder.substring(0, makeText.length()), makeText, propertyDescriptor);
				setter.invoke(obj, objValue);
				builder.delete(0, makeText.length());
			} catch (Exception e) {
				e.printStackTrace();
			}		
		});
		return obj;
	}
	

	public Object parse(List<Object> classParsers, String text, MakeText makeText, PropertyDescriptor obj) {
		Object result = null;
		for(Object classParser : classParsers) {
			try {
				TypeParser parser = (TypeParser) classParser;
				Object parse = parser.parse(text, makeText, obj);
				result = parse;
				if(parse != null) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	

}
