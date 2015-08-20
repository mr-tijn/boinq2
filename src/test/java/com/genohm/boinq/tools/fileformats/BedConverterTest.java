package com.genohm.boinq.tools.fileformats;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.annotation.WebInitParam;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.http.client.methods.HttpGet;
import org.apache.solr.client.solrj.request.CoreAdminRequest.WaitForState;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.qos.logback.classic.pattern.Util;

import com.genohm.boinq.Application;
import com.genohm.boinq.domain.RawSPARQLResultSet;
import com.genohm.boinq.domain.SPARQLResultSet;
import com.genohm.boinq.generated.vocabularies.TrackVocab;
import com.genohm.boinq.init.TripleStoreInitializer;
import com.genohm.boinq.service.FusekiMgmtService;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.service.SPARQLClientService;
import com.genohm.boinq.service.TripleUploadService;
import com.genohm.boinq.service.TripleUploadService.TripleUploader;
import com.genohm.boinq.tools.queries.Prefixes;
import com.genohm.boinq.tools.vocabularies.CommonVocabulary;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.TripleIterator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class BedConverterTest {

	@Inject
	TripleIteratorFactory tripleIteratorFactory;
	
	@Inject
	LocalGraphService localGraphService;
	
	@Inject
	TripleUploadService tripleUploadService;
	
	@Inject
	SPARQLClientService sparqlClient;
	
	@Inject
	TripleStoreInitializer tripleStoreInitializer;

	@Inject
	FusekiMgmtService fusekiMgmtService;
	
	@Before
	public void initTripleStore() {
		// FIXME: find a way to delay the tripleStoreInitializer to after start of the web container
		// this is needed for the proxy to be forwarding
		// the ContextRefreshedEvent is too soon
		Boolean up = false;
		Integer tries = 0;
		while (!up && tries < 10) {
			try {
				tries++;
				Thread.sleep(1000);
				URL endpoint = new URL(localGraphService.getSparqlEndpoint());
				URLConnection conn = endpoint.openConnection();
				conn.connect();
				up = true;
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
				break;
			} catch (MalformedURLException e) {
				e.printStackTrace(System.err);
				break;
			} catch (IOException e) {
				// continue
			} 
		}
		tripleStoreInitializer.checkInit();
	}
	
	@Test
	public void testBedConversion() throws Exception {
		String filePath = getClass().getResource("/inputfiles/ucsc_human_GRCh38_repeatmasker_chr9.bed").getFile();
		Map<String, Node> refMap = new HashMap<String, Node>();
		refMap.put("chr9", TrackVocab.GRCh37chr09.asNode());
		Iterator<Triple> iterator = tripleIteratorFactory.getIterator(new File(filePath), refMap);
		String graphName = localGraphService.createLocalGraph("testGraph");
		TripleUploader uploader = tripleUploadService.getUploader(localGraphService.getUpdateEndpoint(), graphName, Prefixes.getCommonPrefixes());
		while (iterator.hasNext()) {
			uploader.put(iterator.next());
		}
		String query1 = "PREFIX track: <http://www.boinq.org/track#> " +
						"SELECT COUNT(?feature) WHERE {?feature a track:BedFeature}";
		RawSPARQLResultSet result1 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query1);
	}

}
