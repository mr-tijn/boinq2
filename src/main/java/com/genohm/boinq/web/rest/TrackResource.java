package com.genohm.boinq.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.GraphDescriptor;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.Track;
import com.genohm.boinq.domain.jobs.TripleConversion;
import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.repository.TrackRepository;
import com.genohm.boinq.security.AuthoritiesConstants;
import com.genohm.boinq.service.AsynchronousJobService;
import com.genohm.boinq.service.FileManagerService;
import com.genohm.boinq.service.LocalGraphService;
import com.genohm.boinq.web.rest.dto.DatasourceDTO;
import com.genohm.boinq.web.rest.dto.TrackDTO;

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

/**
 * REST controller for managing Track.
 */
@RestController
@RequestMapping("/app")
public class TrackResource {

    private final Logger log = LoggerFactory.getLogger(TrackResource.class);

    @Inject
    private TrackRepository trackRepository;
    @Inject
    private DatasourceRepository datasourceRepository;
    @Inject
    private AsynchronousJobService jobService;
    @Inject
    private FileManagerService fileService;
    @Inject
    private LocalGraphService localGraphService;

    /**
     * POST  /rest/tracks -> Create a new track.
     */
    @RequestMapping(value = "/rest/datasources/{ds_id}/tracks",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(Principal principal, @PathVariable Long ds_id, @RequestBody TrackDTO trackDTO) {
        log.debug("REST request to save Track : {}", trackDTO);
        try {
        	Datasource datasource = datasourceRepository.findOne(ds_id);
        	if (datasource == null) {
        		throw new Exception("No datasource found");
        	}
        	verifyModifyPermission(principal.getName(), datasource);
        	Track track = trackRepository.findOne(trackDTO.getId());
        	if (track == null) {
        		track = new Track();
        		track.setRawDataFiles(new HashSet<RawDataFile>());
        	}
        	track.setDatasource(datasource);
        	track.setGraphName(trackDTO.getGraphName());
        	track.setName(trackDTO.getName());
        	track.setStatus(Track.STATUS_EMPTY);
        	Track savedTrack = trackRepository.save(track);
        	datasource.getTracks().add(track);
        	datasourceRepository.save(datasource);
        	datasourceRepository.flush();
        	trackRepository.flush();
        	if (datasource.getType() == Datasource.TYPE_LOCAL_FALDO) {
        		GraphDescriptor gd = localGraphService.createLocalGraph(savedTrack.getId().toString());
        		savedTrack.setGraphName(gd.graphURI);
        		trackRepository.save(savedTrack);
        	}
        } catch (Exception e) {
        	log.error("Could not save Track {}",trackDTO,e);
        }
    }
    
    @RequestMapping(value = "/rest/datasources/{ds_id}/tracks",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<List<TrackDTO>> getForDatasource(Principal principal, @PathVariable Long ds_id, HttpServletResponse response) {
        log.debug("REST request to get Tracks for datasource : {}", ds_id);
        List<TrackDTO> tracks = null;
    	try {
            Datasource datasource = datasourceRepository.findOne(ds_id);
            if (datasource == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            tracks = new LinkedList<TrackDTO>();
            for (Track track: datasource.getTracks()) {
            	tracks.add(new TrackDTO(track));
            }
    	} catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
        return new ResponseEntity<List<TrackDTO>>(tracks, HttpStatus.OK);
    }

    /**
     * GET  /rest/tracks/:id -> get the "id" track.
     */
    @RequestMapping(value = "/rest/tracks/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<TrackDTO> getDirect(Principal principal, @PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Track : {}", id);
        try {
        	Track track = trackRepository.findOne(id);
        	if (track == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        	}
         	return new ResponseEntity<TrackDTO>(new TrackDTO(track), HttpStatus.OK);
        } catch (Exception e) {
        	log.error("could not get track",e);
        	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @RequestMapping(value = "/rest/datasources/{ds_id}/tracks/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<TrackDTO> get(Principal principal, @PathVariable Long ds_id, @PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Track : {}", id);
        try {
        	Datasource datasource = datasourceRepository.findOne(ds_id);
        	if (datasource == null) {
        		throw new Exception("No datasource found");
        	}
        	for (Track track: datasource.getTracks()) {
        		if (track.getId() == id) {
        			return new ResponseEntity<TrackDTO>(new TrackDTO(track), HttpStatus.OK);
        		}
        	}
        } catch (Exception e) {
        	log.error("could not get track",e);
        	return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * DELETE  /rest/tracks/:id -> delete the "id" track.
     */
    @RequestMapping(value = "/rest/tracks/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public void deleteDirect(@PathVariable Long id) {
        log.debug("REST request to delete Track : {}", id);
        trackRepository.delete(id);
    }
    
    /**
     * DELETE  /rest/datasources/:ds_id/tracks/:id -> delete the "id" track.
     */
    @RequestMapping(value = "/rest/datasources/{ds_id}/tracks/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public void deleteTrack(Principal principal, @PathVariable Long ds_id, @PathVariable Long id) {
        log.debug("REST request to delete Track : {}", id);       
        try {
        	Datasource datasource = datasourceRepository.findOne(ds_id);
        	verifyModifyPermission(principal.getName(), datasource);
        	if (datasource == null) {
        		throw new Exception("No datasource found");
        	}
        	Track found = null;
        	for (Track track: datasource.getTracks()) {
        		if (track.getId() == id) {
        			found = track;
        			break;
        		}
        	}
        	if (found != null) {
        		datasource.getTracks().remove(found);
        		datasourceRepository.save(datasource);
        		trackRepository.deleteFiles(found);
        		trackRepository.delete(found);
        	}

        } catch (Exception e) {
        	log.error("could not delete track",e);
        }
    }

    @RequestMapping(value="/rest/datasources/{ds_id}/tracks/{id}/startconversion", method=RequestMethod.PUT)
    public ResponseEntity<String> startTripleConversion(Principal principal, @PathVariable Long ds_id, @PathVariable Long id, @RequestBody Long data_id) {
    	try {
        	Datasource datasource = datasourceRepository.findOne(ds_id);
        	verifyModifyPermission(principal.getName(), datasource);
        	Track found = null;
        	for (Track track: datasource.getTracks()) {
        		if (track.getId() == id) {
        			found = track;
        			break;
        		}
        	}
        	if (found != null) {
            	for (RawDataFile file : found.getRawDataFiles()) {
            		if (data_id == null || file.getId() == data_id) {
            			jobService.add(new TripleConversion(file));
            		}
           		}
        	}
    	} catch (Exception e) {
    		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<String>("conversion started",HttpStatus.OK);
    }
  
    @RequestMapping(value="/rest/tracks/{id}/startconversion", method=RequestMethod.PUT)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> startTripleConversionDirect(@PathVariable Long id, @RequestBody Long data_id) {
    	try {
        	Track track = trackRepository.findOne(id);
        	for (RawDataFile file : track.getRawDataFiles()) {
        		if (data_id == null || file.getId() == data_id) {
        			jobService.add(new TripleConversion(file));
        		}
       		}
    	} catch (Exception e) {
    		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<String>(HttpStatus.OK);
    }
  
    
    @RequestMapping(value="/rest/datasources/{ds_id}/tracks/{id}/rawdatafiles/{data_id}", method=RequestMethod.DELETE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> deleteDirect(Principal principal, @PathVariable Long ds_id, @PathVariable Long id, @PathVariable Long data_id) {
    	try {
        	Datasource datasource = datasourceRepository.findOne(ds_id);
        	verifyModifyPermission(principal.getName(), datasource);
        	Track foundTrack = null;
        	for (Track track: datasource.getTracks()) {
        		if (track.getId() == id) {
        			foundTrack = track;
        			break;
        		}
        	}
        	if (foundTrack != null) {
        		RawDataFile toRemove = null;
        		for (RawDataFile data: foundTrack.getRawDataFiles()) {
        			if (data_id == data.getId()) {
        				toRemove = data;
        				break;
        			}
        		}
        		if (toRemove != null) {
        			fileService.remove(toRemove);
        			foundTrack.getRawDataFiles().remove(toRemove);
        			trackRepository.save(foundTrack);
        		}
        	}
    	} catch (Exception e) {
    		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<String>(HttpStatus.OK);
    }

    
    @RequestMapping(value="/rest/tracks/{id}/rawdatafiles/{data_id}", method=RequestMethod.DELETE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public ResponseEntity<String> deleteDirect(Principal principal, @PathVariable Long id, @PathVariable Long data_id) {
    	try {
    		Track track = trackRepository.findOne(id);
    		RawDataFile toRemove = null;
    		for (RawDataFile data: track.getRawDataFiles()) {
    			if (data_id == data.getId()) {
    				toRemove = data;
    				break;
    			}
    		}
    		if (toRemove != null) {
    			fileService.remove(toRemove);
    			track.getRawDataFiles().remove(toRemove);
    			trackRepository.save(track);
    		}
    	} catch (Exception e) {
    		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	return new ResponseEntity<String>(HttpStatus.OK);
    }

    private void verifyModifyPermission(String login, Datasource datasource) throws Exception {
    	if (!datasource.getIsPublic() && !datasource.getOwner().getLogin().equals(login)) {
    		throw new Exception("You do not have permission to modify this track");
    	}

    }
}
