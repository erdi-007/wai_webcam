package exception;

public class ImageNotSavedException extends RuntimeException {
	
	public ImageNotSavedException() {
		super("Image couldn't be saved!");
	}
}
