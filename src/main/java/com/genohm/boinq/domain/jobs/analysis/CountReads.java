package com.genohm.boinq.domain.jobs.analysis;

import com.genohm.boinq.domain.Track;

public class CountReads implements Analysis {

	private String name;
	private int status;
	private String description;
	
	private Track roiTrack;
	
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

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub

	}

}
