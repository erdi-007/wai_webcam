package exception;

import org.apache.log4j.Logger;

public class ImagesNotDeletedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(ImagesNotDeletedException.class);
	
	public ImagesNotDeletedException() {
		super("Image couldn't be deleted!");
		jlog.error("Image couldn't be deleted");
	}
}
