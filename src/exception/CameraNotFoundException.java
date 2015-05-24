package exception;

public class CameraNotFoundException extends RuntimeException {
	
	public CameraNotFoundException() {
		super("Camera couldn't be found!");
	}
}
