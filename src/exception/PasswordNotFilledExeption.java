package exception;

public class PasswordNotFilledExeption extends RuntimeException {
	
	public PasswordNotFilledExeption() {
		super("Passwort darf nicht leer sein!");
	}
}
