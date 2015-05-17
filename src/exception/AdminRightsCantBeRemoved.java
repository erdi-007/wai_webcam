package exception;

public class AdminRightsCantBeRemoved extends RuntimeException {
	
	public AdminRightsCantBeRemoved() {
		super("Die Adminrechte können nicht gelöscht werden!");
	}
}
