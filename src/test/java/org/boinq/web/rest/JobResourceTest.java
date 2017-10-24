package org.boinq.web.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.jobs.AsynchronousJob;
import org.boinq.security.AuthoritiesConstants;
import org.boinq.service.AsynchronousJobService;
import org.boinq.web.rest.JobResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@WebAppConfiguration
@ActiveProfiles("dev")
public class JobResourceTest {

	@Inject
	JobResource jobResource;
	
	@Inject
	AsynchronousJobService jobService;

	LongRunningJob testJob;
	MockMvc restJobResourceMockMvc;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);

		this.restJobResourceMockMvc = MockMvcBuilders.standaloneSetup(jobResource).build();

		testJob = new LongRunningJob();
	}

	@Test
	public void testListJobs() throws Exception {

	        
		// Read Jobs
		restJobResourceMockMvc.perform(get("/app/rest/jobs/"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.*",hasSize(0)));

		jobService.add(testJob);
		
		// Allow time for job to get running
		Thread.sleep(2000);
	
		MvcResult result = restJobResourceMockMvc.perform(get("/app/rest/jobs/"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name", equalTo("Jimmy")))
			.andReturn();
	
		System.out.print(result.getResponse().getContentAsString());
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN);
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(authority);
		
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("admin","****",authorities);
		
		SecurityContextHolder.getContext().setAuthentication(principal);
		
		restJobResourceMockMvc.perform(put("/app/rest/jobs/cancel")
			.content(testJob.getName())
			.principal(principal)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		// Allow time for job to get killed
		Thread.sleep(2000);

		restJobResourceMockMvc.perform(get("/app/rest/jobs/"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.*", hasSize(0)));
		
		
	}
	

}

class LongRunningJob implements AsynchronousJob {
	
	private Boolean kill = false;

	@Override
	public String getName() {
		return "Jimmy";
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public int getStatus() {
		return JOB_STATUS_COMPUTING;
	}

	@Override
	public String getDescription() {
		return "Test Job";
	}

	@Override
	public String getErrorDescription() {
		return null;
	}

	@Override
	public void execute() {

		for (int i = 0; i < 100; i++) {
			if (kill) break;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}

	}

	@Override
	public void kill() {
		this.kill = true;
	}

	@Override
	public Long getDuration() {
		return null;
	}

}
