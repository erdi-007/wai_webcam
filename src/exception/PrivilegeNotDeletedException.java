package exception;

public class PrivilegeNotDeletedException extends RuntimeException {
	
	public PrivilegeNotDeletedException() {
		super("Privileg konnte nicht gel�scht werden!");
	}
}
