package exception;

import org.apache.log4j.Logger;

public class LoginFailedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(LoginFailedException.class);
	
	public LoginFailedException() {
		super("User couldn't be logged in!");
		jlog.error("User couldn't be logged in!");
	}
}
