package org.boinq.tools.generators;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.inject.Inject;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.boinq.domain.Datasource;
import org.boinq.domain.GenomicRegion;
import org.boinq.domain.Track;
import org.boinq.domain.match.FeatureQuery;
import org.boinq.domain.match.FeatureSelect;
import org.boinq.domain.match.FeatureTypeCriterion;
import org.boinq.domain.match.LocationOverlap;
import org.boinq.domain.match.MatchDecimalCriterion;
import org.boinq.domain.match.MatchStringCriterion;
import org.boinq.domain.match.MatchTermCriterion;
import org.boinq.service.TripleUploadService;
import org.boinq.service.TripleUploadService.TripleUploader;
import org.boinq.tools.generators.SPARQLQueryGenerator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import org.boinq.generated.vocabularies.SoVocab;

public class SPARQLQueryGeneratorTest {

	@Inject 
	private TripleUploadService tripleUploadService;
	@Value("${spring.triplestore.endpoint.data.update}")
	private String updateEndpoint;
	
	private FeatureQuery featureQueryNoCriteria = new FeatureQuery();
	private FeatureQuery featureQueryTypeFilter = new FeatureQuery();
	private FeatureQuery featureQueryTermFilter = new FeatureQuery();
	private FeatureQuery featureQueryOverlap = new FeatureQuery();
	private GenomicRegion testRegion = new GenomicRegion();
	private Track track1 = new Track();
	private Track track2 = new Track();
	private Datasource datasourceRemote = new Datasource();
	private Datasource datasourceLocal = new Datasource();
	
	@Before
	public void initialize() {
		FeatureSelect noCriteria = new FeatureSelect();
		noCriteria.setRetrieveFeatureData(true);
		noCriteria.setTrack(track1);
		featureQueryNoCriteria.addSelect(noCriteria);
		featureQueryNoCriteria.setTargetGraph("http://nocriteria.com");
		testRegion.assemblyURI = "http://www.boinq.org/resource/homo_sapiens/GRCh38";
		testRegion.start = 0L;
		testRegion.end = 10000L;
		testRegion.strand = true;
		track1.setName("testTrack");
		track1.setGraphName("http://www.boinq.org/iri/testGraph");
		track1.setDatasource(datasourceRemote);
		datasourceRemote.setType(Datasource.TYPE_REMOTE_FALDO);
		datasourceRemote.setEndpointUrl("http://remote/sparql");
		track2.setName("localTrack");
		track2.setGraphName("http://www.boinq.org/iri/localGraph");
		track2.setDatasource(datasourceLocal);
		datasourceLocal.setType(Datasource.TYPE_LOCAL_FALDO);
		datasourceLocal.setEndpointUrl("http://localhost:8888");
		
	}
	
//	@Before
	public void uploadTestData() throws IOException {
		InputStream file = this.getClass().getClassLoader().getResourceAsStream("/inputfiles/testdata.ttl");
		TripleUploader uploader = tripleUploadService.getUploader(updateEndpoint, "<http://test>");
		RDFDataMgr.parse(uploader, file, Lang.TTL);
		file.close();
	}
	
	@Test
	public void noCriteriaSingleSelect() {
		SPARQLQueryGenerator generator = new SPARQLQueryGenerator();
		Query noCriteriaQuery = generator.computeQuery(featureQueryNoCriteria, testRegion);
		System.out.println(noCriteriaQuery.toString());
	}
	
	@Test
	public void typeCriteriaSelect() {
		SPARQLQueryGenerator generator = new SPARQLQueryGenerator();
		featureQueryTypeFilter = new FeatureQuery();
		featureQueryTypeFilter.setTargetGraph("http://typefilter.com");
		FeatureSelect mainSelect = new FeatureSelect();
		mainSelect.setRetrieveFeatureData(true);
		mainSelect.setTrack(track1);
		FeatureTypeCriterion typeCriterion = new FeatureTypeCriterion();
		typeCriterion.setFeatureTypeUri("<http://type>");
		typeCriterion.setFeatureTypeLabel("type label");
		mainSelect.addCriteria(typeCriterion);
		featureQueryTypeFilter.addSelect(mainSelect);
		Query typeCriterionQuery = generator.computeQuery(featureQueryTypeFilter, testRegion);
		System.out.println(typeCriterionQuery.toString());
	}
	
