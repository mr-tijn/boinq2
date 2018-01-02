package org.boinq.service;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.jena.query.Query;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.expr.E_Regex;
import org.apache.jena.sparql.lang.SPARQLParser;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.boinq.Application;
import org.boinq.init.TripleStoreInitializer;
import org.boinq.service.QueryBuilderService;
import org.junit.Before;
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
public class QueryBuilderServiceTest {
	@Inject
	QueryBuilderService queryBuilderService;
	
	@Inject
	TripleStoreInitializer tsInitializer;
	
	@Before
	public void init() {
		tsInitializer.checkInit();
	}
	
	@Test
	public void validQuery() {
		String qry = queryBuilderService.getFilteredTree("gene");
		SPARQLParser parser = SPARQLParser.createParser(Syntax.syntaxSPARQL_11);
		Query parsedQuery = new Query();
		parser.parse(parsedQuery, qry);
		Set<String> varNames = parsedQuery.getProjectVars().stream().map(Var::getVarName).collect(Collectors.toSet());
		assertTrue(varNames.equals(new HashSet<>(Arrays.asList("parenturi","uri","label"))));
		assertTrue(parsedQuery.isSelectType());
		assertTrue(parsedQuery.getQueryPattern() instanceof ElementGroup);
		List<Element> elements = ((ElementGroup) parsedQuery.getQueryPattern()).getElements();
		List<ElementFilter> filters = elements.stream().filter(el -> (el instanceof ElementFilter)).map(el -> (ElementFilter) el).collect(Collectors.toList());
		assertTrue(filters.size() == 1);
		ElementFilter filter = filters.get(0);
		assertTrue(filter.getExpr() instanceof E_Regex);
		E_Regex regex = (E_Regex) filter.getExpr();
		assertTrue(regex.getArgs().get(1).toString().equals("\"gene\""));
	}

}
