package exception;

import org.apache.log4j.Logger;

public class CameraNotFoundException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(CameraNotFoundException.class);
	
	public CameraNotFoundException() {
		super("Camera couldn't be found!");
		jlog.error("Camera couldn't be Found");
	}
}
