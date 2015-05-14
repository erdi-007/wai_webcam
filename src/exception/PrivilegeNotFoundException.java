package exception;

public class PrivilegeNotFoundException extends RuntimeException {
	
	public PrivilegeNotFoundException() {
		super("Privileg konnte nicht gefunden werden!");
	}
}
