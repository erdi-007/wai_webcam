package exception;

public class ImageNotSavedException extends RuntimeException {
	
	public ImageNotSavedException() {
		super("Bild konnte nicht gespeichert werden!");
	}
}
