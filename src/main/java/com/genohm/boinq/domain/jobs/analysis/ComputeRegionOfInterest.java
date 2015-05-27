package com.genohm.boinq.domain.jobs.analysis;

import javax.inject.Inject;

import com.genohm.boinq.domain.jobs.AsynchronousJob;
import com.genohm.boinq.domain.match.Match;
import com.genohm.boinq.domain.match.MatchFactory;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.tools.generators.ARQGenerator;
import com.genohm.boinq.tools.generators.SPARQLGenerator;
import com.genohm.boinq.web.rest.dto.MatchDTO;

public class ComputeRegionOfInterest implements AsynchronousJob {

	@Inject
	private LocalGraphService localGraphService;
	
	private String name;
	private String description;
	private MatchDTO matchDTO;
	
	private int status = JOB_STATUS_UNKNOWN;
	private Boolean irq = false;
	
	public ComputeRegionOfInterest(MatchDTO matchDTO) {
		this.matchDTO = matchDTO;
	}
	
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
		// all loops should include check for irq
		this.status = JOB_STATUS_COMPUTING;
		try {
			Match match = MatchFactory.fromDTO(this.matchDTO);
			SPARQLGenerator generator = new ARQGenerator();
			if (killed()) return;
			match.acceptGenerator(generator, "brol");
		} catch (Exception e) {
			this.status = JOB_STATUS_ERROR;
		}
	}

	@Override
	public void kill() {
		this.irq = false;
	}

	private Boolean killed() {
		return this.irq;
	}

}
