package jndi;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JndiFactory {
	
	private static JndiFactory instance = new JndiFactory();

	private JndiFactory() {
	}

	public static JndiFactory getInstance() {	
		return instance;
	}
	
	public Connection getConnection(String Datasource) throws NamingException, SQLException {
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

