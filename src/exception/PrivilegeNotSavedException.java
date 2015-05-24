package exception;

public class PrivilegeNotSavedException extends RuntimeException {
	
	public PrivilegeNotSavedException() {
		super("Privileg couldn't be saved!");
	}
}
