package br.com.texthelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.texthelper")
public class App {
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
