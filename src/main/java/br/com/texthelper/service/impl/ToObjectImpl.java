package br.com.texthelper.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.parsers.TypeParser;
import br.com.texthelper.service.ToObject;
import br.com.texthelper.utils.ListClassUtils;
import br.com.texthelper.utils.TextHelperLog;
import br.com.texthelper.utils.TextHelperUtils;


public class ToObjectImpl implements ToObject {
		
	@Override
	public Object toObject(String message, Object obj) {		
		StringBuilder builder = new StringBuilder(message);			
		List<Field> fields = TextHelperUtils.builderOrderFields(obj);
		String packageName = TypeParser.class.getPackage().getName();
		List<Object> classParsers = ListClassUtils.findClassByPackageFilterType(packageName, TypeParser.class);
		fields.forEach(field -> {
			MakeText makeText = field.getAnnotation(MakeText.class);
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), obj.getClass());
				Method setter = propertyDescriptor.getWriteMethod();
//				TypeParser parser = (TypeParser) ListClassUtils.find(propertyDescriptor.getPropertyType(), packageName, TypeParser.class.getSimpleName());
				TypeParser parser = TypeParser.find(classParsers, propertyDescriptor.getPropertyType());
				Object objValue = parser.parse(builder.substring(0, makeText.length()), makeText, propertyDescriptor);
				setter.invoke(obj, objValue);
				builder.delete(0, makeText.length());
			} catch (Exception e) {
				TextHelperLog.error(e.getMessage(), e);
			}		
		});
		return obj;
	}
	
	
}
