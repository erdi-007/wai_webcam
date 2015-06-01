package exception;

import org.apache.log4j.Logger;

public class PrivilegeNotFoundException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(PrivilegeNotFoundException.class);
	
	public PrivilegeNotFoundException() {
		super("Privileg couldn't be found!");
		jlog.error("Privilege couldn't be found!");
	}
}
