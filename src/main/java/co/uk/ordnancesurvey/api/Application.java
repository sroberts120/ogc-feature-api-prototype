package co.uk.ordnancesurvey.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point.
 * 
 * Basic spring boot RESTful application to test/prototype the new WFS FES
 * specififcation defined by OGC
 *
 */
@SpringBootApplication
public class Application {

	/**
	 * Main method.
	 * 
	 * Any input paramaters are be defined in the application.properties
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
