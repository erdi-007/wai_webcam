package model;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

public class Image {
	private Long cameraID;
	private Timestamp date;
	
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
	
	public String getPath() throws IOException {
		
		if(date == null || cameraID == null)
			throw new IllegalArgumentException("date or cameraID can not be null");
			
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		String dir = this.getClass().getClassLoader().getResource("").getPath();
		String delete = "/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/wai_webcam/WEB-INF/classes/";
		
		dir = dir.replace(delete, "");
		
		String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		
		if((calendar.get(Calendar.MONTH)+1) < 10)
			month = "0" + (calendar.get(Calendar.MONTH)+1);
		if(calendar.get(Calendar.DAY_OF_MONTH) < 10)
			day = "0" + calendar.get(Calendar.DAY_OF_MONTH);		
		
		String path = dir + "//images//cam_" + cameraID.toString() + "//" + calendar.get(Calendar.YEAR) + "//"
				+ month + "//" + day + "//";				
				
		return path;
	}
	
	public String getName() {
		
		if(date == null)
			throw new IllegalArgumentException("date can not be null");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
		String min = Integer.toString(calendar.get(Calendar.MINUTE));
		
		if(calendar.get(Calendar.HOUR_OF_DAY) < 10)
			hour = "0" + calendar.get(Calendar.HOUR_OF_DAY);
		if(calendar.get(Calendar.MINUTE) < 10)
			min = "0" + calendar.get(Calendar.MINUTE);				
		
		String name = hour + "_" + min + ".jpg";
		
		return name;
	}
}
