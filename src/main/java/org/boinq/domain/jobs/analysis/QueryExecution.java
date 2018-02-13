package org.boinq.domain.jobs.analysis;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.boinq.domain.Datasource;
import org.boinq.domain.Track;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.QueryDefinition;
import org.boinq.domain.query.QueryGraph;
import org.boinq.repository.DatasourceRepository;
import org.boinq.repository.GraphTemplateRepository;
import org.boinq.repository.QueryDefinitionRepository;
import org.boinq.repository.TrackRepository;
import org.boinq.service.GenerateQueryService;
import org.boinq.service.LocalGraphService;
import org.boinq.service.PagedQueryService;
import org.boinq.service.PagedQueryService.PagedQuery;
import org.boinq.service.SPARQLClientService;
import org.boinq.web.rest.dto.AnalysisDTO;


public class QueryExecution extends Analysis {

	private static final Long LOCALDATASOURCE_ID = 1L;
	@Inject
	private GenerateQueryService generateQueryService;
	@Inject
	private PagedQueryService pagedQueryService;
	@Inject
	private LocalGraphService localGraphService;
	@Inject
	private QueryDefinitionRepository queryDefinitionRepository;
	@Inject
	private TrackRepository trackRepository;
	@Inject
	private DatasourceRepository datasourceRepository;
	@Inject
	private SPARQLClientService sparqlClientService;
	@Inject
	private GraphTemplateRepository graphTemplateRepository;
	
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
		String query = null;
		// first find out type of templates
		// needs refactoring to put that info more easily available
		// and to remove redundant typing
		for (GraphTemplate template: definition.getQueryGraphs().stream().map(QueryGraph::getTemplate).collect(Collectors.toSet())) {
			Optional<Datasource> findDS = trackRepository.findOneByGraphTemplateId(template.getId())
					.flatMap(t -> datasourceRepository.findOneByTracksId(t.getId()));
			if (findDS.isPresent()) {
				Datasource datasource = findDS.get();
				switch (datasource.getType()) {
				case Datasource.TYPE_LOCAL:
					template.setType(GraphTemplate.GRAPH_TYPE_LOCAL);
					break;
				case Datasource.TYPE_REMOTE:
					template.setType(GraphTemplate.GRAPH_TYPE_REMOTE);
				}
				template.setEndpointUrl(datasource.getEndpointUrl());
				graphTemplateRepository.deepsave(template);
			}
		}
		if (definition.getSparqlQuery() == null || definition.getSparqlQuery().length() == 0) {
			query = generateQueryService.generateQuery(definition);
		} else {
			query = definition.getSparqlQuery();
		}
		if (definition.getResultAsTable()) {
			definition.setStatus(JOB_STATUS_COMPUTING);
			queryDefinitionRepository.deepsave(definition);
			try {
				PagedQuery pagedQuery = pagedQueryService.create("result.csv",query,localGraphService.getSparqlEndpoint(),null);
				pagedQuery.init();
				do {
					pagedQuery.next();
				} while (!interrupted && pagedQuery.hasNext());
				if (interrupted) {
					definition.setStatus(JOB_STATUS_INTERRUPTED);
					queryDefinitionRepository.deepsave(definition);
				} else {
					definition.setTargetFile(pagedQuery.getFile().getAbsolutePath());
					definition.setStatus(JOB_STATUS_SUCCESS);
					queryDefinitionRepository.deepsave(definition);
				}
			} catch (Exception e) {
				definition.setStatus(JOB_STATUS_ERROR);
				queryDefinitionRepository.deepsave(definition);
			}
		} else {
			try {
				// generate track
				localGraphService.createLocalGraph(definition.getTargetGraph());
				GraphTemplate newGraphTemplate = generateQueryService.buildGraphTemplate(definition);
				if (newGraphTemplate == null) {
					throw new Exception("Could not create graph template");
				}
				GraphTemplate graphTemplate = graphTemplateRepository.deepsave(generateQueryService.buildGraphTemplate(definition));
				Track newTrack = new Track();
				newTrack.setName(getName());
				newTrack.setDescription("Auto generated track resulting from querydefinition " + definition.getName());
				newTrack.setGraphTemplate(graphTemplate);
				trackRepository.save(newTrack);
				Datasource local = datasourceRepository.findOneById(LOCALDATASOURCE_ID).get();
				local.getTracks().add(newTrack);
				datasourceRepository.save(local);
				// fill track
				sparqlClientService.queryUpdate(local.getEndpointUpdateUrl(), query);
			} catch (Exception e) {
				definition.setStatus(JOB_STATUS_ERROR);
				queryDefinitionRepository.deepsave(definition);
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
