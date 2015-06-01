package exception;

import org.apache.log4j.Logger;

public class CameraNotSavedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(CameraNotSavedException.class);
	
	public CameraNotSavedException() {
		super("Camera couldn't be saved!");
		jlog.error("Camera ouldn't be saved!");
	}
}
