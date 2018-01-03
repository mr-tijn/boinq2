package org.boinq.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.Datasource;
import org.boinq.repository.DatasourceRepository;
import org.boinq.repository.UserRepository;
import org.boinq.service.LocalGraphService;
import org.boinq.web.rest.DatasourceResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    
    private static final Long DEFAULT_ID = new Long(99L); // initial value of the pk defined in liquibase

    private static final String DEFAULT_ENDPOINT_URL = "sampleEndpointUrl";

    private static final String UPD_ENDPOINT_URL = "sampleEndpointUrlUpdated";

    @Inject
    private DatasourceRepository datasourceRepository;
    @Inject
    private UserRepository userRepository;
    @Mock
    private LocalGraphService mockLocalGraphService;
    
    private MockMvc restDatasourceMockMvc;
    
    private Datasource datasource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatasourceResource datasourceResource = new DatasourceResource();
        ReflectionTestUtils.setField(datasourceResource, "datasourceRepository", datasourceRepository);
        ReflectionTestUtils.setField(datasourceResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(datasourceResource, "localGraphService", mockLocalGraphService);
        Mockito.when(mockLocalGraphService.getSparqlEndpoint()).thenReturn(DEFAULT_ENDPOINT_URL);
        Mockito.when(mockLocalGraphService.getUpdateEndpoint()).thenReturn("updateEndpoint");
        Mockito.when(mockLocalGraphService.getMetaEndpoint()).thenReturn("metaEndpoint");
        Mockito.when(mockLocalGraphService.getMetaUpdateEndpoint()).thenReturn("metaUpdateEndpoint");
        Mockito.when(mockLocalGraphService.getMetaGraph()).thenReturn("metaGraph");

        this.restDatasourceMockMvc = MockMvcBuilders.standaloneSetup(datasourceResource).build();
        datasource = new Datasource();
        datasource.setId(DEFAULT_ID);
    	datasource.setEndpointUrl(DEFAULT_ENDPOINT_URL);
    	
    }

    @Test
    public void testCRUDDatasource() throws Exception {

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("admin");
    	
    	// Create Datasource
    	restDatasourceMockMvc.perform(post("/app/rest/datasources")
    			.principal(mockPrincipal)
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(datasource)))
                .andExpect(status().isOk());

    	// Read Datasource
    	restDatasourceMockMvc.perform(get("/app/rest/datasources/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.endpointUrl").value(DEFAULT_ENDPOINT_URL));

    	// Update Datasource
    	datasource.setType(Datasource.TYPE_LOCAL_SPARQL); //need to change type or endpoint gets reset to default
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
    			.andExpect(jsonPath("$.endpointUrl").value(UPD_ENDPOINT_URL));

    	// Delete Datasource
    	restDatasourceMockMvc.perform(delete("/app/rest/datasources/{id}", DEFAULT_ID)
    			.principal(mockPrincipal)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    	// Read nonexisting Datasource
    	restDatasourceMockMvc.perform(get("/app/rest/datasources/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
