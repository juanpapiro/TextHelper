package br.com.texthelper.service.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.parsers.TypeParser;
import br.com.texthelper.service.ToText;
import br.com.texthelper.utils.ListClassUtils;
import br.com.texthelper.utils.TextHelperLog;
import br.com.texthelper.utils.TextHelperUtils;

public class ToTextImpl implements ToText {

	private static final String LEFT = "L";
	private static final String IDENTIFY = "Parser";

	@Override
	public String toText(Object obj) {
		
		StringBuilder builder = new StringBuilder("");
		List<Field> fields = TextHelperUtils.builderOrderFields(obj);
		String packageName = TypeParser.class.getPackage().getName();
//		List<Object> parsers = ListClassUtils.innerPackageWithType(TypeParser.class.getPackageName(), TypeParser.class);
				
		fields.forEach(field -> {
			MakeText makeText = field.getAnnotation(MakeText.class);
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), obj.getClass());
				TypeParser parser = (TypeParser) ListClassUtils.find(propertyDescriptor.getPropertyType(), packageName, IDENTIFY);
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
	
	private String formatString(Object value, MakeText makeText, TypeParser parser) {
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
	

}
