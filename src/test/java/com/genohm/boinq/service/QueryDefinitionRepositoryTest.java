package com.genohm.boinq.service;

import static org.junit.Assert.*;

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
import com.genohm.boinq.domain.query.QueryDefinition;
import com.genohm.boinq.repository.QueryDefinitionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class QueryDefinitionRepositoryTest {

	@Inject
	QueryDefinitionRepository queryDefinitionRepository;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void saveAndReadBack() {
		QueryDefinitionTestData testData = new QueryDefinitionTestData();
		QueryDefinition saved = queryDefinitionRepository.save(testData.qd);
		assertNotNull(saved.getId());
		Optional<QueryDefinition> retrieved = queryDefinitionRepository.findOneById(saved.getId());
		assertTrue(retrieved.isPresent());
		assertEquals(retrieved.get().getTargetGraph(), testData.qd.getTargetGraph());
		assertEquals(retrieved.get().getQueryBridges().size(), testData.qd.getQueryBridges().size());
	}

}
