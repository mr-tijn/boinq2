package com.genohm.boinq.web.rest;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.User;
import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.repository.UserRepository;
import com.genohm.boinq.security.AuthoritiesConstants;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.web.rest.dto.DatasourceDTO;

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
    private LocalGraphService localGraphService;

    /**
     * POST  /rest/datasources -> Create a new datasource.
     */
    @RequestMapping(value = "/rest/datasources",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public @ResponseBody ResponseEntity<DatasourceDTO> create(Principal principal, @RequestBody DatasourceDTO datasourceDTO) {
        log.debug("REST request to save Datasource : {}", datasourceDTO);
        Optional<Datasource> find = datasourceRepository.findOneWithMeta(datasourceDTO.getId());
        Datasource datasource;
        if (find.isPresent()) {
        	datasource = find.get();
        } else {
        	datasource = new Datasource();
            datasource.setTracks(new HashSet<Track>());
            Optional<User> currentUser = userRepository.findOneByLogin(principal.getName());
            datasource.setOwner(currentUser.get());
        }
        datasource.setEndpointUrl(datasourceDTO.getEndpointUrl());
        datasource.setIsPublic(datasourceDTO.getIsPublic()!=null?datasourceDTO.getIsPublic():false);
        datasource.setName(datasourceDTO.getName());
        datasource.setType(datasourceDTO.getType());
        datasource.setIri(datasourceDTO.getIri());
        if (Datasource.TYPE_LOCAL_FALDO == datasource.getType()) {
        	datasource.setEndpointUrl(localGraphService.getSparqlEndpoint());
        	datasource.setEndpointUpdateUrl(localGraphService.getUpdateEndpoint());
        	datasource.setMetaEndpointUrl(localGraphService.getMetaEndpoint());
        	datasource.setMetaGraphName(localGraphService.getMetaGraph());
        }
    	return new ResponseEntity<DatasourceDTO> (new DatasourceDTO(datasourceRepository.save(datasource)),HttpStatus.OK);
    }

    /**
     * GET  /rest/datasources -> get all the datasources.
     */
    @RequestMapping(value = "/rest/datasources",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DatasourceDTO> getAll() {
        log.debug("REST request to get all Datasources");
        List<DatasourceDTO> allDTO = new LinkedList<DatasourceDTO>();
        for (Datasource ds: datasourceRepository.findAllWithMeta()) {
        	allDTO.add(new DatasourceDTO(ds));
        }
        return allDTO;
    }

    /**
     * GET  /rest/datasources/:id -> get the "id" datasource.
     */
    @RequestMapping(value = "/rest/datasources/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DatasourceDTO> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Datasource : {}", id);
        Optional<Datasource> result = datasourceRepository.findOneWithMeta(id);
        if (result.isPresent()) {
            DatasourceDTO datasourceDTO = new DatasourceDTO(result.get());
            return new ResponseEntity<>(datasourceDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    
    
    /**
     * DELETE  /rest/datasources/:id -> delete the "id" datasource.
     */
    @RequestMapping(value = "/rest/datasources/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Timed
    public void delete(Principal principal, @PathVariable Long id) {
        log.debug("REST request to delete Datasource : {}", id);
        Datasource ds = datasourceRepository.findOne(id);
        if (ds.getOwner() != null && ds.getOwner().getLogin().equals(principal.getName())) {
        	datasourceRepository.delete(id);
        	//TODO: delete metadata
        } else {
        	log.error("User "+principal.getName()+" attempted to delete datasource "+id);
        }
    }
    
    
}
