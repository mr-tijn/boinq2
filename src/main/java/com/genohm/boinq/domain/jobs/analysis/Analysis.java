package com.genohm.boinq.domain.jobs.analysis;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.genohm.boinq.domain.jobs.AsynchronousJob;
import com.genohm.boinq.web.rest.dto.AnalysisDTO;

@Entity
@Table(name="T_ANALYSIS")
@DiscriminatorColumn(name="type")
public abstract class Analysis implements AsynchronousJob {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@Column(name="name")
	private String name;

	@Column(name="status")
	private int status;
	
	@Column(name="description")
	private String description;
	
	@Column(name="error_description")
	protected String errorDescription;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getErrorDescription() {
		return errorDescription;
	}
	
	public AnalysisDTO createDTO() {
		AnalysisDTO result = new AnalysisDTO();
		result.description = getDescription();
		result.name = getName();
		result.errorDescription = getErrorDescription();
		result.status = getStatus();
		return result;
	}
	
}
