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
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

@Service
public class SPARQLClientService {

	private static final Logger log = Logger.getLogger(SPARQLClientService.class);
	
	public SPARQLResultSet query(String serviceURL, String graphURL, Query query) throws Exception {
		switch (query.getQueryType()) {
		case Query.QueryTypeSelect:
			return querySelect(serviceURL, graphURL, query.toString(Syntax.syntaxSPARQL));
		case Query.QueryTypeAsk:
			return queryAsk(serviceURL, graphURL, query.toString(Syntax.syntaxSPARQL));
		default:
			throw new Exception("Currently only select and ask are handled");
		}
	}
	
	public RawSPARQLResultSet rawQuery(String serviceURL, String graphURL, String queryString, Boolean subClassReasoning, Boolean subPropertyReasoning) throws Exception {
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
			if (graphURL != null) {
				((QueryEngineHTTP) qe).addDefaultGraph(graphURL);
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
	
	public SPARQLResultSet querySelect(String serviceURL, String graphURL, String queryString, Boolean subClassReasoning, Boolean subPropertyReasoning) throws Exception {
		RawSPARQLResultSet rrs = rawQuery(serviceURL, graphURL, queryString, subClassReasoning, subPropertyReasoning);
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
	
	public RawSPARQLResultSet rawQuery(String serviceURL, String graphURL, Query query) throws Exception {
		return rawQuery(serviceURL, graphURL, query.toString(Syntax.syntaxSPARQL), false, false);
	}

	public RawSPARQLResultSet rawQuery(String serviceURL, String graphURL, String query) throws Exception {
		return rawQuery(serviceURL, graphURL, query, false, false);
	}
	
	public SPARQLResultSet querySelect(String serviceURL, String graphURL, String query) throws Exception {
		return querySelect(serviceURL, graphURL, query, false, false);
	}
	
	public SPARQLResultSet queryAsk(String serviceURL, String graphURL, String queryString) throws Exception {
		QueryExecution qe = null;
		Boolean resultAsk = null;
		try {
			qe = new QueryEngineHTTP(serviceURL, queryString);
			if (graphURL != null) {
				((QueryEngineHTTP) qe).addDefaultGraph(graphURL);
			}
			resultAsk = qe.execAsk();
		} catch (Exception e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			String error = "Could not perform query "+queryString+"\n"+e.getMessage()+"\n"+baos.toString();
			log.error(error);
			throw new Exception((Throwable) e); //recast to general type to ensure serialization
		} finally {
			if (qe != null) qe.close();
		}
		SPARQLResultSet result = new SPARQLResultSet();
		result.setAskResult(resultAsk);
		return result;
	}
	
}
