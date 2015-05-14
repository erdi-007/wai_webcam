package exception;

public class UserNotSavedException extends RuntimeException {
	
	public UserNotSavedException() {
		super("User konnte nicht gespeichert werden!");
	}
}
