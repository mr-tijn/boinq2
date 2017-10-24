package org.boinq.domain.jobs.analysis;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.boinq.domain.jobs.AsynchronousJob;
import org.boinq.web.rest.dto.AnalysisDTO;

@Entity
@Table(name="T_ANALYSIS")
@DiscriminatorColumn(name="type")
public abstract class Analysis implements AsynchronousJob {

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(name="name")
	private String name;

	@Column(name="status")
	private int status;
	
	@Lob
	@Column(name="description")
	private String description;
	
	@Lob
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
