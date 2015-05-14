package exception;

public class CameraNotSavedException extends RuntimeException {
	
	public CameraNotSavedException() {
		super("Kamera konnte nicht gespeichert werden!");
	}
}
