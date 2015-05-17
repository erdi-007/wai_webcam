package exception;

public class UserExistsException extends RuntimeException {
	
	public UserExistsException() {
		super("Username bereits vorhanden!");
	}
}
