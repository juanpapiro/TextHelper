package br.com.texthelper.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

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
			
			TextHelperLog.info(System.getProperty("user.dir"));
			
			TextHelperLog.info("Iniciando listagem de classes: " + pathResurce);

			Class<?> clazz = Class.forName(RequestSimple.class.getPackageName().concat(".").concat("Request"));
			Object obj = clazz.getConstructor().newInstance();

			Gson g = new Gson();
			
			TextHelperLog.info("One CLass Locate -> " + g.toJson(obj));
			

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			TextHelperLog.info(classLoader.getResource(pathResurce.concat("/")).getPath());
			
			InputStream is = classLoader.getResourceAsStream(pathResurce.concat("/"));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			List<String> locateClasses = br.lines().collect(Collectors.toList());
			TextHelperLog.info(String.format("Size: %d", locateClasses.size()));
			locateClasses.forEach(locateClass -> TextHelperLog.info("Classe localizada: " + locateClass));
			
			Reflections reflections = new Reflections(
					"br.com.texthelper. ",
					new SubTypesScanner(false),
					ClasspathHelper.forClassLoader());
			Set<URL> urls = reflections.getConfiguration().getUrls();
			Set<Class<?>> listClass = reflections.getSubTypesOf(Object.class);
			
			
			urls.forEach(c -> TextHelperLog.info("class -> " + c.getPath()));
			listClass.forEach(c -> TextHelperLog.info("class -> " + c.getName()));

			
			return listClass.stream().map(Class::getName).collect(Collectors.toList());			
		} catch(Exception e) {
			TextHelperLog.error("Falha ao ler stream de bytes de resource.", e);
			return new ArrayList<>();
		}	
	}

}
