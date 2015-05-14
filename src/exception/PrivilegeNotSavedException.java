package exception;

public class PrivilegeNotSavedException extends RuntimeException {
	
	public PrivilegeNotSavedException() {
		super("Privileg konnte nicht gespeichert werden!");
	}
}
