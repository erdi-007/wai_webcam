package exception;

public class PrivilegeNotFoundException extends RuntimeException {
	
	public PrivilegeNotFoundException() {
		super("Privileg couldn't be found!");
	}
}
