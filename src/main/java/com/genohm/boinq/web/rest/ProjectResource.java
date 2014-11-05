package com.genohm.boinq.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.Project;
import com.genohm.boinq.domain.User;
import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.repository.ProjectRepository;
import com.genohm.boinq.repository.UserRepository;
import com.genohm.boinq.web.rest.dto.DatasourceDTO;
import com.genohm.boinq.web.rest.dto.ProjectDTO;
import com.mchange.v2.c3p0.DataSources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    private DatasourceRepository datasourceRepository;
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
        	owner = userRepository.findOne(projectDTO.getOwnerLogin());
        }
        if (owner == null) {
        	owner = userRepository.findOne(principal.getName());
        }
        Set<Datasource> datasources = project.getDatasources();
        if (datasources == null) {
        	datasources = new HashSet<Datasource>();
        	project.setDatasources(datasources);
        }
    	datasources.clear();
        for (DatasourceDTO datasourceDTO : projectDTO.getDatasources()) {
        	Datasource datasource = datasourceRepository.findOne(datasourceDTO.getId());
        	if (datasource != null) {
        		datasources.add(datasource);
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
