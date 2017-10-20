package com.genohm.boinq.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.genohm.boinq.Application;

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
