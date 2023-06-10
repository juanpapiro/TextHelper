package br.com.texthelper.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextHelperLog {
	
	private static Logger log = LoggerFactory.getLogger(TextHelperLog.class);
	
	private TextHelperLog() {
		throw new IllegalStateException("Uyility class");
	}

	public static void info(String message) {
		log.info(message);
	}
	
	public static void info(String message, Object obj) {
		log.info(message, obj);
	}
	
	public static void error(String message) {
		log.error(message);
	}
	
	public static void error(String message, Throwable e) {
		log.error(message, e);
	}
	
	public static void warning(String message) {
		log.warn(message);
	}
	
	
	
}
