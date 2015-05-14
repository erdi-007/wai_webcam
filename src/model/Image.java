package model;

import java.sql.Timestamp;

public class Image {
	private Long cameraID;
	private Timestamp date;
	
	public Long get_cameraID() {
		return cameraID;
	}
	
	public void set_cameraID(Long id) {
		this.cameraID = id;
	}
	
	public Timestamp get_Date() {
		return date;
	}
	
	public void set_Date(Timestamp date) {
		this.date = date;
	}
}
