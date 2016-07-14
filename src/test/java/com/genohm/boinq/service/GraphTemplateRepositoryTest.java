package com.genohm.boinq.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.genohm.boinq.Application;
import com.genohm.boinq.domain.query.GraphTemplate;
import com.genohm.boinq.repository.GraphTemplateRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class GraphTemplateRepositoryTest {

	@Inject
	GraphTemplateRepository graphTemplateRepository;
	
	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void canSaveNode() {
		
	}
	
	@Test
	public void canSaveAndReadBack() {
		GraphTemplate saved = graphTemplateRepository.save(GraphTemplateRepositoryTestData.disGenet);
		assertNotNull(saved.getId());
		Optional<GraphTemplate> retrieved = graphTemplateRepository.findOneById(saved.getId());
		assertTrue(retrieved.isPresent());
	}
	

}
