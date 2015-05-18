package exception;

public class InputNotFilledExeption extends RuntimeException {
	
	public InputNotFilledExeption() {
		super("Passwort und/oder Name darf nicht leer sein!");
	}
}
