package org.boinq.web.rest.dto;

import org.boinq.domain.jobs.AsynchronousJob;

public class JobDTO {
	private String name;
	private String description;
	private int status;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public JobDTO() {}

	public JobDTO(String name, String description, int status) {
		super();
		this.name = name;
		this.description = description;
		this.status = status;
	}
	
	public JobDTO(AsynchronousJob job) {
		this.setName(job.getName());
		this.setDescription(job.getDescription());
		this.setStatus(job.getStatus());
	}
}
