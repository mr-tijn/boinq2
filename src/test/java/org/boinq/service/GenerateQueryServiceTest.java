package org.boinq.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementService;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.boinq.Application;
import org.boinq.service.GenerateQueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
		String resultString = queryService.generateQuery(testData.locationOverlapQuery);
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
		assertTrue(modify.getInsertQuads().size() == 2*8+1);
		ElementGroup where = (ElementGroup) modify.getWherePattern();
		assertTrue(where.getElements().size() == 2+3);
		List<Element> filterElements = where.getElements().stream().filter(el -> (el instanceof ElementFilter)).collect(Collectors.toList());
		assertTrue(filterElements.size() == 3);
		List<Element> serviceElements = where.getElements().stream().filter(el -> (el instanceof ElementService)).collect(Collectors.toList());
		assertTrue(serviceElements.size() == 1);
		ElementService serviceElement = (ElementService) serviceElements.get(0);
		Element element = serviceElement.getElement();
		assertTrue(element instanceof ElementGroup);
		ElementGroup remoteGroup = (ElementGroup) element;
		List<Element> remoteGroupElements = remoteGroup.getElements();
		List<Element> remoteGraphs = remoteGroupElements.stream().filter(el -> (el instanceof ElementNamedGraph)).collect(Collectors.toList());
		assertTrue(remoteGraphs.size() == 1);
		ElementNamedGraph remoteGraph = (ElementNamedGraph) remoteGraphs.get(0);
		element = remoteGraph.getElement();
		assertTrue(element instanceof ElementGroup);
		List<Element> remoteGraphElements = ((ElementGroup) element).getElements();
		assertTrue(remoteGraphElements.size() == 3);
		List<Element> filters = remoteGraphElements.stream().filter(el -> (el instanceof ElementFilter)).collect(Collectors.toList());
		assertTrue(filters.size() == 2); // rank = 1; default faldo position type
		List<Element> blocks = remoteGraphElements.stream().filter(el -> (el instanceof ElementPathBlock)).collect(Collectors.toList());
		assertTrue(blocks.size() == 1);
		ElementPathBlock pathBlock = (ElementPathBlock) blocks.get(0);
		assertTrue(pathBlock.getPattern().size() == 8+2); // faldo ; orderedPart
		List<Element> graphElements = where.getElements().stream().filter(el -> (el instanceof ElementNamedGraph)).collect(Collectors.toList());
		assertTrue(graphElements.size() == 1);
		ElementNamedGraph localGraph = (ElementNamedGraph) graphElements.get(0);
		assertTrue(localGraph.getElement() instanceof ElementGroup);
		List<Element> localGraphElements = ((ElementGroup) localGraph.getElement()).getElements();
		List<Element> localFilters = localGraphElements.stream().filter(el -> (el instanceof ElementFilter)).collect(Collectors.toList());
		assertTrue(localFilters.size() == 2); // type; default faldo filter
		List<Element> localBlocks = localGraphElements.stream()
				.filter(el -> (el instanceof ElementPathBlock))
				.collect(Collectors.toList());
		assertTrue(localBlocks.size() == 1);
		ElementPathBlock localPathBlock = (ElementPathBlock) localBlocks.get(0);
		assertTrue(localPathBlock.getPattern().size() == 8+1); // faldo + featuretype
	}

}
