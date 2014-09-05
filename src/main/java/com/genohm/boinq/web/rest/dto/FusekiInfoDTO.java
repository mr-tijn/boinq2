package com.genohm.boinq.web.rest.dto;

public class FusekiInfoDTO {
	
	public static final String COMMAND_START = "start";
	public static final String COMMAND_STOP = "stop";
	public static final String COMMAND_RESTART = "restart";
	public static final String COMMAND_STATUS = "status";
	
	private String serverStatus;
	private String errorMessage;
	private String command;
	public String getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(String serverStatus) {
		this.serverStatus = serverStatus;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public FusekiInfoDTO() {}
	public FusekiInfoDTO(String serverStatus, String errorMessage,
			String command) {
		super();
		this.serverStatus = serverStatus;
		this.errorMessage = errorMessage;
		this.command = command;
	}
	
	@Override
	public String toString() {
		return String.format("{serverStatus='%s',errorMessage='%s',command='%s'", serverStatus, errorMessage, command);
	}

}
