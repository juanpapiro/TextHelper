package br.com.texthelper.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.parsers.TypeConverter;
import br.com.texthelper.parsers.TypeParser;
import br.com.texthelper.service.ToObject;
import br.com.texthelper.utils.ListClassUtils;
import br.com.texthelper.utils.TextHelperLog;
import br.com.texthelper.utils.TextHelperUtils;

@Service
public class ToObjectImpl implements ToObject {
	
	@Autowired
	private List<TypeParser> parsers;
	
//	@Autowired()
//	public ToObjectImpl(List<TypeParser> parsers) {
//		this.parsers = parsers;
//	}
		
	@Override
	public Object toObject(String message, Object obj) {		
		StringBuilder builder = new StringBuilder(message);			
		List<Field> fields = TextHelperUtils.builderOrderFields(obj);
		String packageName = TypeParser.class.getPackage().getName();
//		List<Object> classParsers = ListClassUtils.findClassByPackageFilterType(packageName, TypeParser.class);
		fields.forEach(field -> {
			MakeText makeText = field.getAnnotation(MakeText.class);
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), obj.getClass());
				Method setter = propertyDescriptor.getWriteMethod();
//				TypeParser parser = (TypeParser) ListClassUtils.find(propertyDescriptor.getPropertyType(), packageName, TypeParser.class.getSimpleName());
//				TypeParser parser = TypeParser.find(parsers, propertyDescriptor.getPropertyType());
				TypeConverter parser = TypeConverter.find(propertyDescriptor.getPropertyType());
				Object objValue = parser.parse(builder.substring(0, makeText.length()), makeText, propertyDescriptor);
				setter.invoke(obj, objValue);
				builder.delete(0, makeText.length());
			} catch (Exception e) {
				TextHelperLog.error(e.getMessage(), e);
			}		
		});
		return obj;
	}
	
	@PostConstruct
	public void order() {
		this.parsers.sort(Comparator.comparing(parser -> parser.getClass().getSimpleName()));
	}
	
}
