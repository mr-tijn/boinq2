package org.boinq.web.rest;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.boinq.domain.Project;
import org.boinq.domain.Track;
import org.boinq.domain.User;
import org.boinq.repository.ProjectRepository;
import org.boinq.repository.TrackRepository;
import org.boinq.repository.UserRepository;
import org.boinq.web.rest.dto.ProjectDTO;
import org.boinq.web.rest.dto.TrackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/app")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    @Inject
    private ProjectRepository projectRepository;
    @Inject
    private TrackRepository trackRepository;
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /rest/projects -> Create a new project.
     */
    @RequestMapping(value = "/rest/projects",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(Principal principal, @RequestBody ProjectDTO projectDTO) {
        log.debug("REST request to save Project : {}", projectDTO);
        Project project = null;
        if (projectDTO.getId() != null) {
        	project = projectRepository.findOne(projectDTO.getId());
        } 
        if (project == null) {
        	project = new Project();
        }
        User owner = null;
        if (projectDTO.getOwnerLogin() != null) {
        	owner = userRepository.findOneByLogin(projectDTO.getOwnerLogin()).get();
        }
        if (owner == null) {
        	owner = userRepository.findOneByLogin(principal.getName()).get();
        }
        Set<Track> tracks = project.getTracks();
        if (tracks == null) {
        	tracks = new HashSet<Track>();
        	project.setTracks(tracks);
        }
    	tracks.clear();
    	for (TrackDTO trackDTO: projectDTO.getTracks()) {
    		Track track = trackRepository.findOne(trackDTO.getId());
    		if (track != null) {
    			tracks.add(track);
    		}
    	}
        project.setOwner(owner);
        project.setTitle(projectDTO.getTitle());
        project = projectRepository.save(project);
    }

    /**
     * GET  /rest/projects -> get all the projects.
     */
    @RequestMapping(value = "/rest/projects",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<ProjectDTO> getAll() {
        log.debug("REST request to get all Projects");
        List<ProjectDTO> projects = new LinkedList<ProjectDTO>();
        for (Project project : projectRepository.findAll()) {
        	projects.add(new ProjectDTO(project));
        }
        return projects;
    }

    /**
     * GET  /rest/projects/:id -> get the "id" project.
     */
    @RequestMapping(value = "/rest/projects/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<ProjectDTO> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        if (project == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ProjectDTO(project), HttpStatus.OK);
    }

    /**
     * DELETE  /rest/projects/:id -> delete the "id" project.
     */
    @RequestMapping(value = "/rest/projects/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectRepository.delete(id);
    }
}
