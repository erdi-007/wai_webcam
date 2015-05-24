package exception;

public class ImageNotFoundException extends RuntimeException {
	
	public ImageNotFoundException() {
		super("Image couldn't be found!");
	}
}
