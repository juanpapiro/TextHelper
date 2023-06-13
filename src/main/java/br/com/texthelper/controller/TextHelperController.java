//package br.com.texthelper.controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import br.com.texthelper.models.RequestSimple;
//import br.com.texthelper.parsers.TypeParser;
//import br.com.texthelper.service.ToObject;
//import br.com.texthelper.service.impl.ToObjectImpl;
//import br.com.texthelper.utils.ListClassUtils;
//import br.com.texthelper.utils.TextHelperLog;
//
//@RestController
//public class TextHelperController {
//
//
////	
////	@GetMapping("/parse")
////	public Object parse(@RequestParam String text) {
////		ToObject parserFromText = new ToObjectImpl();
////		try {
////			return parserFromText.toObject(text, new RequestSimple());
////		} catch (Exception e) {
////			return null;
////		}
////	}
//	
//	@GetMapping("/listclass")
//	public Object find() {
//		try {	
//			String pathResurce = TypeParser.class.getPackageName();
//			TextHelperLog.info(System.getProperty("parent"));
//			TextHelperLog.info(System.getProperty("artifactId"));
//			TextHelperLog.info(System.getProperty("user.dir"));
//			List<String> classes = ListClassUtils.getClasseNamesInPackage(pathResurce);
//			classes.forEach(TextHelperLog::info);
//			return classes;			
//		} catch(Exception e) {
//			TextHelperLog.error("Falha ao ler stream de bytes de resource.", e);
//			return new ArrayList<>();
//		}
//	}
//
//}
