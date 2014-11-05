package com.genohm.boinq.web.rest;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.genohm.boinq.service.FusekiMgmtService;
import com.genohm.boinq.web.rest.dto.FusekiInfoDTO;



@RestController
@RequestMapping("/app")
public class FusekiManagementResource {

	@Inject
	FusekiMgmtService fusekiManager;
	
    @RequestMapping(value = "/rest/management/fuseki",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public ResponseEntity<FusekiInfoDTO> fusekiManager(@RequestBody FusekiInfoDTO fusekiInfoDTO) {
    	String command = fusekiInfoDTO.getCommand();
    	try {
    		if (FusekiInfoDTO.COMMAND_STATUS.equals(command)) {
    			fusekiInfoDTO.setServerStatus(fusekiManager.getStatus());
    		} else if (FusekiInfoDTO.COMMAND_START.equals(command)) {
    			fusekiManager.startServer();
    			fusekiInfoDTO.setServerStatus(fusekiManager.getStatus());
    		} else if (FusekiInfoDTO.COMMAND_STOP.equals(command)) {
    			fusekiManager.stopServer();
    			fusekiInfoDTO.setServerStatus(fusekiManager.getStatus());      	
    		} else if (FusekiInfoDTO.COMMAND_RESTART.equals(command)) {
    			fusekiManager.restartServer();
    			fusekiInfoDTO.setServerStatus(fusekiManager.getStatus());
    		}
    	}
    	catch (Throwable t) {
    		fusekiInfoDTO.setErrorMessage(t.getMessage());
    		return new ResponseEntity<FusekiInfoDTO>(fusekiInfoDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
        return new ResponseEntity<FusekiInfoDTO>(fusekiInfoDTO, HttpStatus.OK);
    }
	
}
