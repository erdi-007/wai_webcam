package exception;

public class CameraNotSavedException extends RuntimeException {
	
	public CameraNotSavedException() {
		super("Camera couldn't be saved!");
	}
}
