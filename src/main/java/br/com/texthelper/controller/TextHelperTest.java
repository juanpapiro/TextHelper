package br.com.texthelper.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.texthelper.models.Request;
import br.com.texthelper.utils.TextHelperFromObject;
import br.com.texthelper.utils.TextHelperFromText;

public class TextHelperTest {
	
	
	public static void main(String[] args) {
//		fromTextTest();
//		fromObjectTest();
	}

	
	public static String fromObjectTest() {
		String response = TextHelperFromObject.toText(buildRequest());
		System.out.println(response);
		return response;
	}
	
	public static Object fromTextTest() {
		Request request = new Request();
		TextHelperFromText parser = new TextHelperFromText();
		request = (Request) parser.toObject(buildText(), request);
		System.out.println(request.toJson());
		return request;
	}
	
	public static Request buildRequest() {
		return Request.build(BigDecimal.valueOf(10d), 1, 
				"Notebook Dell", LocalDateTime.now(), 55d, 10.234f);
	}
	
	public static String buildText() {
//		return "0000000001Notebook Dell                 00000000000000001000100320221745320000000000000000222200000000000000002222";
		return "0000000001Notebook Dell                 00000000000000001000290520221249220000000000000000550000000000000000010234";
	}
}
