package org.boinq.tools.fileformats;

import static org.junit.Assert.assertEquals;

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
import org.boinq.generated.vocabularies.FormatVocab;
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
	public void testVCFConversion() throws Exception {
		String filePath = getClass().getResource("/inputfiles/testVCF.vcf").getFile();
		TripleConversion.Metadata meta = new TripleConversion.Metadata();
		Iterator<Triple> iterator = tripleIteratorFactory.getIterator(new File(filePath), meta);
		String graphName = localGraphService.createLocalGraph("testGraph");
		TripleUploader uploader = tripleUploadService.getUploader(localGraphService.getUpdateEndpoint(), graphName, Prefixes.getCommonPrefixes());
		while (iterator.hasNext()) {
			uploader.triple(iterator.next());
		}
		uploader.finish();
		String prefixes = "PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
						  "PREFIX rdfs: 	<http://www.w3.org/2000/01/rdf-schema#>\n" +
						  "PREFIX format: 	<http://www.boinq.org/iri/ontologies/format#>\n" + 
						  "PREFIX gfvo: 	<http://www.biointerchange.org/gfvo#>\n" +
						  "PREFIX faldo:    <http://biohackathon.org/resource/faldo#>\n" +
						  "PREFIX obo:      <http://purl.obolibrary.org/obo/>";
		String query1 = prefixes +
						"SELECT (COUNT(?entry) as ?entryCount) WHERE {?entry a format:VCF_Entry}";
		RawSPARQLResultSet result1 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query1);
		assertEquals(result1.getRecords().size(), 1);
		assertEquals(result1.getRecords().get(0).get("entryCount").asLiteral().getInt(), 5);
		String query2 = prefixes + 
				        "SELECT (COUNT(?feature) as ?featureCount) WHERE {"
				        + "?feature faldo:location [faldo:reference ?ref], "
				        + "						   [faldo:begin [faldo:position ?start]], "
				        + "						   [faldo:end [faldo:position ?end]]. "
				        + "FILTER(?start > 1110000 && ?end < 1231000)  }";
		RawSPARQLResultSet result2 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query2);
		assertEquals(result2.getRecords().size(), 1);
		assertEquals(result2.getRecords().get(0).get("featureCount").asLiteral().getInt(), 2);
		String query3 = prefixes + 
		        "SELECT (COUNT(?feature) as ?snpCount) WHERE {"
		        + "?feature a <http://purl.obolibrary.org/obo/SO_0000694>  }";
		RawSPARQLResultSet result3 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query3);
		assertEquals(result3.getRecords().size(), 1);
		assertEquals(result3.getRecords().get(0).get("snpCount").asLiteral().getInt(), 3);
		String query4 = prefixes + 
		        "SELECT * WHERE {" +
		        "?feature gfvo:isRefutedBy [rdfs:label \"q10\"]}";
		RawSPARQLResultSet result4 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query4);
		assertEquals(result4.getRecords().size(), 1);
		String query5 = prefixes +
				"SELECT ?genotype WHERE {" +
		        "?genotype a gfvo:Genotype; gfvo:hasQuality gfvo:Homozygous}";
		RawSPARQLResultSet result5 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query5);
		assertEquals(result5.getRecords().size(), 9);
		String query6 = prefixes +
				"SELECT ?feature WHERE {" +
				"?feature gfvo:hasEvidence [gfvo:hasSource [rdfs:label \"NA00001\"];" +
				"         					gfvo:hasAttribute [a gfvo:Genotype; " +
				"											   gfvo:hasAttribute [a gfvo:ConditionalGenotypeQuality; " +
				"																  rdf:value ?GQ]]]." +
				"FILTER (?GQ > 40)" +
				"}"	;
		RawSPARQLResultSet result6 = sparqlClient.rawQuery(localGraphService.getSparqlEndpoint(), graphName, query6);
		assertEquals(result6.getRecords().size(), 3);
	}

}
