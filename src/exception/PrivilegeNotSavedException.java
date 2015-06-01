package exception;

import org.apache.log4j.Logger;

public class PrivilegeNotSavedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(PrivilegeNotSavedException.class);
	
	public PrivilegeNotSavedException() {
		super("Privileg couldn't be saved!");
		jlog.error("Privilege couldn't be saved");
	}
}
