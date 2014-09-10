package com.genohm.boinq.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.genohm.boinq.Application;
import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.repository.DatasourceRepository;


/**
 * Test class for the DatasourceResource REST controller.
 *
 * @see DatasourceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class DatasourceResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);

    private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

    private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

    private static final String DEFAULT_ENDPOINT_URL = "sampleEndpointUrl";

    private static final String UPD_ENDPOINT_URL = "sampleEndpointUrlUpdated";

    @Inject
    private DatasourceRepository datasourceRepository;

    private MockMvc restDatasourceMockMvc;
    
    private Datasource datasource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatasourceResource datasourceResource = new DatasourceResource();
        ReflectionTestUtils.setField(datasourceResource, "datasourceRepository", datasourceRepository);

        this.restDatasourceMockMvc = MockMvcBuilders.standaloneSetup(datasourceResource).build();

        datasource = new Datasource();
        datasource.setId(DEFAULT_ID);
    	datasource.setSampleDateAttribute(DEFAULT_SAMPLE_DATE_ATTR);
    	datasource.setEndpointUrl(DEFAULT_ENDPOINT_URL);
    }

    @Test
    public void testCRUDDatasource() throws Exception {

    	// Create Datasource
    	restDatasourceMockMvc.perform(post("/app/rest/datasources")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(datasource)))
                .andExpect(status().isOk());

    	// Read Datasource
    	restDatasourceMockMvc.perform(get("/app/rest/datasources/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
    			.andExpect(jsonPath("$.endpointUrl").value(DEFAULT_ENDPOINT_URL));

    	// Update Datasource
    	datasource.setSampleDateAttribute(UPD_SAMPLE_DATE_ATTR);
    	datasource.setEndpointUrl(UPD_ENDPOINT_URL);
  
    	restDatasourceMockMvc.perform(post("/app/rest/datasources")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(datasource)))
                .andExpect(status().isOk());

    	// Read updated Datasource
    	restDatasourceMockMvc.perform(get("/app/rest/datasources/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.sampleDateAttribute").value(UPD_SAMPLE_DATE_ATTR.toString()))
    			.andExpect(jsonPath("$.endpointUrl").value(UPD_ENDPOINT_URL));

    	// Delete Datasource
    	restDatasourceMockMvc.perform(delete("/app/rest/datasources/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    	// Read nonexisting Datasource
    	restDatasourceMockMvc.perform(get("/app/rest/datasources/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
