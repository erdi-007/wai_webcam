package exception;

public class LoginFailedException extends RuntimeException {
	
	public LoginFailedException() {
		super("User konnte nicht eingeloggt werden!");
	}
}
