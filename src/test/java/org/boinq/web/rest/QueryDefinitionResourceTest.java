package org.boinq.web.rest;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.StringReader;
import java.io.StringWriter;

import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.query.QueryDefinition;
import org.boinq.repository.QueryDefinitionRepository;
import org.boinq.service.QueryDefinitionTestData;
import org.boinq.web.rest.QueryDefinitionResource;
import org.boinq.web.rest.dto.QueryDefinitionDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@Profile("test")
public class QueryDefinitionResourceTest {

	@Inject
	private QueryDefinitionRepository queryDefinitionRepository;
	
	private QueryDefinition savedData;
	private QueryDefinitionTestData testData;
	private MockMvc queryDefinitionMockMvc;
	
	
    @Inject
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    
	
	@Before
	public void setUp() throws Exception {
		testData = new QueryDefinitionTestData();
		savedData = queryDefinitionRepository.save(testData.qd);
		QueryDefinitionResource qdResource = new QueryDefinitionResource();
		ReflectionTestUtils.setField(qdResource, "queryDefinitionRepository", queryDefinitionRepository);
		queryDefinitionMockMvc = MockMvcBuilders.standaloneSetup(qdResource).build();
	}

	@Test
	public void testSerialization() throws Exception {
		assertTrue(mappingJackson2HttpMessageConverter.canWrite(QueryDefinitionDTO.class, MediaType.APPLICATION_JSON));
		QueryDefinitionDTO original = QueryDefinitionDTO.create(testData.qd);
		StringWriter writer = new StringWriter();
		mappingJackson2HttpMessageConverter.getObjectMapper().writeValue(writer, original);
		String serialized = writer.toString();
    	StringReader reader = new StringReader(serialized);
    	QueryDefinitionDTO result = mappingJackson2HttpMessageConverter.getObjectMapper().readValue(reader, QueryDefinitionDTO.class);
    	assertTrue(result.equals(original));

		System.out.print(serialized);
	}
	
	@Test
	public void testGet() throws Exception {
		queryDefinitionMockMvc.perform(get("/app/rest/querydefinition/{id}",savedData.getId()))
		.andDo(print())
		.andExpect(status().isOk());
	}

}
