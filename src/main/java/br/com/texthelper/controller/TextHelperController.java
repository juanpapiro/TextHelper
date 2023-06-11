package br.com.texthelper.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.texthelper.App;
import br.com.texthelper.models.RequestSimple;
import br.com.texthelper.parsers.TypeParser;
import br.com.texthelper.service.ToObject;
import br.com.texthelper.service.impl.ToObjectImpl;
import br.com.texthelper.utils.TextHelperLog;

@RestController
public class TextHelperController {

	@Autowired
	private ResourceLoader resourceLoader;
	
	@GetMapping("/parse")
	public Object parse(@RequestParam String text) {
		ToObject parserFromText = new ToObjectImpl();
		try {
			return parserFromText.toObject(text, new RequestSimple());
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping("/listclass")
	public Object find() {
		try {
			String pathResurce = App.class.getPackageName().replaceAll("[.]", "/");
			TextHelperLog.info("Iniciando listagem de classes: " + App.class.getPackageName());
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream is = classLoader.getSystemResourceAsStream(pathResurce);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			List<String> locateClasses = br.lines().collect(Collectors.toList());
			locateClasses.forEach(locateClass -> TextHelperLog.info("Classe localizada: " + locateClass));
			return locateClasses;			
		} catch(Exception e) {
			TextHelperLog.error("Falha ao ler stream de bytes de resource.", e);
			return new ArrayList<>();
		}
	}

}
