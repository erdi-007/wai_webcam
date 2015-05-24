package exception;

public class CameraNotDeletedException extends RuntimeException {
	
	public CameraNotDeletedException() {
		super("Camera couldn't be deleted!");
	}
}
