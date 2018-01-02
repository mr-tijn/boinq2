package org.boinq.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.RawDataFile;
import org.boinq.repository.RawDataFileRepository;
import org.boinq.web.rest.RawDataFileResource;
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
 * Test class for the RawDataFileResource REST controller.
 *
 * @see RawDataFileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class RawDataFileResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);

    private static final String DEFAULT_PATH = "dummy";

    private static final String UPD_PATH = "yummie";

    @Inject
    private RawDataFileRepository rawdatafileRepository;

    private MockMvc restRawDataFileMockMvc;
    
    private RawDataFile rawdatafile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RawDataFileResource rawdatafileResource = new RawDataFileResource();
        ReflectionTestUtils.setField(rawdatafileResource, "rawdatafileRepository", rawdatafileRepository);

        this.restRawDataFileMockMvc = MockMvcBuilders.standaloneSetup(rawdatafileResource).build();

        rawdatafile = new RawDataFile();
        rawdatafile.setId(DEFAULT_ID);
    	rawdatafile.setFilePath(DEFAULT_PATH);
    }

//    @Test
    public void testCRUDRawDataFile() throws Exception {

    	// Create RawDataFile
    	restRawDataFileMockMvc.perform(post("/app/rest/rawdatafiles")
    			.contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(rawdatafile)))
                .andExpect(status().isOk());

    	// Read RawDataFile
    	restRawDataFileMockMvc.perform(get("/app/rest/rawdatafiles/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.filePath").value(DEFAULT_PATH));

    	// Update RawDataFile
    	rawdatafile.setFilePath(UPD_PATH);
  
    	restRawDataFileMockMvc.perform(post("/app/rest/rawdatafiles")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rawdatafile)))
                .andExpect(status().isOk());

    	// Read updated RawDataFile
    	restRawDataFileMockMvc.perform(get("/app/rest/rawdatafiles/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.filePath").value(UPD_PATH));

    	// Delete RawDataFile
    	restRawDataFileMockMvc.perform(delete("/app/rest/rawdatafiles/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    	// Read nonexisting RawDataFile
    	restRawDataFileMockMvc.perform(get("/app/rest/rawdatafiles/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
