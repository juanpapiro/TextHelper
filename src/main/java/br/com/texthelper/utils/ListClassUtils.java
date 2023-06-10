package br.com.texthelper.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.el.util.ReflectionUtil;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import br.com.texthelper.annotations.MakeText;
import br.com.texthelper.parsers.TypeParser;

public class ListClassUtils {
	
	private ListClassUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	/**
	 * Obtem lista de classes Object de um pacote filtradas por um tipo
	 * Ideal para obter classes com extends ou implements
	 */
	public static List<Object> innerPackageWithType(String path, Class<?> type) {
		List<Class<?>> classes = innerPackage(path);
		return classes.stream().map(clazz -> extractInstance(clazz, type))
			.filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	/**
	 * Obtem lista de Class<?> de um pacote
	 */
	public static List<Class<?>> innerPackage(String path) {
		String pathResurce = path.replaceAll("[.]","/");
		List<Class<?>> myClasses = new ArrayList<>();
		try {
			List<String> files = getFilesByResource(pathResurce);
			for(String fileName : files) {
				fileName = fileName.replace(".class", "");
				Class<?> myClassLocate = Class.forName(path+"."+fileName);
				if(myClassLocate != null) {
					myClasses.add(myClassLocate);
				}
			}
		} catch(Exception e) {
			TextHelperLog.error("Falha ao listar classes de um pacote.", e);
		}
		return myClasses;
	}
	
	/**
	 * Lista classes de um pacote
	 */
	private static List<String> getFilesByResource(String pathResurce) {
		try {
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
	
	/**
	 * Verifica se uma classe é de um determinado tipo
	 * retorna uma instância da classe verificada
	 */
	private static Object extractInstance(Class<?> myClassLocate, Class<?> type) {
		try {
			Object obj = myClassLocate.getDeclaredConstructor().newInstance();		
			type.cast(obj);
			return obj;	
		} catch(Exception e) {
			return null;
		}
	}
	

}
