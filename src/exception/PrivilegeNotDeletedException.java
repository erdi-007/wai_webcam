package exception;

public class PrivilegeNotDeletedException extends RuntimeException {
	
	public PrivilegeNotDeletedException() {
		super("Privileg couldn't be deleted!");
	}
}
