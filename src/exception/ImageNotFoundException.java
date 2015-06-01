package exception;

import org.apache.log4j.Logger;

public class ImageNotFoundException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(ImageNotFoundException.class);
	
	public ImageNotFoundException() {
		super("Image couldn't be found!");
		jlog.error("Image couldn't be found!");
	}
}
