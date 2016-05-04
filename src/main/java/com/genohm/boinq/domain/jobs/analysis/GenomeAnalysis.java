package com.genohm.boinq.domain.jobs.analysis;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;

import com.genohm.boinq.web.rest.dto.AnalysisDTO;

@Entity
public abstract class GenomeAnalysis extends Analysis {
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="T_PROGRESS", joinColumns=@JoinColumn(name="analysis_id"))
	@MapKeyColumn(name="reference")
	@Column(name="percentage")
	protected Map<String, Double> progressPercentages = new HashMap<>();
	
	public Double getProgressPercentage(String assemblyURI) {
		return progressPercentages.get(assemblyURI);
	}
	public Map<String, Double> getProgressPercentages() {
		return progressPercentages;
	}

	@Override
	public AnalysisDTO createDTO() {
		AnalysisDTO result = super.createDTO();
		result.progressPercentages = new HashMap<>();
		for (String ref: this.progressPercentages.keySet()) {
			result.progressPercentages.put(ref, this.progressPercentages.get(ref));
		}
		return result;
	}
	
}
