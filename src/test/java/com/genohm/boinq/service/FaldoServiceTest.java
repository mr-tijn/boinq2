package com.genohm.boinq.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.genohm.boinq.Application;
import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.faldo.FaldoFeature;
import com.genohm.boinq.repository.TrackRepository;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.vocabularies.CommonVocabulary;
import com.genohm.boinq.tools.vocabularies.FaldoVocabulary;
import com.genohm.boinq.tools.vocabularies.TrackVocabulary;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("dev")
public class FaldoServiceTest {

	@Inject
	FaldoService faldoService;
	
	@Inject
	TripleUploadService uploadService;
	
	Track track;
	Datasource ds;
	
	@Before
	public void init() {
		ds = new Datasource();
		ds.setEndpointUrl("http://localhost:8899/DYNAMIC/sparql");
		ds.setEndpointUpdateUrl("http://localhost:8899/DYNAMIC/sparul");
		ds.setMetaGraphName("http://www.boinq.org/META/");
		ds.setMetaEndpointUrl("http://localhost:8899/DYNAMIC/sparql");
		
		track = new Track();
		track.setDatasource(ds);
		track.setRawDataFiles(new HashSet<RawDataFile>());
		track.setGraphName("http://boinq.org/test1");
		
		HttpClient client = new HttpClient();
		TripleUploader uploader = uploadService.getUploader("http://localhost:8899/DYNAMIC/sparul", "http://www.boinq.org/track", QueryBuilderService.commonPrefixes);
		
		// read from file and put into graph
		
	}
	
	@Test
	public void test() throws Exception {
		List<FaldoFeature> features = new LinkedList<FaldoFeature>();
		for (Long l = 0L; l < 10L; l++) {
			FaldoFeature feature = new FaldoFeature();
			feature.id = "feature" + l;
			feature.assembly = TrackVocabulary.GRCh37chr01.toString();
			feature.start = 100*l;
			feature.end = feature.start+99;
			feature.strand = true;
			features.add(feature);
		}
		
		
		faldoService.writeFeatures(track, features);
		List<FaldoFeature> readBack = faldoService.getFeatures(track, TrackVocabulary.GRCh37chr01.toString(), 0L, 500L, true);
		assertThat(readBack).hasSize(10);
	}

}
