package exception;

public class AdminRightsCantBeRemoved extends RuntimeException {
	
	public AdminRightsCantBeRemoved() {
		super("Die Adminrechte k�nnen nicht gel�scht werden!");
	}
}
