package exception;

public class CameraNotFoundException extends RuntimeException {
	
	public CameraNotFoundException() {
		super("Kamera konnte nicht gefunden werden!");
	}
}
