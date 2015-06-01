package exception;

import org.apache.log4j.Logger;

public class CameraNotDeletedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(CameraNotDeletedException.class);
	
	public CameraNotDeletedException() {
		super("Camera couldn't be deleted!");
		jlog.error("Camera couldn't be deleted");
	}
}
