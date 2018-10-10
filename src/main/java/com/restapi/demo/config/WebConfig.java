package com.restapi.demo.config;

import org.apache.catalina.servlets.WebdavServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * This class is to register the database servlet
 *
 */

@Configuration
public class WebConfig {

    @Bean
    ServletRegistrationBean h2servletRegistration()
    {
        ServletRegistrationBean regBean = new ServletRegistrationBean(new WebdavServlet());
        regBean.addUrlMappings("/console/*");
        return regBean;
    }
}
