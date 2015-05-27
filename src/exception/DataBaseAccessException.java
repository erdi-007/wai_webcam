package exception;

public class DataBaseAccessException extends RuntimeException {
	
	public DataBaseAccessException()
	{
		super("Error: Accessing Database");
	}
}
