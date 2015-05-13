package com.genohm.boinq.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epimorphics.lda.restlets.RouterRestlet;
import com.sun.jersey.spi.container.servlet.ServletContainer;

@Configuration
@AutoConfigureAfter(PropertyPlaceholderReplaceConfiguration.class)
public class EldaConfiguration implements ApplicationContextAware, ServletContextInitializer {

	public Servlet jerseyServlet() {
		return new ServletContainer();
	}

	@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("com.epimorphics.api.initialSpecFile", "elda/config.ttl");
    }
	
	
	@Bean
	public ServletRegistrationBean jerseyServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(jerseyServlet(), "/iri/*");
        Map<String,String> params = new HashMap<String,String>();
        params.put("com.sun.jersey.config.property.packages", "com.epimorphics.lda.restlets");
        registration.setInitParameters(params);
        return registration;
	}
	
	@Bean
	ServletContextListener routerListener() {
		return new RouterRestlet.Init();
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
	}
	
	
}
