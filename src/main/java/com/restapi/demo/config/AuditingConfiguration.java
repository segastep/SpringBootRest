package com.restapi.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.restapi.demo.audit.AuditAwareImpl;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * @author G.Nikolov on 14/10/18
 * @project rest-service-basic
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = {
        "com.restapi.demo.repositories"
})
@ComponentScan(basePackages = "com.restapi.demo")
public class AuditingConfiguration {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditAwareImpl();
    }

}