package exception;

import org.apache.log4j.Logger;

public class UserNotDeletedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(UserNotDeletedException.class);
	
	public UserNotDeletedException() {
		super("User couldn't be deleted!");
		jlog.error("User couldn't be deleted!");
	}
}
