package br.com.texthelper;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.utils.TextHelperLog;

@SpringBootApplication
public class App {
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		Reflections reflections = new Reflections(
				"br.com.texthelper",
				new SubTypesScanner(false),
				ClasspathHelper.forClassLoader());
		Set<Class<?>> listClass = reflections.getSubTypesOf(Object.class);
		listClass.forEach(c -> TextHelperLog.info("class -> " + c.getName()));
	}

}
