package org.boinq.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.boinq.Application;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.domain.query.QueryDefinition;
import org.boinq.domain.query.QueryGraph;
import org.boinq.repository.GraphTemplateRepository;
import org.boinq.repository.QueryDefinitionRepository;
import org.boinq.repository.QueryGraphRepository;
import org.boinq.repository.UserRepository;
import org.boinq.web.rest.dto.QueryDefinitionDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class QueryDefinitionRepositoryTest {

	@Inject
	QueryDefinitionRepository queryDefinitionRepository;
	@Inject
	UserRepository userRepository;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Transactional
	public void saveAndReadBack() {
		QueryDefinitionTestData testData = new QueryDefinitionTestData();
		testData.locationOverlapQuery.setOwner(userRepository.findOneByLogin("admin").get());
		QueryDefinition saved = queryDefinitionRepository.deepsave(testData.locationOverlapQuery);
		assertNotNull(saved.getId());
		Optional<QueryDefinition> retrieved = queryDefinitionRepository.findOneById(saved.getId());
		assertTrue(retrieved.isPresent());
		assertEquals(retrieved.get().getTargetGraph(), testData.locationOverlapQuery.getTargetGraph());
		assertEquals(retrieved.get().getQueryBridges().size(), testData.locationOverlapQuery.getQueryBridges().size());
	}

}
