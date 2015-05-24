package exception;

public class UserNotDeletedException extends RuntimeException {
	
	public UserNotDeletedException() {
		super("User couldn't be deleted!");
	}
}
