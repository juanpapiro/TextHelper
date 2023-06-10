package br.com.texthelper.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
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
			
			File file = new File(this.getClass().getResource(System.getProperty("user.dir")).getPath());
			
			TextHelperLog.info("File: " + file);
			TextHelperLog.info("Iniciando listagem de classes: " + pathResurce);

			Class<?> clazz = Class.forName(TypeParser.class.getPackageName().concat(".").concat("BigDecimalParser"));
			TextHelperLog.info("One CLass Locate -> " + clazz.getName());

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream is = classLoader.getResourceAsStream(pathResurce);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			List<String> locateClasses = br.lines().collect(Collectors.toList());
			locateClasses.forEach(locateClass -> TextHelperLog.info("Classe localizada: " + locateClass));
			
			Reflections reflections = new Reflections(
					"br.com.texthelper",
					new SubTypesScanner(false),
					ClasspathHelper.forClassLoader());
			Set<Class<?>> listClass = reflections.getTypesAnnotatedWith(MakeText.class);
			listClass.forEach(c -> TextHelperLog.info("class -> " + c.getName()));
			
			return locateClasses;			
		} catch(Exception e) {
			TextHelperLog.error("Falha ao ler stream de bytes de resource.", e);
			return new ArrayList<>();
		}	
	}

}
