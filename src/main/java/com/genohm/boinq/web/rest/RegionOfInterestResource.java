package com.genohm.boinq.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

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
import com.genohm.boinq.domain.RegionOfInterest;
import com.genohm.boinq.repository.RegionOfInterestRepository;

/**
 * REST controller for managing RegionOfInterest.
 */
@RestController
@RequestMapping("/app")
public class RegionOfInterestResource {

    private final Logger log = LoggerFactory.getLogger(RegionOfInterestResource.class);

    @Inject
    private RegionOfInterestRepository regionofinterestRepository;

    /**
     * POST  /rest/regionofinterests -> Create a new regionofinterest.
     */
    @RequestMapping(value = "/rest/regionofinterests",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(@RequestBody RegionOfInterest regionofinterest) {
        log.debug("REST request to save RegionOfInterest : {}", regionofinterest);
        regionofinterestRepository.save(regionofinterest);
    }

    /**
     * GET  /rest/regionofinterests -> get all the regionofinterests.
     */
    @RequestMapping(value = "/rest/regionofinterests",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<RegionOfInterest> getAll() {
        log.debug("REST request to get all RegionOfInterests");
        return regionofinterestRepository.findAll();
    }

    /**
     * GET  /rest/regionofinterests/:id -> get the "id" regionofinterest.
     */
    @RequestMapping(value = "/rest/regionofinterests/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<RegionOfInterest> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get RegionOfInterest : {}", id);
        RegionOfInterest regionofinterest = regionofinterestRepository.findOne(id);
        if (regionofinterest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(regionofinterest, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/regionofinterests/:id -> delete the "id" regionofinterest.
     */
    @RequestMapping(value = "/rest/regionofinterests/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete RegionOfInterest : {}", id);
        regionofinterestRepository.delete(id);
    }
}
