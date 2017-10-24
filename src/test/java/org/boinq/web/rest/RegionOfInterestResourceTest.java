package org.boinq.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.RegionOfInterest;
import org.boinq.repository.RegionOfInterestRepository;
import org.boinq.web.rest.RegionOfInterestResource;
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


/**
 * Test class for the RegionOfInterestResource REST controller.
 *
 * @see RegionOfInterestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class RegionOfInterestResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);

    private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

    private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

    private static final String DEFAULT_SAMPLE_TEXT_ATTR = "sampleTextAttribute";

    private static final String UPD_SAMPLE_TEXT_ATTR = "sampleTextAttributeUpt";

    @Inject
    private RegionOfInterestRepository regionofinterestRepository;

    private MockMvc restRegionOfInterestMockMvc;
    
    private RegionOfInterest regionofinterest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegionOfInterestResource regionofinterestResource = new RegionOfInterestResource();
        ReflectionTestUtils.setField(regionofinterestResource, "regionofinterestRepository", regionofinterestRepository);

        this.restRegionOfInterestMockMvc = MockMvcBuilders.standaloneSetup(regionofinterestResource).build();

        regionofinterest = new RegionOfInterest();
        regionofinterest.setId(DEFAULT_ID);
    	regionofinterest.setSampleDateAttribute(DEFAULT_SAMPLE_DATE_ATTR);
    	regionofinterest.setSampleTextAttribute(DEFAULT_SAMPLE_TEXT_ATTR);
    }

    @Test
    public void testCRUDRegionOfInterest() throws Exception {

    	// Create RegionOfInterest
    	restRegionOfInterestMockMvc.perform(post("/app/rest/regionofinterests")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(regionofinterest)))
                .andExpect(status().isOk());

    	// Read RegionOfInterest
    	restRegionOfInterestMockMvc.perform(get("/app/rest/regionofinterests/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
    			.andExpect(jsonPath("$.sampleTextAttribute").value(DEFAULT_SAMPLE_TEXT_ATTR));

    	// Update RegionOfInterest
    	regionofinterest.setSampleDateAttribute(UPD_SAMPLE_DATE_ATTR);
    	regionofinterest.setSampleTextAttribute(UPD_SAMPLE_TEXT_ATTR);
  
    	restRegionOfInterestMockMvc.perform(post("/app/rest/regionofinterests")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(regionofinterest)))
                .andExpect(status().isOk());

    	// Read updated RegionOfInterest
    	restRegionOfInterestMockMvc.perform(get("/app/rest/regionofinterests/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.sampleDateAttribute").value(UPD_SAMPLE_DATE_ATTR.toString()))
    			.andExpect(jsonPath("$.sampleTextAttribute").value(UPD_SAMPLE_TEXT_ATTR));

    	// Delete RegionOfInterest
    	restRegionOfInterestMockMvc.perform(delete("/app/rest/regionofinterests/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    	// Read nonexisting RegionOfInterest
    	restRegionOfInterestMockMvc.perform(get("/app/rest/regionofinterests/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
