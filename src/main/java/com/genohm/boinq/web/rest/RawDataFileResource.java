package com.genohm.boinq.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.repository.RawDataFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing RawDataFile.
 */
@RestController
@RequestMapping("/app")
public class RawDataFileResource {

    private final Logger log = LoggerFactory.getLogger(RawDataFileResource.class);

    @Inject
    private RawDataFileRepository rawdatafileRepository;

    /**
     * POST  /rest/rawdatafiles -> Create a new rawdatafile.
     */
    @RequestMapping(value = "/rest/rawdatafiles",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(@RequestBody RawDataFile rawdatafile) {
        log.debug("REST request to save RawDataFile : {}", rawdatafile);
        rawdatafileRepository.save(rawdatafile);
    }

    /**
     * GET  /rest/rawdatafiles -> get all the rawdatafiles.
     */
    @RequestMapping(value = "/rest/rawdatafiles",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<RawDataFile> getAll() {
        log.debug("REST request to get all RawDataFiles");
        return rawdatafileRepository.findAll();
    }

    /**
     * GET  /rest/rawdatafiles/:id -> get the "id" rawdatafile.
     */
    @RequestMapping(value = "/rest/rawdatafiles/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<RawDataFile> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get RawDataFile : {}", id);
        RawDataFile rawdatafile = rawdatafileRepository.findOne(id);
        if (rawdatafile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rawdatafile, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/rawdatafiles/:id -> delete the "id" rawdatafile.
     */
    @RequestMapping(value = "/rest/rawdatafiles/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete RawDataFile : {}", id);
        rawdatafileRepository.delete(id);
    }
}
