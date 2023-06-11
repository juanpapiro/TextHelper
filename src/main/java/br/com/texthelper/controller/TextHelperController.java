package br.com.texthelper.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.models.RequestSimple;
import br.com.texthelper.parsers.TypeParser;
import br.com.texthelper.service.ToObject;
import br.com.texthelper.service.impl.ToObjectImpl;
import br.com.texthelper.utils.TextHelperLog;

@RestController
public class TextHelperController {

	
	@GetMapping("/parse")
	public Object parse(@RequestParam String text) {
		ToObject parserFromText = new ToObjectImpl();
		try {
			return parserFromText.toObject(text, new RequestSimple());
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping("listclass")
	public List<String> listClass() {
		String pathResurce = TypeParser.class.getPackageName().replaceAll("[.]","/");
		try {
			
			TextHelperLog.info("Iniciando listagem de classes: " + pathResurce);

			Reflections reflections = new Reflections("br.com.texthelper");
			
			Set<Class<? extends TypeParser>> subTypes = reflections.getSubTypesOf(TypeParser.class);

			Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(MakeText.class);
			
			TextHelperLog.info(String.format("Lista sybTypes: %d", subTypes.size()));
			TextHelperLog.info(String.format("Lista annotations: %d", annotated.size()));
			
			return subTypes.stream().map(Class::getName).collect(Collectors.toList());			
		} catch(Exception e) {
			TextHelperLog.error("Falha ao ler stream de bytes de resource.", e);
			return new ArrayList<>();
		}	
	}

}
