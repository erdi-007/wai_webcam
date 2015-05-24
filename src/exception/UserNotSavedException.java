package exception;

public class UserNotSavedException extends RuntimeException {
	
	public UserNotSavedException() {
		super("User couldn't be saved!");
	}
}
