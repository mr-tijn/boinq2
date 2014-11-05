package com.genohm.boinq.web.rest;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.domain.RawDataFile;
import com.genohm.boinq.domain.jobs.TripleConversion;
import com.genohm.boinq.repository.DatasourceRepository;
import com.genohm.boinq.repository.RawDataFileRepository;
import com.genohm.boinq.service.FileManagerService;

@RestController
public class DatasourceUploadController {

	  	@Inject
	    private DatasourceRepository datasourceRepository;
	  	@Inject
	  	private RawDataFileRepository rawDataFileRepository;
	  	@Inject
	  	private FileManagerService fileManagerService;

	  	@RequestMapping(value="/rest/datasources/{id}/upload", method=RequestMethod.GET)
	    public @ResponseBody String provideUploadInfo(Principal principal, @PathVariable Long id) {
	  		String login = principal.getName();
		   Datasource datasource = datasourceRepository.findOne(id);
		   if (Datasource.TYPE_LOCAL_FALDO != datasource.getType()) {
		        return "File upload is only supported for local datasources, my dear " + login;
		   }
		   return "POST to this url for uploading data, my dear " + login;
	    }

	    @RequestMapping(value="/rest/datasources/{id}/upload", method=RequestMethod.POST)
	    @Transactional
	    public @ResponseBody String handleFileUpload(Principal principal, @PathVariable Long id, @RequestParam("name") String name,  @RequestParam("file") MultipartFile file){
			   Datasource datasource = datasourceRepository.findOne(id);
			   if (Datasource.TYPE_LOCAL_FALDO != datasource.getType()) {
			        return "File upload is only supported for local datasources";
			   }
			   if (!file.isEmpty()) {
				   try {
					   String fileName = file.getOriginalFilename();
					   checkExtension(FilenameUtils.getExtension(fileName));
					   datasource.setStatus(Datasource.STATUS_RAW_DATA);
					   Set<RawDataFile> rawDataFiles = datasource.getRawDataFiles();
					   if (rawDataFiles == null) {
						   rawDataFiles = new HashSet<RawDataFile>();
						   datasource.setRawDataFiles(rawDataFiles);
					   }
					   RawDataFile newDataFile = fileManagerService.putFile(file);
					   newDataFile.setDatasource(datasource);
					   newDataFile = rawDataFileRepository.saveAndFlush(newDataFile);
					   rawDataFiles.add(newDataFile);
					   datasourceRepository.save(datasource);
					   datasourceRepository.flush();
					   return "You successfully uploaded " + name + " into " + newDataFile.getFilePath();
				   } catch (Exception e) {
					   return "You failed to upload " + name + " => " + e.getMessage();
				   }
			   } else {
				   return "You failed to upload " + name + " because the file was empty.";
			   }
	    }

	    private void checkExtension(String extension) throws Exception {
	    	for (String ext : TripleConversion.SUPPORTED_EXTENSIONS) {
	    		if (extension.equalsIgnoreCase(ext)) return; 
	    	}
	    	throw new Exception("Unsupported file format "+extension);
	    }
	
}
