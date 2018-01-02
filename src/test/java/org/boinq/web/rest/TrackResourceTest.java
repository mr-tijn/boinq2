package org.boinq.web.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.Datasource;
import org.boinq.domain.Track;
import org.boinq.domain.User;
import org.boinq.domain.jobs.AsynchronousJob;
import org.boinq.domain.jobs.TripleConversion;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.repository.DatasourceRepository;
import org.boinq.repository.GraphTemplateRepository;
import org.boinq.repository.TrackRepository;
import org.boinq.service.AsynchronousJobService;
import org.boinq.service.LocalGraphService;
import org.boinq.web.rest.TrackResource;
import org.boinq.web.rest.dto.EdgeTemplateDTO;
import org.boinq.web.rest.dto.GraphTemplateDTO;
import org.boinq.web.rest.dto.NodeTemplateDTO;
import org.boinq.web.rest.dto.TrackDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
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
    
    private static final Long DEFAULT_ID = new Long(999L);

	private static final String DEFAULT_NAME = "A track";
	
	private static final String UPD_NAME = "A modified track";

    @Mock
    private TrackRepository mockTrackRepository;
    @Mock
    private DatasourceRepository mockDatasourceRepository;
    @Mock
    private GraphTemplateRepository mockGraphTemplateRepository;
    @Mock
    private LocalGraphService mockLocalGraphService;
    @Mock
    private AsynchronousJobService mockJobService;

    private MockMvc restTrackMockMvc;
    private Datasource datasource;
    private Track track;
    

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrackResource trackResource = new TrackResource();
        ReflectionTestUtils.setField(trackResource, "trackRepository", mockTrackRepository);
        ReflectionTestUtils.setField(trackResource, "datasourceRepository", mockDatasourceRepository);
        ReflectionTestUtils.setField(trackResource, "graphTemplateRepository", mockGraphTemplateRepository);
        ReflectionTestUtils.setField(trackResource, "localGraphService", mockLocalGraphService);
        ReflectionTestUtils.setField(trackResource, "jobService", mockJobService);
        
        track = new Track();
        track.setId(DEFAULT_ID);
        track.setName(DEFAULT_NAME);
    	track.setRawDataFiles(new HashSet<>());

    	datasource = new Datasource();
    	User admin = new User();
    	admin.setLogin("admin");
    	datasource.setId(0L);
    	datasource.setOwner(admin);
    	datasource.setIsPublic(false);
    	datasource.setTracks(new HashSet<>());
    	datasource.getTracks().add(track);
    	
        when(mockDatasourceRepository.findOneById(1L)).thenReturn(Optional.of(datasource));
		when(mockLocalGraphService.createLocalGraph(any(String.class))).thenReturn("http://graphName");
        when(mockGraphTemplateRepository.findOneById(1L)).thenReturn(Optional.of(new GraphTemplate()));
        when(mockTrackRepository.findOneWithMeta(1L)).thenReturn(Optional.of(track));
        when(mockTrackRepository.save(any(Track.class))).thenReturn(track);

        this.restTrackMockMvc = MockMvcBuilders.standaloneSetup(trackResource).build();
    }

    @Test
    public void testCRUDTrack() throws Exception {

    	Principal mockAdmin = mock(Principal.class);
    	when(mockAdmin.getName()).thenReturn("admin");
    	
    	TrackDTO trackdto = new TrackDTO(track);
    	trackdto.setGraphTemplateId(1L);
    	
        when(mockTrackRepository.findOneWithMeta(DEFAULT_ID)).thenReturn(Optional.empty());
    	// Create Track
    	restTrackMockMvc.perform(post("/app/rest/datasources/1/tracks")
    			.principal(mockAdmin)
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(trackdto)))
                .andExpect(status().isOk());
    	verify(mockTrackRepository, atLeastOnce()).save(any(Track.class));
    	// from now on assume saved with DEFAULT_ID 
        when(mockTrackRepository.findOneWithMeta(DEFAULT_ID)).thenReturn(Optional.of(track));
    	// Read Track
    	restTrackMockMvc.perform(get("/app/rest/datasources/1/tracks/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.name").value(DEFAULT_NAME));

    	// Update Track
    	track.setName(UPD_NAME);
    	restTrackMockMvc.perform(post("/app/rest/datasources/1/tracks")
    			.principal(mockAdmin)
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(track)))
                .andExpect(status().isOk());
    	ArgumentCaptor<Track> captor = ArgumentCaptor.forClass(Track.class);
    	verify(mockTrackRepository, atLeastOnce()).save(captor.capture());
    	assertEquals(captor.getValue().getName(), UPD_NAME);
    	
    	// Read updated Track - stupid test without a real backend
    	restTrackMockMvc.perform(get("/app/rest/datasources/1/tracks/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.name").value(UPD_NAME));

    	// Delete Track
    	restTrackMockMvc.perform(delete("/app/rest/datasources/1/tracks/{id}", DEFAULT_ID)
    			.principal(mockAdmin)
    			.accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    	// did the repo get a delete statement ?
    	verify(mockTrackRepository, atLeastOnce()).delete(any(Track.class));
    	assertTrue(datasource.getTracks().size() == 0);
    	
        when(mockTrackRepository.findOneWithMeta(DEFAULT_ID)).thenReturn(Optional.empty());
    	// Read nonexisting Track
    	restTrackMockMvc.perform(get("/app/rest/datasources/1/tracks/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    	datasource.getTracks().add(track);
        when(mockTrackRepository.findOneWithMeta(DEFAULT_ID)).thenReturn(Optional.of(track));
        restTrackMockMvc.perform(get("/app/rest/datasources/1/tracks/{id}/empty", DEFAULT_ID)
        		.principal(mockAdmin))
        		.andExpect(status().isOk());
        verify(mockTrackRepository, atLeastOnce()).empty(any(Track.class));
        
        restTrackMockMvc.perform(put("/app/rest/datasources/{ds_id}/tracks/{track_id}/startconversion", 1L, DEFAULT_ID)
        		.param("mainType", "http://maintype")
        		.param("subType", "http://subtype")
        		.principal(mockAdmin)
        		.accept(TestUtil.APPLICATION_JSON_UTF8))
        		.andExpect(status().isOk());
        verify(mockJobService, atLeastOnce()).add(any(AsynchronousJob.class));
        
        //TODO: add rawdatafile deletion and addition tests
        
        
    }
    
    
    
}
