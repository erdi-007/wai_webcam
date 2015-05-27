package dao;

import java.sql.*;

import javax.naming.NamingException;

import jndi.JndiFactory;

import exception.DataBaseAccessException;

public class Dao {
	
	final String DATABASE = "jdbc/libraryDB";
	final JndiFactory jndi = JndiFactory.getInstance();
	
	private Connection connection = null;
	
	protected void executeUpdate(String SqlStatement, Object[] values)
	{		
		try
		{
			connection = connectToDB(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SqlStatement);
			setValues(preparedStatement, values);
			preparedStatement.executeUpdate();
		}
		catch (Exception e)
		{
			e.getStackTrace();
			throw new DataBaseAccessException();
		}
	}
	
	protected ResultSet executeQuery(String SqlStatement)
	{
		ResultSet resultSet = null;
		
		try
		{
			connection = connectToDB(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SqlStatement);
			resultSet = preparedStatement.executeQuery();
		}
		catch (Exception e)
		{
			e.getStackTrace();
			throw new DataBaseAccessException();
		}
		
		return resultSet;
	}
	
	protected ResultSet executeQuery(String SqlStatement, Object[] values)
	{
		ResultSet resultSet = null;
		
		try
		{
			connection = connectToDB(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SqlStatement);
			setValues(preparedStatement, values);
			resultSet = preparedStatement.executeQuery();
		}
		catch (Exception e)
		{
			e.getStackTrace();
			throw new DataBaseAccessException();
		}
		
		return resultSet;
	}
	
	private Connection connectToDB(String DB) throws NamingException, SQLException
	{
		return jndi.getConnection(DB);
	}
	
	
	private void setValues(PreparedStatement preparedStatement, Object[] values) throws SQLException
	{
		for(int i = 0; i < values.length; i++)
		{
			preparedStatement.setObject(i + 1, values[i]);
		}
	}
	
	protected void closeConnectionToDB()
	{
		if (connection != null) 
		{
			try 
			{
				connection.close();
				connection = null;
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}				
		}
	}
}
