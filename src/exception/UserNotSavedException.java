package exception;

import org.apache.log4j.Logger;

public class UserNotSavedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(UserNotSavedException.class);
	
	public UserNotSavedException() {
		super("User couldn't be saved!");
		jlog.error("User couldn't be saved!");
	}
}
