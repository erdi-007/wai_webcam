package exception;

public class ImageNotFoundException extends RuntimeException {
	
	public ImageNotFoundException() {
		super("Bild konnte nicht gefunden werden!");
	}
}
