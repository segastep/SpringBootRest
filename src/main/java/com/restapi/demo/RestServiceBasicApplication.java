package com.restapi.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * Spring application runner class
 */

@SpringBootApplication
@EnableJpaAuditing
@EnableWebMvc
public class RestServiceBasicApplication {


	public static void main(String[] args) {
		SpringApplication.run(RestServiceBasicApplication.class, args);
	}
}
