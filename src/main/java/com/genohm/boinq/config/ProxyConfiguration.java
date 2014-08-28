package com.genohm.boinq.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.genohm.boinq.web.servlet.FusekiProxyServlet;

@Configuration
@AutoConfigureAfter(CacheConfiguration.class)
public class ProxyConfiguration {
	
	@Bean
	public Servlet proxyServlet() {
		return new FusekiProxyServlet();
	}
	
	@Bean
	public ServletRegistrationBean proxyServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(proxyServlet(), "/fuseki/*");
        Map<String,String> params = new HashMap<String,String>();
        params.put("proxyHost", "localhost");
        params.put("proxyPort", "3456");
        params.put("proxyPath", "/exampledata/");
        params.put("sourcePath", "/fuseki/");
        registration.setInitParameters(params);
        return registration;
	}
	
}
