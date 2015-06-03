package model;

import java.sql.Timestamp;

public class Image {
	private Long cameraID;
	private Timestamp date;
	private String path;
	private String pathThumbnail;
	
	public Long getCameraID() {
		return cameraID;
	}
	
	public void setCameraID(Long id) {
		this.cameraID = id;
	}
	
	public Timestamp getDate() {
		return date;
	}
	
	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPathThumbnail() {
		return pathThumbnail;
	}
	
	public void setPathThumbnail(String pathThumbnail) {
		this.pathThumbnail = pathThumbnail;
	}

}
