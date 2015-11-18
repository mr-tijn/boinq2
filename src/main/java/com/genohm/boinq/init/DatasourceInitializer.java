package com.genohm.boinq.init;

import javax.inject.Inject;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.repository.DatasourceRepository;

@Component
public class DatasourceInitializer implements EnvironmentAware, ApplicationListener<ContextRefreshedEvent> {

	@Inject
	DatasourceRepository datasourceRepository;
	
	private RelaxedPropertyResolver propertyResolver;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Datasource exampleDS = datasourceRepository.findOne(1L);
		Boolean changed = false;
		if ("DSENDPOINT_PLACEHOLDER".equals(exampleDS.getEndpointUrl())) {
			exampleDS.setEndpointUrl(propertyResolver.getProperty("triplestore.endpoint.external"));
			changed = true;
		}
		if ("DSNAME_PLACEHOLDER".equals(exampleDS.getIri())) {
			exampleDS.setIri(propertyResolver.getProperty("triplestore.localdatasource"));
			changed = true;
		}
		if ("DSENDPOINT_UPDATE_PLACEHOLDER".equals(exampleDS.getEndpointUpdateUrl())) {
			exampleDS.setEndpointUpdateUrl(propertyResolver.getProperty("triplestore.endpoint.meta"));
			changed = true;
		}
		if ("DSENDPOINT_META_PLACEHOLDER".equals(exampleDS.getMetaEndpointUrl())) {
			exampleDS.setMetaEndpointUrl(propertyResolver.getProperty("triplestore.endpoint.update"));
			changed = true;
		}
		if (changed) {
			datasourceRepository.save(exampleDS);
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.");
	}
	
	
}
