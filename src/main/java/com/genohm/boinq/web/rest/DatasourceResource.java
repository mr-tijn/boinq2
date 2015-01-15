package com.genohm.boinq.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.GraphDescriptor;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.User;
import com.genohm.boinq.domain.jobs.TripleConversion;
import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.repository.RawDataFileRepository;
import com.genohm.boinq.repository.TrackRepository;
import com.genohm.boinq.repository.UserRepository;
import com.genohm.boinq.security.AuthoritiesConstants;
import com.genohm.boinq.service.AsynchronousJobService;
import com.genohm.boinq.service.FileManagerService;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.web.rest.dto.DatasourceDTO;
import com.genohm.boinq.web.rest.dto.RawDataFileDTO;
import com.genohm.boinq.web.rest.dto.TrackDTO;
import com.mongodb.RawDBObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * REST controller for managing Datasource.
 */
@RestController
@RequestMapping("/app")
public class DatasourceResource {

  	private final Logger log = LoggerFactory.getLogger(DatasourceResource.class);

    @Inject
    private DatasourceRepository datasourceRepository;

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private TrackRepository	trackRepository;
            
    @Inject
    private LocalGraphService localGraphService;

    /**
     * POST  /rest/datasources -> Create a new datasource.
     */
    @RequestMapping(value = "/rest/datasources",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(Principal principal, @RequestBody DatasourceDTO datasourceDTO) {
        log.debug("REST request to save Datasource : {}", datasourceDTO);
        Datasource datasource = datasourceRepository.findOne(datasourceDTO.getId());
        if (datasource == null) {
        	datasource = new Datasource();
        	datasource.setEndpointUrl(datasourceDTO.getEndpointUrl());
        	datasource.setIsPublic(datasourceDTO.getIsPublic()!=null?datasourceDTO.getIsPublic():false);
        	datasource.setName(datasourceDTO.getName());
        	datasource.setType(datasourceDTO.getType());
            datasource.setTracks(new HashSet<Track>());
            User currentUser = userRepository.findOne(principal.getName());
            datasource.setOwner(currentUser);
        } else {
        	datasource.setEndpointUrl(datasourceDTO.getEndpointUrl());
        	datasource.setIsPublic(datasourceDTO.getIsPublic()!=null?datasourceDTO.getIsPublic():false);
        	datasource.setName(datasourceDTO.getName());
        	datasource.setType(datasourceDTO.getType());
        }
        if (Datasource.TYPE_LOCAL_FALDO == datasource.getType()) {
        	// there is one single meta graph for all local faldo datasources
        	// there is one read and one update endpoint
        	// each track will create its own graph and put info in the meta graph
        	datasource.setEndpointUrl(localGraphService.getSparqlEndpoint());
        	datasource.setEndpointUpdateUrl(localGraphService.getUpdateEndpoint());
        	datasource.setMetaEndpointUrl(localGraphService.getMetaEndpoint());
        }
    	datasourceRepository.save(datasource);
    }

    /**
     * GET  /rest/datasources -> get all the datasources.
     */
    @RequestMapping(value = "/rest/datasources",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<DatasourceDTO> getAll() {
        log.debug("REST request to get all Datasources");
        List<DatasourceDTO> allDTO = new LinkedList<DatasourceDTO>();
        for (Datasource ds: datasourceRepository.findAll()) {
        	allDTO.add(new DatasourceDTO(ds));
        }
        return allDTO;
    }

    /**
     * GET  /rest/datasources/:id -> get the "id" datasource.
     */
    @RequestMapping(value = "/rest/datasources/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<DatasourceDTO> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Datasource : {}", id);
        Datasource datasource = datasourceRepository.findOne(id);
        if (datasource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DatasourceDTO datasourceDTO = new DatasourceDTO(datasource);
        return new ResponseEntity<>(datasourceDTO, HttpStatus.OK);
    }

    
    
    /**
     * DELETE  /rest/datasources/:id -> delete the "id" datasource.
     */
    @RequestMapping(value = "/rest/datasources/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public void delete(Principal principal, @PathVariable Long id) {
        log.debug("REST request to delete Datasource : {}", id);
        Datasource ds = datasourceRepository.findOne(id);
        if (ds.getOwner() != null && ds.getOwner().getLogin().equals(principal.getName())) {
        	datasourceRepository.delete(id);
        } else {
        	log.error("User "+principal.getName()+" attempted to delete datasource "+id);
        }
    }
    
    
}
