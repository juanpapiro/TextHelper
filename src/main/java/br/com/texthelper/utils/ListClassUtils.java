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

public class ListClassUtils {
	
	private static final String JAR_NAME = "texthelper-0.0.2.jar";
	
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
	

	
	/**
	 * Obtem lista de classes Object de um pacote filtradas por um tipo
	 * Ideal para obter classes com extends ou implements
	 */
	public static List<Object> findClassByPackageFilterType(String path, Class<?> type) {
		List<Class<?>> classes = findClassByPackage(path);
		return classes.stream().map(clazz -> extractInstance(clazz, type))
			.filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	
	public static List<Class<?>> findClassByPackage(String packageName) {
		List<String> classLocateNames = getClasseNamesInPackage(packageName);
		List<Class<?>> classLocateLoad = new ArrayList<>();
		classLocateNames.forEach(classLocate -> {
			Class<?> classLoad = extractClass(packageName, classLocate);
			if(classLoad != null) {
				classLocateLoad.add(classLoad);
			}
		});
		return classLocateLoad;
	}
	
	
	public static List<String> getClasseNamesInPackage(String packageName) {
		ArrayList<String> classLocateNames = new ArrayList<>();
		packageName = packageName.replaceAll("[.]", "/");
		String pathJar = System.getProperty("user.dir")+"/target/"+JAR_NAME;
		TextHelperLog.info(String.format("Jar name: %s - Package: %s - Path jar: %s", JAR_NAME, packageName, pathJar));
		try(JarInputStream jarFile = new JarInputStream(new FileInputStream(pathJar))) {
			JarEntry jarEntry;
			while(true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				TextHelperLog.info(jarEntry.getName());
				if ((jarEntry.getName().contains(packageName)) && (jarEntry.getName().endsWith(".class") )) {
					String[] splitClass = jarEntry.getName().replace(".class", "").split("/");
					classLocateNames.add(splitClass[splitClass.length-1]);
				}
			}
		} catch (Exception e) {
			TextHelperLog.error("Falha ao ler jar.", e);
		}
		classLocateNames.forEach(classLocate -> String.format("Classe localizada: %s", classLocateNames));
		return classLocateNames;
	}
	
	private static Class<?> extractClass(String packageName, String className) {
		try {
			return Class.forName(packageName + "." + className);
		} catch(Exception e) {
			TextHelperLog.error("Falha ao obter classe com Calss.forName().", e);
			return null;
		}
	}
	

}
