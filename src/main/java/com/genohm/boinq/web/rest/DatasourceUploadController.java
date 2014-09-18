package com.genohm.boinq.web.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.genohm.boinq.domain.Datasource;
import com.genohm.boinq.repository.DatasourceRepository;

@RestController
public class DatasourceUploadController {

	  	@Inject
	    private DatasourceRepository datasourceRepository;

	  	@RequestMapping(value="/rest/datasources/{id}/upload", method=RequestMethod.GET)
	    public @ResponseBody String provideUploadInfo(Principal principal, @PathVariable Long id) {
	  		String login = principal.getName();
		   Datasource datasource = datasourceRepository.findOne(id);
		   if (Datasource.DATASOURCE_TYPE_LOCAL_FALDO != datasource.getType()) {
		        return "File upload is only supported for local datasources, my dear " + login;
		   }
		   return "POST to this url for uploading data, my dear " + login;
	    }

	    @RequestMapping(value="/rest/datasources/{id}/upload", method=RequestMethod.POST)
	    public @ResponseBody String handleFileUpload(Principal principal, @PathVariable Long id, @RequestParam("name") String name,  @RequestParam("file") MultipartFile file){
			   Datasource datasource = datasourceRepository.findOne(id);
			   if (Datasource.DATASOURCE_TYPE_LOCAL_FALDO != datasource.getType()) {
			        return "File upload is only supported for local datasources";
			   }
			   if (!file.isEmpty()) {
				   try {
					   String fileName = file.getOriginalFilename();
					   String extension = FilenameUtils.getExtension(fileName);
					   if (Datasource.DATASOURCE_STATUS_RAW_DATA != datasource.getStatus()) {

					   }
					   
					   
					   byte[] bytes = file.getBytes();
					   BufferedOutputStream stream =
							   new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
					   stream.write(bytes);
					   stream.close();
					   datasource.setStatus(Datasource.DATASOURCE_STATUS_RAW_DATA);
					   datasourceRepository.saveAndFlush(datasource);
					   return "You successfully uploaded " + name + " into " + name + "-uploaded !";
				   } catch (Exception e) {
					   return "You failed to upload " + name + " => " + e.getMessage();
				   }
			   } else {
				   return "You failed to upload " + name + " because the file was empty.";
			   }
	    }

	
	
}
