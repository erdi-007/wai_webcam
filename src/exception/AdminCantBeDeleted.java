package exception;

public class AdminCantBeDeleted extends RuntimeException {
	
	public AdminCantBeDeleted() {
		super("Der Admin kann nicht gelöscht werden!");
	}
}
