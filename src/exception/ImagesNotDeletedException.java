package exception;

public class ImagesNotDeletedException extends RuntimeException {
	
	public ImagesNotDeletedException() {
		super("Bilder konnte nicht gelöscht werden!");
	}
}
