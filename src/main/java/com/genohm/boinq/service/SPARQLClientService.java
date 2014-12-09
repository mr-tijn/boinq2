package com.genohm.boinq.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.genohm.boinq.domain.RawSPARQLResultSet;
import com.genohm.boinq.domain.SPARQLResultSet;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

@Service
public class SPARQLClientService {

	private static final Logger log = Logger.getLogger(SPARQLClientService.class);
	
	public SPARQLResultSet query(String serviceURL, Query query) throws Exception {
		return query(serviceURL, query.toString(Syntax.syntaxSPARQL));
	}
	
	public RawSPARQLResultSet rawQuery(String serviceURL, String queryString, Boolean subClassReasoning, Boolean subPropertyReasoning) throws Exception {
		QueryExecution qe = null;
		List<Map<String, RDFNode>> resultList = null;
		List<String> varList = null;
		
		String rules = "";
		if (subClassReasoning) rules += "SUBC+";
		if (subPropertyReasoning) rules += "SUBP";
		if (rules.endsWith("+")) rules = rules.substring(0, rules.length()-1);
		
		try {
			
			qe = new QueryEngineHTTP(serviceURL, queryString);
			if (rules.length() > 0) {
				((QueryEngineHTTP) qe).addParam("rules", rules);
			}
			ResultSet rs = qe.execSelect();
			resultList = new LinkedList<Map<String,RDFNode>>();
			varList = rs.getResultVars();
			while (rs.hasNext()) {
				Map<String,RDFNode> result = new HashMap<String, RDFNode>();
				QuerySolution qs = rs.nextSolution();
				for (String var: varList) {
					result.put(var, qs.get(var));
				}
				resultList.add(result);
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String error = "Could not perform query "+queryString+"\n"+e.getMessage()+"\n"+baos.toString();
			log.error(error);
			throw new Exception((Throwable) e); //recast to general type to ensure serialization
		} finally {
			if (qe != null) qe.close();
		}
		RawSPARQLResultSet srs = new RawSPARQLResultSet();
		srs.setRecords(resultList);
		srs.setVariableNames(varList);
		return srs;		
	}
	
	public SPARQLResultSet query(String serviceURL, String queryString, Boolean subClassReasoning, Boolean subPropertyReasoning) throws Exception {
		RawSPARQLResultSet rrs = rawQuery(serviceURL, queryString, subClassReasoning, subPropertyReasoning);
		List<String> varList = rrs.getVariableNames();
		List<Map<String, String>> resultList = new LinkedList<Map<String,String>>();
		SPARQLResultSet rs = new SPARQLResultSet();
		rs.setVariableNames(rrs.getVariableNames());
		for (Map<String,RDFNode> record: rrs.getRecords()) {
			Map<String,String> result = new HashMap<String, String>();
			for (String var: varList) {
				if (record.get(var) != null) {
					if (record.get(var).isLiteral()) result.put(var,record.get(var).asLiteral().getValue().toString());
					else result.put(var, record.get(var).toString());
				}
			}
			resultList.add(result);
		}
		rs.setRecords(resultList);
		return rs;
	}
	
	public RawSPARQLResultSet rawQuery(String serviceURL, Query query) throws Exception {
		return rawQuery(serviceURL, query.toString(Syntax.syntaxSPARQL), false, false);
	}

	public RawSPARQLResultSet rawQuery(String serviceURL, String query) throws Exception {
		return rawQuery(serviceURL, query, false, false);
	}
	
	public SPARQLResultSet query(String serviceURL, String query) throws Exception {
		return query(serviceURL, query, false, false);
	}
	
	public List<Map<String,RDFNode>> queryForListOfNodeMaps(String serviceURL, String query) throws Exception {
		QueryExecution qe = null;
		List<Map<String, RDFNode>> resultList = null;
		try {
			qe = QueryExecutionFactory.sparqlService(serviceURL, query);
			ResultSet rs = qe.execSelect();
			resultList = new LinkedList<Map<String,RDFNode>>();
			List<String> varList = rs.getResultVars();
			while (rs.hasNext()) {
				Map<String,RDFNode> result = new HashMap<String, RDFNode>();
				QuerySolution qs = rs.nextSolution();
				for (String var: varList) {
					if (qs.get(var) != null) result.put(var, qs.get(var));
				}
				resultList.add(result);
			}
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String error = "Could not perform query "+query+"\n"+e.getMessage()+"\n"+baos.toString();
			log.error(error);
			throw new Exception((Throwable) e); //recast to general type to ensure serialization
		} finally {
			if (qe != null) qe.close();
		}
		return resultList;		
	}	
	
}
