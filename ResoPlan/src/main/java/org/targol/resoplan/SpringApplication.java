package org.targol.resoplan;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class SpringApplication {

	public static void main(final String[] args) {
		Application.launch(ResoPlanJavaFxApplication.class, args);
	}

}
