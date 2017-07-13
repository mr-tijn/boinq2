package com.genohm.boinq.domain.jobs.analysis;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;
import javax.transaction.Transactional;

import com.genohm.boinq.domain.query.QueryDefinition;
import com.genohm.boinq.repository.QueryDefinitionRepository;
import com.genohm.boinq.service.GenerateQueryService;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.service.PagedQueryService;
import com.genohm.boinq.service.PagedQueryService.PagedQuery;
import com.genohm.boinq.web.rest.dto.AnalysisDTO;

public class QueryExecution extends Analysis {

	@Inject
	private GenerateQueryService generateQueryService;
	@Inject
	private PagedQueryService pagedQueryService;
	@Inject
	private LocalGraphService localGraphService;
	@Inject
	private QueryDefinitionRepository queryDefinitionRepository;
	
	private QueryDefinition definition;
	private Boolean interrupted;
	private Date startDate;
	private Date endDate;
	
	public QueryExecution(QueryDefinition definition) {
		this.definition = definition;
	}
	
	@Override
	@Transactional
	public void execute() {
		if (definition.getSparqlQuery() == null) {
			definition.setSparqlQuery(generateQueryService.generateQuery(definition));
		}
		if (definition.getResultAsTable()) {
			definition.setStatus(JOB_STATUS_COMPUTING);
			queryDefinitionRepository.save(definition);
			try {
				PagedQuery pagedQuery = pagedQueryService.create("result.csv",definition.getSparqlQuery(),localGraphService.getSparqlEndpoint(),null);
				pagedQuery.init();
				do {
					pagedQuery.next();
				} while (!interrupted && pagedQuery.hasNext());
				if (interrupted) {
					definition.setStatus(JOB_STATUS_INTERRUPTED);
					queryDefinitionRepository.save(definition);
				} else {
					definition.setTargetFile(pagedQuery.getFile().getAbsolutePath());
					definition.setStatus(JOB_STATUS_SUCCESS);
					queryDefinitionRepository.save(definition);
				}
			} catch (Exception e) {
				definition.setStatus(JOB_STATUS_ERROR);
				queryDefinitionRepository.save(definition);
			}
		}
		
	}

	@Override
	public void kill() {
		this.interrupted = true;
	}
	
	@Override
	public AnalysisDTO createDTO() {
		AnalysisDTO result = super.createDTO();
		return result;
	}

	@Override
	public Long getDuration() {
		return endDate.getTime() - startDate.getTime();
	}

}
