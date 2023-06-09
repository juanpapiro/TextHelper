package br.com.texthelper.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListClassUtils {
	
	private ListClassUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static List<Object> innerPackageWithType(String path, Class<?> type) {
		List<Class<?>> classes = innerPackage(path);
		return classes.stream().map(clazz -> extractInstance(clazz, type))
			.filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	public static List<Class<?>> innerPackage(String path) {
		String pathResurce = path.replaceAll("[.]","/");
		List<Class<?>> myClasses = new ArrayList<>();
		try {
//			File file = new File(TypeParser.class.getClassLoader().getResource(PACKAGE_PATH).getPath());		
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			File file = new File(classLoader.getResource(pathResurce).getPath());		
			List<String> files = Arrays.asList(file.list());
			for(String fileName : files) {
				fileName = fileName.replace(".class", "");
				Class<?> myClassLocate = Class.forName(path+"."+fileName);
//				myClassLocate = extractInstance(myClassLocate);
				if(myClassLocate != null) {
					myClasses.add(myClassLocate);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return myClasses;
	}
	
	private static Object extractInstance(Class<?> myClassLocate, Class<?> type) {
		try {
			Object obj = myClassLocate.getDeclaredConstructor().newInstance();		
			type.cast(obj);
			return obj;
//			if(myClassLocate.getDeclaredConstructor().newInstance() instanceof TypeParser) {
//				return myClassLocate;
//			}			
		} catch(Exception e) {
			return null;
		}
	}
	
	private static Class<?>[] getClasses(String packageName) throws Exception {
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    assert classLoader != null;
	    String path = packageName.replace('.', '/');
	    Enumeration<URL> resources = classLoader.getResources(path);
	    List<File> dirs = new ArrayList<File>();
	    while (resources.hasMoreElements()) {
	        URL resource = resources.nextElement();
	        dirs.add(new File(resource.getFile()));
	    }
	    ArrayList<Class<?>> classes = new ArrayList<>();
	    for (File directory : dirs) {
	        classes.addAll(findClasses(directory, packageName));
	    }
	    return classes.toArray(new Class[classes.size()]);
	}

	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
	    List<Class<?>> classes = new ArrayList<>();
	    if (!directory.exists()) {
	        return classes;
	    }
	    File[] files = directory.listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            assert !file.getName().contains(".");
	            classes.addAll(findClasses(file, packageName + "." + file.getName()));
	        } else if (file.getName().endsWith(".class")) {
	            classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
	        }
	    }
	    return classes;
	}
	
	public static List<Class<?>> listByLib() {
//		Reflections reflections = new Reflections("br.com.texthelper");
//		Set<Class<? extends Type>> subTypes = reflections.getSubTypesOf(Type.class);
//		List<Object> objects = new ArrayList<>();
//		subTypes.forEach(type -> {
//			for(int i = 0; i < type.getMethods().length; i++) {
//				if(type.getMethods()[i].getName().equals("parse")) {
//					Method m = type.getMethods()[i];
//					try {
//						objects.add(m.invoke(type.getDeclaredConstructor().newInstance(), obj, text));
//					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
//							| InstantiationException | NoSuchMethodException | SecurityException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
		return new ArrayList<>();
	}

}
