package org.boinq.tools.fileformats;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.jena.graph.Triple;
import org.boinq.Application;
import org.boinq.domain.RawSPARQLResultSet;
import org.boinq.domain.jobs.TripleConversion;
import org.boinq.init.TripleStoreInitializer;
import org.boinq.service.FusekiMgmtService;
import org.boinq.service.LocalGraphService;
import org.boinq.service.SPARQLClientService;
import org.boinq.service.TripleUploadService;
import org.boinq.service.TripleUploadService.TripleUploader;
import org.boinq.tools.fileformats.TripleIteratorFactory;
import org.boinq.tools.queries.Prefixes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class VCFConverterTest {

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
		String filePath = getClass().getResource("/inputfiles/testVCF.vcf").getFile();
		TripleConversion.Metadata meta = new TripleConversion.Metadata();
		Iterator<Triple> iterator = tripleIteratorFactory.getIterator(new File(filePath), null);
		String graphName = localGraphService.createLocalGraph("testGraph");
		TripleUploader uploader = tripleUploadService.getUploader(localGraphService.getUpdateEndpoint(), graphName, Prefixes.getCommonPrefixes());
		while (iterator.hasNext()) {
			uploader.triple(iterator.next());
		}
		uploader.finish();
		String query1 = "PREFIX track: <http://www.boinq.org/track#> " +
						"SELECT COUNT(?feature) WHERE {?feature a track:BedFeature}";
		RawSPARQLResultSet result1 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query1);
	}

}
