package exception;

public class LoginFailedException extends RuntimeException {
	
	public LoginFailedException() {
		super("User couldn't be logged in!");
	}
}
