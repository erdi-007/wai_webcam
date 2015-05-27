package utils;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JndiFactory {

	private static Logger log = Logger.getLogger(JndiFactory.class);

	private static JndiFactory instance = null;

	protected JndiFactory() {
		// Exists only to defeat instantiation.
	}

	public static synchronized JndiFactory getInstance() {
		if (instance == null) {
			instance = new JndiFactory();
		}
		return instance;
	}

	public String getEnvironmentAsString(String envName) throws NamingException {
		String env = null;
		InitialContext ictx = new InitialContext();
		Context myenv = (Context) ictx.lookup("java:comp/env");
		try {
			env = (String) myenv.lookup(envName);
		} catch (NamingException n) {
			log.debug("String Environment '" + envName + "' is missing.");
		}
		return env;
	}

	public Integer getEnvirontmenAsInteger(String envName)
			throws NamingException {
		Integer env = null;
		InitialContext ictx = new InitialContext();
		Context myenv = (Context) ictx.lookup("java:comp/env");
		try {
			env = (Integer) myenv.lookup(envName);
		} catch (NamingException n) {
			log.debug("Integer Environment '" + envName + "' is missing.");
		}
		return env;
	}

	public Boolean getEnvironmentAsBoolean(String envName)
			throws NamingException {
		Boolean env = null;
		InitialContext ictx = new InitialContext();
		Context myenv = (Context) ictx.lookup("java:comp/env");
		try {
			env = (Boolean) myenv.lookup(envName);
		} catch (NamingException n) {
			log.debug("Boolean Environment '" + envName + "' is missing.");
		}
		return env;
	}

	public Connection getConnection(String Datasource) throws NamingException,
			SQLException {
		Context initContext = new InitialContext();

		Context envContext = (Context) initContext.lookup("java:/comp/env");

		if (envContext == null)
			throw new NamingException("InitialContext lookup wrong");

		DataSource ds = (DataSource) envContext.lookup(Datasource);

		if (ds == null)
			throw new NamingException("No Datasource");

		Connection conn = ds.getConnection();

		if (conn == null)
			throw new SQLException("No Connection found");

		return conn;

	}
}
