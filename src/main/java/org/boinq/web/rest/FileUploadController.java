package org.boinq.web.rest;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.boinq.domain.Datasource;
import org.boinq.domain.RawDataFile;
import org.boinq.domain.Track;
import org.boinq.domain.jobs.TripleConversion;
import org.boinq.repository.DatasourceRepository;
import org.boinq.repository.RawDataFileRepository;
import org.boinq.repository.TrackRepository;
import org.boinq.repository.UserRepository;
import org.boinq.service.FileManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

	private static Logger log = LoggerFactory.getLogger(FileUploadController.class);
	
	@Inject
	private TrackRepository trackRepository;

	@Inject
	private DatasourceRepository datasourceRepository;
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private RawDataFileRepository rawDataFileRepository;

	@Inject
	private FileManagerService fileManagerService;

	@RequestMapping(value="/rest/tracks/{id}/upload", method=RequestMethod.GET)
	public @ResponseBody String provideUploadInfo(@PathVariable Long id) {
		return "POST to this url for uploading data";
	}

	@RequestMapping(value="/rest/datasources/{ds_id}/tracks/{id}/upload", method=RequestMethod.GET)
	public @ResponseBody String provideUploadInfo(@PathVariable Long ds_id, @PathVariable Long id) {
		return "POST to this url for uploading data";
	}

	@RequestMapping(value="/rest/datasources/{ds_id}/tracks/{id}/upload", method=RequestMethod.POST)
	@Transactional
	public @ResponseBody ResponseEntity<String> handleFileUpload(Principal principal, @PathVariable Long ds_id, @PathVariable Long id, @RequestParam("name") String name,  @RequestParam("file") MultipartFile file) {
		try {
			String login = principal.getName();
			Datasource datasource = datasourceRepository.findOne(ds_id);
			Track track = trackRepository.findOne(id);
			if (!datasource.getTracks().contains(track)) {
	    		return new ResponseEntity<String>("Track and datasource do not match", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			verifyUploadPermission(login, datasource);
			if (file.isEmpty()) {
				log.error("Empty file " + file.getName());
				return new ResponseEntity<String>("Empty file", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			String fileName = file.getOriginalFilename();
			checkExtension(FilenameUtils.getExtension(fileName),track);
			track.setStatus(Track.STATUS_RAW_DATA);
			Set<RawDataFile> rawDataFiles = track.getRawDataFiles();
			if (rawDataFiles == null) {
				rawDataFiles = new HashSet<RawDataFile>();
				track.setRawDataFiles(rawDataFiles);
			}
			RawDataFile newDataFile = fileManagerService.putFile(file);
			newDataFile.setTrack(track);
			newDataFile = rawDataFileRepository.saveAndFlush(newDataFile);
			rawDataFiles.add(newDataFile);
			trackRepository.save(track);
			trackRepository.flush();
			String result = "You successfully uploaded " + name + " into " + newDataFile.getFilePath();
			return new ResponseEntity<String>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Problem with upload",e);
			return new ResponseEntity<String>("A problem occurred: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void checkExtension(String extension, Track track) throws Exception {
		if (null == track.getFileType()) {
			for (String ext : TripleConversion.SUPPORTED_EXTENSIONS) {
				if (extension.equalsIgnoreCase(ext)) return; 
			}
			log.error("Unsupported file format " + extension);
			throw new Exception("Unsupported file format "+extension);
		}
		else {
			if (extension.equalsIgnoreCase(track.getFileType())) return;
			log.error("Cannot mix file formats currently. Please use "+track.getFileType());
			throw new Exception("Cannot mix file formats currently. Please use "+track.getFileType());
		}
	}

	private void verifyUploadPermission(String login, Datasource datasource) throws Exception {
		if (datasource.getType() != Datasource.TYPE_LOCAL) {
			log.error("Only possible for local datasources");
			throw new Exception("Only possible for local datasources");
		}
		if (datasource.getOwner() != userRepository.findOneByLogin(login).get()) {
			log.error("Only possible for your own datasources");
			throw new Exception("Only possible for your own datasources");
		}
	}
	
}
