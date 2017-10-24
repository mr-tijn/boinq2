package org.boinq.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.commons.io.IOUtils;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RFileInputStream;
import org.rosuda.REngine.Rserve.RFileOutputStream;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RClientService {

	private static final Logger log = LoggerFactory.getLogger(RClientService.class);
	
	@Value("${spring.R.port}")
	private int port = 6311;
	@Value("${spring.R.host}")
	private String host = "localhost";
	
	private RConnection _rConnection;
		
	private RConnection rConnection() throws RserveException {
		if (null == _rConnection || !_rConnection.isConnected()) {
			log.info("Starting R connection");
			this._rConnection = new RConnection(host, port);
		}
		return _rConnection;
	}
	
	
	public void sourceScript(String scriptPath) throws RserveException {
		String command = String.format("source(\"%s\")", scriptPath.toString());
		rConnection().eval(command);
	}
	
	
	public void putFile(File sourceFile, String targetFile) throws IOException, RserveException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(sourceFile));
		RFileOutputStream out = rConnection().createFile(targetFile);
		IOUtils.copy(in,out);
		in.close();
		out.close();
	}
	
	public void getFile(File targetFile, String sourceFile) throws IOException, RserveException {
		RFileInputStream in = rConnection().openFile(sourceFile);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
		IOUtils.copy(in,out);
		in.close();
		out.close();
	}
	
	public String evalForString(String script) throws RserveException, REXPMismatchException {
		REXP res = rConnection().eval(script);
		return res.asString();
	}

	public Integer evalForInteger(String script) throws RserveException, REXPMismatchException {
		REXP res = rConnection().eval(script);
		return res.asInteger();
	}
	
	public void eval(String script) throws RserveException {
		rConnection().eval(script);
	}
	
	@PreDestroy
	public void close() {
		if (_rConnection != null) {
			_rConnection.close();
			_rConnection = null;
		}
	}

}
