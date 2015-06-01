package exception;

import org.apache.log4j.Logger;

public class DataBaseAccessException extends RuntimeException {
	
	private static Logger jlog = Logger.getLogger(DataBaseAccessException.class);
	
	public DataBaseAccessException()
	{
		super("Error: Accessing Database");
		jlog.error("Accessing Database");
	}
}
