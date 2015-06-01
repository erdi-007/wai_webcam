package exception;

import org.apache.log4j.Logger;

public class ImageNotSavedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(ImageNotSavedException.class);
	
	public ImageNotSavedException() {
		super("Image couldn't be saved!");
		jlog.error("Image couldn't be saved!");
	}
}
