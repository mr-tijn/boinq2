package com.genohm.boinq.service;

import static com.genohm.boinq.domain.query.TemplateFactory.entityNode;
import static com.genohm.boinq.domain.query.TemplateFactory.literalNode;
import static com.genohm.boinq.domain.query.TemplateFactory.typeEdgeTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.sparql.modify.request.UpdateVisitor;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.genohm.boinq.Application;
import com.genohm.boinq.domain.query.EdgeTemplate;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.domain.query.NodeFilter;
import com.genohm.boinq.domain.query.NodeTemplate;
import com.genohm.boinq.domain.query.QueryBridge;
import com.genohm.boinq.domain.query.QueryDefinition;
import com.genohm.boinq.domain.query.QueryEdge;
import com.genohm.boinq.domain.query.QueryGraph;
import com.genohm.boinq.domain.query.QueryNode;
import com.genohm.boinq.generated.vocabularies.FaldoVocab;
import com.genohm.boinq.generated.vocabularies.FormatVocab;
import com.genohm.boinq.generated.vocabularies.GfvoVocab;
import com.genohm.boinq.generated.vocabularies.SioVocab;
import com.genohm.boinq.generated.vocabularies.SoVocab;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class GenerateQueryServiceTest {

	@Inject
	private GenerateQueryService queryService; 
	

	@Test
	public void test() {
		QueryDefinitionTestData testData = new QueryDefinitionTestData();
		String resultString = queryService.generateQuery(testData.qd);
		// parse and verify
//		Query resultQuery = QueryFactory.create(resultString);
//		assertTrue(resultQuery.getQueryType() == Query.QueryTypeSelect);
		UpdateRequest resultUpdate = UpdateFactory.create(resultString);
		// only insert
		assertTrue(resultUpdate.getOperations().size() == 1);
		Update insert = resultUpdate.getOperations().get(0);
		assertTrue(insert instanceof UpdateModify);
		UpdateModify modify = (UpdateModify) insert;
		assertFalse(modify.hasDeleteClause());
		assertTrue(modify.hasInsertClause());
		assertTrue(modify.getInsertQuads().size() == 4);
		ElementGroup where = (ElementGroup) modify.getWherePattern();
		assertTrue(where.getElements().size() == 5);
	}

}
