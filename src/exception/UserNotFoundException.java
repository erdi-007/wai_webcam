package exception;

public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException() {
		super("User couldn't be found!");
	}
}