	@Test
	public void matchTermSelect() {
		String pathExpression = "(rdf:type|rdfs:subClassOf)*";
		String matchUri = OWL.Thing.toString();
		SPARQLQueryGenerator generator = new SPARQLQueryGenerator();
		featureQueryTermFilter = new FeatureQuery();
		featureQueryTermFilter.setTargetGraph("http://termfilterterm.com");
		FeatureSelect mainSelect = new FeatureSelect();
		mainSelect.setRetrieveFeatureData(true);
		mainSelect.setTrack(track1);
		MatchTermCriterion termCriterion = new MatchTermCriterion();
		termCriterion.setExactMatch(false);
		termCriterion.setTermUri(matchUri);
		termCriterion.setTermLabel(OWL.Thing.getLocalName());
		termCriterion.setPathExpression(pathExpression);
		mainSelect.addCriteria(termCriterion);
		featureQueryTermFilter.addSelect(mainSelect);
		Query typeCriterionQuery = generator.computeQuery(featureQueryTermFilter, testRegion);
		System.out.println(typeCriterionQuery.toString());
		
	}

	
	@Test
	public void matchNumberSelect() {
		String pathExpression = "(rdf:type|rdfs:subClassOf)*";
		SPARQLQueryGenerator generator = new SPARQLQueryGenerator();
		featureQueryTermFilter = new FeatureQuery();
		featureQueryTermFilter.setTargetGraph("http://termfilternumber.com");
		FeatureSelect mainSelect = new FeatureSelect();
		mainSelect.setRetrieveFeatureData(true);
		mainSelect.setTrack(track1);
		MatchDecimalCriterion decimalCriterion = new MatchDecimalCriterion();
		decimalCriterion.setPathExpression(pathExpression);
		decimalCriterion.setExactMatch(false);
		decimalCriterion.setMinDouble(-.7);
		mainSelect.addCriteria(decimalCriterion);
		featureQueryTermFilter.addSelect(mainSelect);
		Query typeCriterionQuery = generator.computeQuery(featureQueryTermFilter, testRegion);
		System.out.println(typeCriterionQuery.toString());
		decimalCriterion.setMaxDouble(7e4);
		typeCriterionQuery = generator.computeQuery(featureQueryTermFilter, testRegion);
		System.out.println(typeCriterionQuery.toString());
		decimalCriterion.setExactMatch(true);
		decimalCriterion.setMatchDouble(3.2);
		typeCriterionQuery = generator.computeQuery(featureQueryTermFilter, testRegion);
		System.out.println(typeCriterionQuery.toString());
		
	}

	@Test
	public void matchStringSelect() {
		String pathExpression = "dcterms:identifier";
		SPARQLQueryGenerator generator = new SPARQLQueryGenerator();
		featureQueryTermFilter = new FeatureQuery();
		featureQueryTermFilter.setTargetGraph("http://termfilterstring.com");
		FeatureSelect mainSelect = new FeatureSelect();
		mainSelect.setRetrieveFeatureData(true);
		mainSelect.setTrack(track1);
		MatchStringCriterion stringCriterion = new MatchStringCriterion();
		stringCriterion.setPathExpression(pathExpression);
		stringCriterion.setExactMatch(false);
		stringCriterion.setMatchString("match this !");
		mainSelect.addCriteria(stringCriterion);
		featureQueryTermFilter.addSelect(mainSelect);
		Query typeCriterionQuery = generator.computeQuery(featureQueryTermFilter, testRegion);
		System.out.println(typeCriterionQuery.toString());
		stringCriterion.setExactMatch(true);
		typeCriterionQuery = generator.computeQuery(featureQueryTermFilter, testRegion);
		System.out.println(typeCriterionQuery.toString());
	}

	@Test
	public void overlapSelect() {
		SPARQLQueryGenerator generator = new SPARQLQueryGenerator();
		featureQueryOverlap = new FeatureQuery();
		featureQueryOverlap.setTargetGraph("http://overlap.com");
		FeatureSelect firstSelect = new FeatureSelect();
		firstSelect.setRetrieveFeatureData(true);
		HashMap<Node,Node> referenceMap1 = new HashMap<>();
		Node chr1 = NodeFactory.createURI("http://www.boinq.org/resource/homo_sapiens/GRCh38/1");
		Node chr2 = NodeFactory.createURI("http://www.boinq.org/resource/homo_sapiens/GRCh38/2");
		referenceMap1.put(chr1, NodeFactory.createURI("http://track1/chrom1"));
		referenceMap1.put(chr2, NodeFactory.createURI("http://track1/chrom2"));
		track1.setReferenceMap(referenceMap1);
		firstSelect.setTrack(track1);
		FeatureTypeCriterion typeCriterion = new FeatureTypeCriterion();
		typeCriterion.setFeatureTypeUri(SoVocab.gene.toString());
		typeCriterion.setFeatureTypeLabel("gene");
		firstSelect.addCriteria(typeCriterion);
		FeatureSelect secondSelect = new FeatureSelect();
		secondSelect.setRetrieveFeatureData(false);
		HashMap<Node,Node> referenceMap2 = new HashMap<>();
		referenceMap2.put(chr1, NodeFactory.createURI("http://track2/GRCh38#chromosome1"));
		referenceMap2.put(chr2, NodeFactory.createURI("http://track2/GRCh38#chromosome2"));
		track2.setReferenceMap(referenceMap2);
		secondSelect.setTrack(track2);
		LocationOverlap overlap = new LocationOverlap();
		overlap.setSource(firstSelect);
		overlap.setTarget(secondSelect);
		overlap.setSameStrand(true);
		featureQueryOverlap.addSelect(firstSelect);
		featureQueryOverlap.addSelect(secondSelect);
		featureQueryOverlap.addJoin(overlap);
		Query overlapQuery = generator.computeQuery(featureQueryOverlap, testRegion);
		System.out.println("Overlap Query:");
		System.out.println(overlapQuery.toString());
		
	}
}
