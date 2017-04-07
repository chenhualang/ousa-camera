package com.server.api.model;

public class PhotoInfo {
	
	private String fileName;
	
	
	private String fileAbsolutPath;
	
	private String deviceid;
	
	private String threshold;

	public String getFileAbsolutPath() {
		return fileAbsolutPath;
	}

	public void setFileAbsolutPath(String fileAbsolutPath) {
		this.fileAbsolutPath = fileAbsolutPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}




	
	

}
