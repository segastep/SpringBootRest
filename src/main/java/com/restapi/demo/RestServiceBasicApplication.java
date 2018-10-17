package com.restapi.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * Spring application runner class
 */

@SpringBootApplication
@EnableJpaAuditing
public class RestServiceBasicApplication {


	public static void main(String[] args) {

		SpringApplication.run(RestServiceBasicApplication.class, args);
	}
}
