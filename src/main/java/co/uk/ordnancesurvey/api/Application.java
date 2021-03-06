package co.uk.ordnancesurvey.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application entry point.
 * 
 * Basic spring boot RESTful application to test/prototype the new WFS FES
 * specification defined by OGC
 *
 */
@SpringBootApplication
@ComponentScan("co.uk.ordnancesurvey")
public class Application {

	/**
	 * Main method.
	 * 
	 * Any input parameters are be defined in the application.properties
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
