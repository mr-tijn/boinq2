package org.boinq.service;

public interface EndpointManagerService {
	
	public Boolean isUp();
	public String getStatus();
	public void startServer();
	public void stopServer();
	public void restartServer();

}
