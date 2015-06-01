package exception;

import org.apache.log4j.Logger;

public class UserNotFoundException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(UserNotFoundException.class);
	
	public UserNotFoundException() {
		super("User couldn't be found!");
		jlog.error("User couldn't be found");
	}
}
