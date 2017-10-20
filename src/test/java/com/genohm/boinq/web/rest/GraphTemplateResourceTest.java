package com.genohm.boinq.web.rest;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.StringReader;
import java.io.StringWriter;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.genohm.boinq.Application;
import com.genohm.boinq.config.Constants;
import com.genohm.boinq.repository.GraphTemplateRepository;
import com.genohm.boinq.service.GraphTemplateRepositoryTestData;
import com.genohm.boinq.web.rest.dto.GraphTemplateDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:7777")
@ActiveProfiles(value = {Constants.SPRING_PROFILE_TEST})
public class GraphTemplateResourceTest {
	
    @Inject
    private GraphTemplateRepository graphTemplateRepository;

    @Inject
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    
    private MockMvc restGraphTemplateMockMvc;

    @Before
    public void setup() {
    	graphTemplateRepository.save(GraphTemplateRepositoryTestData.disGenet);
    	GraphTemplateResource gtResource = new GraphTemplateResource();
    	ReflectionTestUtils.setField(gtResource, "graphTemplateRepository", graphTemplateRepository);
    	this.restGraphTemplateMockMvc = MockMvcBuilders.standaloneSetup(gtResource).build();
    }

    @Test
    public void testSerializationAndDeserialization() throws Exception {
    	assertTrue(mappingJackson2HttpMessageConverter.canWrite(GraphTemplateDTO.class, MediaType.APPLICATION_JSON));
    	GraphTemplateDTO original = GraphTemplateDTO.create(GraphTemplateRepositoryTestData.disGenet);
    	StringWriter writer = new StringWriter();
    	mappingJackson2HttpMessageConverter.getObjectMapper().writeValue(writer, original);
    	String serialized = writer.toString();
    	StringReader reader = new StringReader(serialized);
    	GraphTemplateDTO result = mappingJackson2HttpMessageConverter.getObjectMapper().readValue(reader, GraphTemplateDTO.class);
    	assertTrue(result.equals(original));
    }
    
    @Test
    public void testGetExistingGraphTemplate() throws Exception {
       restGraphTemplateMockMvc
       .perform(
    		   get("/app/rest/graphtemplate/99")
    		   .accept(MediaType.APPLICATION_JSON))
       .andDo(print())
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.id").value(99))
       .andExpect(jsonPath("$.name").value(GraphTemplateRepositoryTestData.DIS_GE_NET));
    }

 
}
