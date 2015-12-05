package com.genohm.boinq.config;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.proxy.ProxyServlet;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.genohm.boinq.init.TripleStoreInitializer;

@Configuration
@AutoConfigureAfter(CacheConfiguration.class)
@AutoConfigureBefore(TripleStoreInitializer.class)
public class ProxyConfiguration implements EnvironmentAware {

	protected Environment environment;
    protected RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(Environment environment) {
    	this.environment = environment;
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.fuseki.");
	}
	
	
	@Bean
	public ServletRegistrationBean fusekiProxy() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ProxyServlet.Transparent(), "/fuseki/*");
        Map<String,String> params = new HashMap<String,String>();
        params.put("proxyTo", "http://localhost:8080/fuseki");
        params.put("prefix", "/fuseki");
        registration.setInitParameters(params);
        return registration;
	}

	
	private class StaticProxyServlet extends ProxyServlet.Transparent {
		private static final long serialVersionUID = 1L;
	}

	@Bean
	public ServletRegistrationBean eldaProxy() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new EldaProxyServlet(), "/iri/*");
        Map<String,String> params = new HashMap<String,String>();
        params.put("proxyTo", "http://localhost:8080/elda-common");
        params.put("prefix", "/iri");
        registration.setInitParameters(params);
        return registration;
	}

	private class EldaProxyServlet extends ProxyServlet.Transparent {
		private static final long serialVersionUID = 1L;
		// two ProxyServlets of same type won't work together
	}



	

	
}
