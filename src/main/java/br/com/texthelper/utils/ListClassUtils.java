package br.com.texthelper.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import br.com.texthelper.parsers.TypeParser;

public class ListClassUtils {
	
	private ListClassUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static Object find(Class<?> type, String path, String identify) {
		try {
			StringBuilder sb = new StringBuilder(path);
			sb.append(".");
			sb.append(type.getSimpleName().substring(0, 1).toUpperCase());
			sb.append(type.getSimpleName().substring(1, type.getSimpleName().length()));
			sb.append(identify);
			return Class.forName(sb.toString()).getDeclaredConstructor().newInstance();	
		} catch (Exception e) {
			TextHelperLog.error("Falha ao listar classes de um pacote.", e);
			return null;
		}
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
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream is = classLoader.getResourceAsStream(pathResurce);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			List<String> locateClasses = br.lines().collect(Collectors.toList());
			locateClasses.forEach(locateClass -> TextHelperLog.info("Classe localizada: " + locateClass));
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
	
	
	
	public static List<String> getClasseNamesInPackage(String jarName, String packageName) {
		boolean getJar = true;
		ArrayList<String> arrayList = new ArrayList<>();
		packageName = packageName.replaceAll("\\.", "/");
		if(getJar)
			TextHelperLog.info("Jar " + jarName + " for " + packageName);
//		try(JarInputStream jarFile = new JarInputStream(new FileInputStream(TypeParser.class.getResource("/"+jarName).getPath()))) {
		try(JarInputStream jarFile = new JarInputStream(new FileInputStream(System.getProperty("user.dir")+"/target/"+jarName))) {
			JarEntry jarEntry;
			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				TextHelperLog.info(jarEntry.getName());
				if ((jarEntry.getName().startsWith("BOOT-INF/classes/"+packageName)) && (jarEntry.getName().endsWith(".class"))) {
					arrayList.add(jarEntry.getName().replaceAll("/", "\\."));
				}
			}
		} catch (Exception e) {
			TextHelperLog.error("Falha ao ler stream de bytes de resource.", e);
		}
		return arrayList;
	}
	

}
