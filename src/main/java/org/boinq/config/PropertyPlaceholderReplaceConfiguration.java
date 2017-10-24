package org.boinq.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyPlaceholderReplaceConfiguration implements ApplicationContextAware {

	private static Logger log = LoggerFactory.getLogger(PropertyPlaceholderReplaceConfiguration.class);
	private static final String PROPERTY_BASE = "spring.propertyplaceholder.";
	private ConfigurableListableBeanFactory factory;
	private RelaxedPropertyResolver resolver;
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		factory = ((ConfigurableApplicationContext) context).getBeanFactory();
		resolver = new RelaxedPropertyResolver(context.getEnvironment(),PROPERTY_BASE);
	}
	
	@PostConstruct
	public void init() {
		int i = 0;
		Map<String,Object> templateFile = resolver.getSubProperties(String.format("templates[%d]",i++));
		while (templateFile != null && templateFile.size() > 0) {
			replacePlaceholders((String) templateFile.get(".template"), (String) templateFile.get(".target"));
			templateFile = resolver.getSubProperties(String.format("templates[%d]",i++));			
		}
	}
	
	public void replacePlaceholders(String templateFile, String targetFile) {
		InputStream inFile = this.getClass().getClassLoader().getResourceAsStream(templateFile);
		File outFile = new File(targetFile);
		BufferedReader in = null;
		BufferedWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(inFile));
			out = new BufferedWriter(new FileWriter(outFile));
			String line = in.readLine();
			while (line != null) {
				out.write(factory.resolveEmbeddedValue(line));
				out.write("\n");
				line = in.readLine();
			}
		} catch (Exception e) {
			log.error("Could not replace placeholders for "+templateFile+" into "+targetFile, e);
		} finally {
			try {
				if (null != in) in.close();
				if (null != out) out.close();
			} catch (IOException e) {
				log.error("unable to close stream");
			}
		}
	}
	
}
