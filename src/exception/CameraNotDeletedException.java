package exception;

public class CameraNotDeletedException extends RuntimeException {
	
	public CameraNotDeletedException() {
		super("Kamera konnte nicht gel�scht werden!");
	}
}
