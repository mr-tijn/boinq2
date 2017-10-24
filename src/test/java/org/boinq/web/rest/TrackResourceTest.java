package org.boinq.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.Track;
import org.boinq.repository.TrackRepository;
import org.boinq.web.rest.TrackResource;
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
 * Test class for the TrackResource REST controller.
 *
 * @see TrackResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class TrackResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);

	private static final String DEFAULT_NAME = "A track";
	
	private static final String UPD_NAME = "A modified track";

    @Inject
    private TrackRepository trackRepository;

    private MockMvc restTrackMockMvc;
    
    private Track track;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrackResource trackResource = new TrackResource();
        ReflectionTestUtils.setField(trackResource, "trackRepository", trackRepository);

        this.restTrackMockMvc = MockMvcBuilders.standaloneSetup(trackResource).build();

        track = new Track();
        track.setId(DEFAULT_ID);
        track.setName(DEFAULT_NAME);
    }

    @Test
    public void testCRUDTrack() throws Exception {

    	// Create Track
    	restTrackMockMvc.perform(post("/app/rest/tracks")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(track)))
                .andExpect(status().isOk());

    	// Read Track
    	restTrackMockMvc.perform(get("/app/rest/tracks/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.name").value(DEFAULT_NAME));

    	// Update Track
    	track.setName(UPD_NAME);
    	
    	restTrackMockMvc.perform(post("/app/rest/tracks")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(track)))
                .andExpect(status().isOk());

    	// Read updated Track
    	restTrackMockMvc.perform(get("/app/rest/tracks/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.name").value(UPD_NAME));

    	// Delete Track
    	restTrackMockMvc.perform(delete("/app/rest/tracks/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    	// Read nonexisting Track
    	restTrackMockMvc.perform(get("/app/rest/tracks/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
