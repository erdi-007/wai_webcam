package exception;

public class ImagesNotDeletedException extends RuntimeException {
	
	public ImagesNotDeletedException() {
		super("Image couldn't be deleted!");
	}
}
