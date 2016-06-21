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
		for (Datasource ds: datasourceRepository.findAll()) {
			replaceFields(ds);
		}
	}

	private void replaceFields(Datasource ds) {
		Boolean changed = false;
		if ("DSNAME_PLACEHOLDER".equals(ds.getIri())) {
			ds.setIri(propertyResolver.getProperty("triplestore.localdatasource"));
			changed = true;
		}
		if ("DSENDPOINT_PLACEHOLDER".equals(ds.getEndpointUrl())) {
			ds.setEndpointUrl(propertyResolver.getProperty("triplestore.endpoint.data.query"));
			changed = true;
		}
		if ("DSENDPOINT_UPDATE_PLACEHOLDER".equals(ds.getEndpointUpdateUrl())) {
			ds.setEndpointUpdateUrl(propertyResolver.getProperty("triplestore.endpoint.data.update"));
			changed = true;
		}
		if ("DSENDPOINT_META_PLACEHOLDER".equals(ds.getMetaEndpointUrl())) {
			ds.setMetaEndpointUrl(propertyResolver.getProperty("triplestore.endpoint.meta.query"));
			changed = true;
		}
		if ("DSENDPOINT_META_UPDATE_PLACEHOLDER".equals(ds.getMetaEndpointUpdateUrl())) {
			ds.setMetaEndpointUpdateUrl(propertyResolver.getProperty("triplestore.endpoint.meta.update"));
			changed = true;
		}
		if (changed) {
			datasourceRepository.save(ds);
		}

	}
	
	@Override
	public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.");
	}
	
	
}
