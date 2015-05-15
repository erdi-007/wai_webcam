package model;

public class Privilege {
	private Long userId;
	private	Long cameraId;
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userid) {
		this.userId = userid;
	}
	
	public Long getCameraId() {
		return cameraId;
	}
	
	public void setCameraId(Long cameraid) {
		this.cameraId = cameraid;
	}
}
