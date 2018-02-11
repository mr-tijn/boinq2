package org.boinq.web.rest;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.boinq.domain.Datasource;
import org.boinq.domain.RawDataFile;
import org.boinq.domain.Track;
import org.boinq.domain.jobs.TripleConversion;
import org.boinq.domain.query.GraphTemplate;
import org.boinq.repository.DatasourceRepository;
import org.boinq.repository.GraphTemplateRepository;
import org.boinq.repository.RawDataFileRepository;
import org.boinq.repository.TrackRepository;
import org.boinq.security.AuthoritiesConstants;
import org.boinq.service.AsynchronousJobService;
import org.boinq.service.FileManagerService;
import org.boinq.service.LocalGraphService;
import org.boinq.web.rest.dto.RawDataFileDTO;
import org.boinq.web.rest.dto.TrackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

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
	private GraphTemplateRepository graphTemplateRepository;
	@Inject
	private DatasourceRepository datasourceRepository;
	@Inject
	private AsynchronousJobService jobService;
	@Inject
	private FileManagerService fileService;
	@Inject
	private LocalGraphService localGraphService;
	@Inject
	private RawDataFileRepository rawDataFileRepository;

	/**
	 * POST /rest/tracks -> Create a new track.
	 */
	@RequestMapping(value = "/rest/datasources/{ds_id}/tracks", method = RequestMethod.POST, produces = "application/json")
	@Timed
	public ResponseEntity<TrackDTO>  create(Principal principal, @PathVariable Long ds_id, @RequestBody TrackDTO trackDTO) {
		log.debug("REST request to save Track : {}", trackDTO);
		try {
			Optional<Datasource> ds = datasourceRepository.findOneById(ds_id);
			if (!ds.isPresent()) {
				throw new Exception("No datasource found");
			}
			Datasource datasource = ds.get();
			verifyModifyPermission(principal.getName(), datasource);
			Optional<Track> result = trackRepository.findOneById(trackDTO.getId());
			Track track;
			if (result.isPresent()) {
				track = result.get();
			} else {
				track = new Track();
				track.setRawDataFiles(new HashSet<RawDataFile>());
			}
			track.setDatasource(datasource);
			track.setGraphName(trackDTO.getGraphName());
			track.setName(trackDTO.getName());
			track.setDescription(trackDTO.getDescription());
			track.setStatus(trackDTO.getStatus());
			track.setType(trackDTO.getType());
			track.setFileType(trackDTO.getFileType());
			track.setSpecies(trackDTO.getSpecies());
			track.setAssembly(trackDTO.getAssembly());
			track.setContigPrefix(trackDTO.getContigPrefix());
			track.setEntryCount(trackDTO.getEntryCount());
			track.setFeatureCount(trackDTO.getFeatureCount());
			track.setTripleCount(trackDTO.getTripleCount());
			if (trackDTO.getGraphTemplateId() == null) {
				GraphTemplate gt = new GraphTemplate();
				gt = graphTemplateRepository.save(gt);
				track.setGraphTemplate(gt);
			} else {
				Optional<GraphTemplate> gtOpt = graphTemplateRepository.findOneById(trackDTO.getGraphTemplateId());
				if (gtOpt.isPresent()) {
					track.setGraphTemplate(gtOpt.get());
				} else {
					GraphTemplate gt = new GraphTemplate();
					gt = graphTemplateRepository.save(gt);
					track.setGraphTemplate(gt);
				}
			}
			track = trackRepository.save(track);
			datasource.getTracks().add(track);
			datasourceRepository.save(datasource);
			//TODO: check if still needed when using @Transactional
			datasourceRepository.flush();
			trackRepository.flush();
			if (datasource.getType() == Datasource.TYPE_LOCAL_FALDO) {
				String graphName = localGraphService.createLocalGraph(datasource.getId()+"_"+track.getId().toString());
				track.setGraphName(graphName);
				trackRepository.save(track);
			}
			return new ResponseEntity<TrackDTO>(new TrackDTO(track), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Could not save Track {}", trackDTO, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/datasources/{ds_id}/tracks", method = RequestMethod.GET, produces = "application/json")
	@Timed
	public ResponseEntity<List<TrackDTO>> getForDatasource(Principal principal, @PathVariable Long ds_id,
			HttpServletResponse response) {
		log.debug("REST request to get Tracks for datasource : {}", ds_id);
		try {
			Optional<List<Track>> tracks = trackRepository.findByDatasourceId(ds_id);
			List<TrackDTO> dtos = null;
			if (tracks.isPresent()) {
				dtos = tracks.get().stream().map(track -> new TrackDTO(track)).collect(Collectors.toList());
			}
			return new ResponseEntity<List<TrackDTO>>(dtos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/tracks", method = RequestMethod.GET, produces = "application/json")
	@Timed
	public ResponseEntity<List<TrackDTO>> getAll(Principal principal, HttpServletResponse response) {
		log.debug("REST request to get all Tracks");
		List<TrackDTO> tracks = null;
		try {
			tracks = new LinkedList<TrackDTO>();
			for (Track track : trackRepository.findAll()) {
				if (track.getDatasource().getIsPublic()
						|| track.getDatasource().getOwner().getLogin().equals(principal.getName())) {
					tracks.add(new TrackDTO(track));
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<TrackDTO>>(tracks, HttpStatus.OK);
	}

	/**
	 * GET /rest/tracks/:id -> get the "id" track.
	 */
	@RequestMapping(value = "/rest/tracks/{id}", method = RequestMethod.GET, produces = "application/json")
	@Timed
	// @RolesAllowed(AuthoritiesConstants.ADMIN)
	public ResponseEntity<TrackDTO> getDirect(Principal principal, @PathVariable Long id,
			HttpServletResponse response) {
		log.debug("REST request to get Track : {}", id);
		try {
			Optional<Track> track = trackRepository.findOneById(id);
			if (track.isPresent()) {
				return new ResponseEntity<TrackDTO>(new TrackDTO(track.get()), HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error("could not get track", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/rest/datasources/{ds_id}/tracks/{id}", method = RequestMethod.GET, produces = "application/json")
	@Timed
	public ResponseEntity<TrackDTO> get(Principal principal, @PathVariable Long ds_id, @PathVariable Long id,
			HttpServletResponse response) {
		log.debug("REST request to get Track : {}", id);
		try {
			Optional<Datasource> result = datasourceRepository.findOneById(ds_id);
			if (!result.isPresent())
				throw new Exception("Could not find datasource " + ds_id);
			Datasource datasource = result.get();
			if (datasource == null) {
				throw new Exception("No datasource found");
			}
			for (Track track : datasource.getTracks()) {
				if (track.getId().equals(id)) {
					return new ResponseEntity<TrackDTO>(new TrackDTO(track), HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			log.error("could not get track", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * DELETE /rest/tracks/:id -> delete the "id" track.
	 */
	@RequestMapping(value = "/rest/tracks/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@Timed
	@RolesAllowed(AuthoritiesConstants.ADMIN)
	public void deleteDirect(@PathVariable Long id) {
		log.debug("REST request to delete Track : {}", id);
		trackRepository.delete(id);
	}

	/**
	 * DELETE /rest/datasources/:ds_id/tracks/:id -> delete the "id" track.
	 */
	@RequestMapping(value = "/rest/datasources/{ds_id}/tracks/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@Timed
	@RolesAllowed(AuthoritiesConstants.ADMIN)
	public void deleteTrack(Principal principal, @PathVariable Long ds_id, @PathVariable Long id) {
		log.debug("REST request to delete Track : {}", id);
		try {
			Optional<Datasource> result = datasourceRepository.findOneById(ds_id);
			if (!result.isPresent())
				throw new Exception("Could not find datasource " + ds_id);
			Datasource datasource = result.get();
			verifyModifyPermission(principal.getName(), datasource);
			if (datasource == null) {
				throw new Exception("No datasource found");
			}
			Optional<Track> trackOpt = datasource.getTracks().stream().filter(track -> (id.equals(track.getId()))).findFirst();
			if (trackOpt.isPresent()) {
				Track found = trackOpt.get();
				datasource.getTracks().remove(found);
				datasourceRepository.save(datasource);
				trackRepository.deleteFiles(found);
				trackRepository.empty(found);
				trackRepository.delete(found);
			}

		} catch (Exception e) {
			log.error("could not delete track", e);
		}
	}

	@RequestMapping(value = "/rest/datasources/{ds_id}/tracks/{track_id}/empty", method = RequestMethod.GET, produces = "application/json")
	@Timed
	@RolesAllowed(AuthoritiesConstants.ADMIN)
	public ResponseEntity<TrackDTO> emptyTrack(Principal principal, @PathVariable Long ds_id, @PathVariable Long track_id) {
		log.debug("REST request to empty Track : {}", track_id);
		try {
			Optional<Datasource> result = datasourceRepository.findOneById(ds_id);
			if (!result.isPresent())
				throw new Exception("Could not find datasource " + ds_id);
			Datasource datasource = result.get();
			verifyModifyPermission(principal.getName(), datasource);
			Optional<Track> found = datasource.getTracks().stream().filter(track -> track.getId().equals(track_id)).findFirst();
			if (!found.isPresent()) {
				throw new Exception("Could not find track " + track_id);
			}
			Track track = found.get();
			trackRepository.deleteFiles(track);
			trackRepository.empty(track);
			trackRepository.save(track);
			return new ResponseEntity<TrackDTO>(new TrackDTO(track), HttpStatus.OK);
		} catch (Exception e) {
			log.error("could not empty track", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value = "/rest/datasources/{ds_id}/tracks/{track_id}/startconversion", method = RequestMethod.PUT)
	public ResponseEntity<String> startTripleConversion(Principal principal, @PathVariable Long ds_id,
			@PathVariable Long track_id, @RequestParam(defaultValue="") String mainType, @RequestParam(defaultValue="") String subType, @RequestParam(defaultValue="") String attributeType) {
		log.debug("REST request to start triple conversion on Track " + track_id);
		try {
			Optional<Datasource> result = datasourceRepository.findOneById(ds_id);
			if (!result.isPresent())
				throw new Exception("Could not find datasource " + ds_id);
			Datasource datasource = result.get();
			verifyModifyPermission(principal.getName(), datasource);
			Optional<Track> findTrack = datasource.getTracks().stream().filter(test -> test.getId().equals(track_id)).findFirst();
			if (findTrack.isPresent()) {
				Track track = findTrack.get();
				if (track.getStatus() != Track.STATUS_DONE) {
					jobService.add(new TripleConversion(track, mainType, subType, attributeType));
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	
	@RequestMapping(value = "/rest/datasources/{ds_id}/tracks/{track_id}/rawdatafiles", method = RequestMethod.POST,  produces = "application/json")
	@Transactional
	public ResponseEntity<RawDataFileDTO> add(Principal principal, @PathVariable Long ds_id, @PathVariable Long track_id, @RequestBody String filePath) {
		log.debug("REST request to add server file on Track " + track_id);
		try {
			Optional<Datasource> result = datasourceRepository.findOneById(ds_id);
			if (!result.isPresent()) {
				throw new Exception("Could not find datasource " + ds_id);
			}
			Datasource datasource = result.get();
			verifyModifyPermission(principal.getName(), datasource);
			Optional<Track> findTrack = datasource.getTracks().stream().filter(test -> test.getId().equals(track_id)).findFirst();
			if (!findTrack.isPresent()) {
				throw new Exception("Could not find track " + track_id);
			}
			Track track = findTrack.get();
			if (track.getStatus() == Track.STATUS_DONE) {
				throw new Exception("Cannot add files to converted tracks.");
			}
			RawDataFile dataFile = new RawDataFile(filePath);
			dataFile.setTrack(track);
			dataFile.setStatus(RawDataFile.STATUS_WAITING);
			rawDataFileRepository.save(dataFile);
			track.getRawDataFiles().add(dataFile);
			trackRepository.save(track);
			return new ResponseEntity<>(new RawDataFileDTO(dataFile),HttpStatus.OK);
		} catch (Exception e) {
			log.error("could not empty track", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/rest/datasources/{ds_id}/tracks/{track_id}/rawdatafiles/{data_id}", method = RequestMethod.DELETE)
	@RolesAllowed(AuthoritiesConstants.ADMIN)
	public ResponseEntity<String> deleteDirect(Principal principal, @PathVariable Long ds_id, @PathVariable Long track_id,
			@PathVariable Long data_id) {
		try {
			Optional<Datasource> result = datasourceRepository.findOneById(ds_id);
			if (!result.isPresent())
				throw new Exception("Could not find datasource " + ds_id);
			Datasource datasource = result.get();
			verifyModifyPermission(principal.getName(), datasource);
			Track foundTrack = null;
			for (Track track : datasource.getTracks()) {
				if (track.getId().equals(track_id)) {
					foundTrack = track;
					break;
				}
			}
			if (foundTrack != null) {
				RawDataFile toRemove = null;
				for (RawDataFile data : foundTrack.getRawDataFiles()) {
					if (data_id.equals(data.getId())) {
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
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/rest/tracks/{id}/rawdatafiles/{data_id}", method = RequestMethod.DELETE)
	@RolesAllowed(AuthoritiesConstants.ADMIN)
	public ResponseEntity<String> deleteDirect(Principal principal, @PathVariable Long id, @PathVariable Long data_id) {
		try {
			Optional<Track> result = trackRepository.findOneById(id);
			if (!result.isPresent())
				throw new Exception("Could not find track " + id);
			Track track = result.get();
			RawDataFile toRemove = null;
			for (RawDataFile data : track.getRawDataFiles()) {
				if (data_id.equals(data.getId())) {
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
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private void verifyModifyPermission(String login, Datasource datasource) throws Exception {
		if (!datasource.getIsPublic() && !datasource.getOwner().getLogin().equals(login)) {
			throw new Exception("You do not have permission to modify this track");
		}

	}
}
