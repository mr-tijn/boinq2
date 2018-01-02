package org.boinq.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.Datasource;
import org.boinq.domain.RawDataFile;
import org.boinq.domain.Track;
import org.boinq.domain.faldo.FaldoFeature;
import org.boinq.service.FaldoService;
import org.boinq.service.LocalGraphService;
import org.boinq.service.TripleUploadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class FaldoServiceTest {

	@Inject
	FaldoService faldoService;
	
	@Inject
	TripleUploadService uploadService;
	
	@Inject
	LocalGraphService localGraphService;
	
	@Inject
	Environment env;
	
	Track track;
	Datasource ds;
	
	@Before
	public void init() {
		
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.");

		ds = new Datasource();
		ds.setEndpointUpdateUrl(propertyResolver.getProperty("triplestore.endpoint.data.update"));
		ds.setEndpointUrl(propertyResolver.getProperty("triplestore.endpoint.data.query"));
		ds.setMetaGraphName("http://www.boinq.org/META/");
		ds.setMetaEndpointUrl(propertyResolver.getProperty("triplestore.endpoint.meta.update"));
		
		track = new Track();
		track.setDatasource(ds);
		track.setRawDataFiles(new HashSet<RawDataFile>());
		track.setGraphName("http://boinq.org/test1");
		
		localGraphService.deleteGraph("http://boinq.org/test1");
	}
	
	@Test
	public void test() throws Exception {
		String chr1 = "http://www.boinq.org/resource/homo_sapiens/GRCh38/1";
		List<FaldoFeature> features = new LinkedList<FaldoFeature>();
		for (Long l = 0L; l < 10L; l++) {
			FaldoFeature feature = new FaldoFeature();
			feature.id = "feature" + l;
			feature.assembly = chr1;
			feature.start = 100*l;
			feature.end = feature.start+99;
			feature.strand = true;
			features.add(feature);
		}
		
		
		faldoService.writeFeatures(track, features);
		List<FaldoFeature> readBack = faldoService.getFeatures(track, chr1, 0L, 499L, true);
		assertThat(readBack).hasSize(5);
	}

}
