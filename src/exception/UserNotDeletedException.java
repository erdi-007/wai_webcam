package exception;

public class UserNotDeletedException extends RuntimeException {
	
	public UserNotDeletedException() {
		super("User konnte nicht gel�scht werden!");
	}
}
