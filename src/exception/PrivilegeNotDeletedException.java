package exception;

import org.apache.log4j.Logger;

public class PrivilegeNotDeletedException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(PrivilegeNotDeletedException.class);
	
	public PrivilegeNotDeletedException() {
		super("Privileg couldn't be deleted!");
		jlog.error("Privilege couldn't be deleted!");
	}
}
