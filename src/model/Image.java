package model;

import java.sql.Timestamp;

public class Image {
	private Long cameraID;
	private Timestamp date;
	private String name;
	
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
