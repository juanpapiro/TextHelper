package br.com.texthelper.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.parsers.TypeConverter;
import br.com.texthelper.parsers.TypeParser;
import br.com.texthelper.service.ToText;
import br.com.texthelper.utils.ListClassUtils;
import br.com.texthelper.utils.TextHelperLog;
import br.com.texthelper.utils.TextHelperUtils;

@Service
public class ToTextImpl implements ToText {
	
	@Autowired
	private List<TypeParser> parsers;

	private static final String LEFT = "L";

	@Override
	public String toText(Object obj) {
		
		StringBuilder builder = new StringBuilder("");
		List<Field> fields = TextHelperUtils.builderOrderFields(obj);
		String packageName = TypeParser.class.getPackage().getName();
//		List<Object> parsers = ListClassUtils.findClassByPackageFilterType(packageName, TypeParser.class);
				
		fields.forEach(field -> {
			MakeText makeText = field.getAnnotation(MakeText.class);
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), obj.getClass());
//				TypeParser parser = (TypeParser) ListClassUtils.find(propertyDescriptor.getPropertyType(), packageName, TypeParser.class.getSimpleName());
//				TypeParser parser = TypeParser.find(parsers, propertyDescriptor.getPropertyType());
				TypeConverter parser = TypeConverter.find(propertyDescriptor.getPropertyType());
				Method getter = propertyDescriptor.getReadMethod();
				Object objValue = getter.invoke(obj);
				builder.append(formatString(objValue, makeText, parser));
			} catch (Exception e) {
				builder.append(makeText.trelling().repeat(makeText.length()));
				TextHelperLog.error(e.getMessage(), e);
			}
		});
		
		return builder.toString();
	}
	
	private String formatString(Object value, MakeText makeText, TypeConverter parser) {
		StringBuilder sb = new StringBuilder("");
		int lengthTrelling;
		try {
			sb.append(parser.parse(value, makeText));
		} catch (Exception e) {
			TextHelperLog.error("Falha ao realizar parse.", e);
		}	
		lengthTrelling = makeText.length() - sb.length();
		if(makeText.align().equals(LEFT)) {
			return sb.append(makeText.trelling().repeat(lengthTrelling)).toString();			
		} else {
			return sb.insert(0, makeText.trelling().repeat(lengthTrelling)).toString();
		}
	}
//	private String formatString(Object value, MakeText makeText, TypeParser parser) {
//		StringBuilder sb = new StringBuilder("");
//		int lengthTrelling;
//		try {
//			sb.append(parser.parse(value, makeText));
//		} catch (Exception e) {
//			TextHelperLog.error("Falha ao realizar parse.", e);
//		}	
//		lengthTrelling = makeText.length() - sb.length();
//		if(makeText.align().equals(LEFT)) {
//			return sb.append(makeText.trelling().repeat(lengthTrelling)).toString();			
//		} else {
//			return sb.insert(0, makeText.trelling().repeat(lengthTrelling)).toString();
//		}
//	}
	

}
