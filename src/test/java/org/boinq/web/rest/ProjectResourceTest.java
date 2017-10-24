package org.boinq.web.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;

import javax.inject.Inject;

import org.boinq.Application;
import org.boinq.domain.Datasource;
import org.boinq.domain.Project;
import org.boinq.domain.Track;
import org.boinq.domain.User;
import org.boinq.repository.DatasourceRepository;
import org.boinq.repository.ProjectRepository;
import org.boinq.repository.UserRepository;
import org.boinq.web.rest.ProjectResource;
import org.boinq.web.rest.dto.ProjectDTO;
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
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@ActiveProfiles("dev")
public class ProjectResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);

    private static final String DEFAULT_TITLE = "a title";

	private static final String UPD_TITLE = "a completely different title altogether";
    
    @Inject
    private ProjectRepository projectRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private DatasourceRepository datasourceRepository;

    @Inject
    private UserRepository userRepository;
    @Inject
    private DatasourceRepository datasourceRepository;
    
    private MockMvc restProjectMockMvc;
    
    private Project project;
    private User user;
    Datasource ds0;
    Datasource ds1;
    Datasource ds2;
    Track t1;
    Track t2;
    Track t3;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        user = userRepository.findOneByLogin("admin").get();

        ds0 = new Datasource();
        ds1 = new Datasource();
        ds2 = new Datasource();
        ds0.setOwner(user);
        ds0.setName("Dataset 0");
        ds1.setOwner(user);
        ds1.setName("Dataset 1");
        ds2.setOwner(user);
        ds2.setName("Dataset 2");
        
        
        ds0 = datasourceRepository.save(ds0);
        ds1 = datasourceRepository.save(ds1);
        ds2 = datasourceRepository.save(ds2);

        t1 = new Track();
        t2 = new Track();
        t3 = new Track();
        
        ds0.setTracks(new HashSet<Track>());
        t1.setDatasource(ds0);
        ds0.getTracks().add(t1);
        
        ds0.setTracks(new HashSet<Track>());
        t2.setDatasource(ds0);
        ds0.getTracks().add(t2);
        
        ds1.setTracks(new HashSet<Track>());
        t3.setDatasource(ds1);
        ds1.getTracks().add(t3);
        
        project = new Project();
        project.setId(DEFAULT_ID);
    	project.setTracks(new HashSet<Track>());
    	project.getTracks().add(t1);
    	project.getTracks().add(t2);
    	project.getTracks().add(t3);
    	
    	project.setOwner(user);
    	project.setTitle(DEFAULT_TITLE);
 
 
        ProjectResource projectResource = new ProjectResource();
        ReflectionTestUtils.setField(projectResource, "projectRepository", projectRepository);
        ReflectionTestUtils.setField(projectResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(projectResource, "datasourceRepository", datasourceRepository);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource).build();
        
   }

    @Test
    public void testCRUDProject() throws Exception {

    	// Create Project
    	restProjectMockMvc.perform(post("/app/rest/projects")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new ProjectDTO(project))))
                .andExpect(status().isOk());

    	// Read Project
    	restProjectMockMvc.perform(get("/app/rest/projects/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
    			.andExpect(jsonPath("$.tracks", hasSize(3)))
    			.andExpect(jsonPath("$.ownerLogin").value(user.getLogin()));
    	//TODO: count elements in datasources and check contents
    	
    	// Update Project
    	project.setTitle(UPD_TITLE);
    	project.getTracks().remove(ds1);
  
    	restProjectMockMvc.perform(post("/app/rest/projects")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(new ProjectDTO(project))))
                .andExpect(status().isOk());

    	// Read updated Project
    	restProjectMockMvc.perform(get("/app/rest/projects/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    			.andExpect(jsonPath("$.tracks", hasSize(2)));

    	// Delete Project
    	restProjectMockMvc.perform(delete("/app/rest/projects/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    	// Read nonexisting Project
    	restProjectMockMvc.perform(get("/app/rest/projects/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
