package com.genohm.boinq.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.genohm.boinq.web.servlet.FusekiProxyServlet;
import com.hp.hpl.jena.assembler.assemblers.FileManagerAssembler;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

@Configuration
@AutoConfigureAfter(CacheConfiguration.class)
public class ProxyConfiguration implements EnvironmentAware {

	protected Environment environment;
    protected RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(Environment environment) {
    	this.environment = environment;
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.fuseki.");
	}
	@Bean
	public Servlet proxyServlet() {
		return new FusekiProxyServlet();
	}
	
	@Bean
	public ServletRegistrationBean proxyServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(proxyServlet(), "/fuseki/*");
        Map<String,String> params = new HashMap<String,String>();
        params.put("proxyHost", "localhost");
        params.put("proxyPort", propertyResolver.getProperty(FusekiConfiguration.PROP_PORT));
        //FIXME: if more inputs to be handled, either read them from localdata.ttl
        //       or proxy to root and give service in client
        params.put("proxyPath", "/DYNAMIC/");
        params.put("sourcePath", "/fuseki/");
        registration.setInitParameters(params);
        return registration;
	}
	
}
