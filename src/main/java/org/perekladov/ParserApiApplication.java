package org.perekladov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ParserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParserApiApplication.class, args);
	}

}
