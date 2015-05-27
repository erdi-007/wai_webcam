package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.DataBaseAccessException;
import exception.UserNotDeletedException;
import exception.UserNotFoundException;
import exception.UserNotSavedException;
import model.User;

public class UserDao extends Dao {
	
	private final String SQL_ADD_NEW_USER = "insert into public.user (is_admin, name, password) values (?,?,?)";
	private final String SQL_UPDATE_USER = "update public.user set is_admin = ?, name = ?, password = ? where userID = ?";
	private final String SQL_DELETE_USER = "delete from public.user where userID = ?";
	private final String SQL_SELECT_ALL_USERS = "select * from public.user where name != 'admin' order by userID";
	private final String SQL_SELECT_USER_BY_NAME = "select * from public.user where name = ?";
	private final String SQL_SELECT_USER_BY_ID = "select * from public.user where userID = ?";
	
	public void saveUser(User user)
	{
		if (user == null)
			throw new IllegalArgumentException("user can not be null");
		
		try
		{
			// User is currently known
			if(user.getId() != null)
			{
				Object[] values = new Object[4];
				values[0] = user.isAdmin();
				values[1] = user.getName();
				values[2] = user.getPassword();
				values[3] = user.getId();
				
				executeUpdate(SQL_UPDATE_USER, values);
			}
			// New user
			else
			{
				Object[] values = new Object[3];
				values[0] = user.isAdmin();
				values[1] = user.getName();
				values[2] = user.getPassword();
				
				executeUpdate(SQL_ADD_NEW_USER, values);
			}
		}
		catch (DataBaseAccessException e)
		{
			throw new UserNotSavedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public void deleteUser(Long userID)
	{
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		PrivilegeDao privilegeDao = new PrivilegeDao();
		privilegeDao.deletePrivilegesFromUser(userID);
		
		try
		{
			Object values[] = new Object[1];
			values[0] = userID;
			
			executeUpdate(SQL_DELETE_USER, values);
		}
		catch (DataBaseAccessException e)
		{
			throw new UserNotDeletedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public List<User> getListOfAllUsers()
	{
		List<User> users = new ArrayList<User>();
		
		try 
		{
			ResultSet resultSet = executeQuery(SQL_SELECT_ALL_USERS);
		
			while (resultSet.next()) 
			{
				User user = new User();
				
				user.setId(resultSet.getLong("userID"));
				user.setAdmin(resultSet.getBoolean("is_admin"));
				user.setName(resultSet.getString("name"));
				user.setPassword(resultSet.getString("password"));
				users.add(user);
			}
			
			return users;
		} 
		catch (DataBaseAccessException e)
		{
			throw new UserNotFoundException();
		}
		catch (SQLException e) 
		{
			e.getStackTrace();
			throw new UserNotFoundException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public User getUser(String userName)
	{
		if (userName == null)
			throw new IllegalArgumentException("name can not be null");
		
		try
		{
			Object values[] = new Object[1];
			values[0] = userName;
			
			ResultSet resultSet = executeQuery(SQL_SELECT_USER_BY_NAME, values);
			
			return extractUserFromResultSet(resultSet);			
		}
		catch (DataBaseAccessException e)
		{
			throw new UserNotDeletedException();
		} 	
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public User getUser(Long userID)
	{
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		try
		{
			Object values[] = new Object[1];
			values[0] = userID;
			
			ResultSet resultSet = executeQuery(SQL_SELECT_USER_BY_ID, values);
			
			return extractUserFromResultSet(resultSet);			
		}
		catch (DataBaseAccessException e)
		{
			throw new UserNotDeletedException();
		} 		
		finally
		{
			closeConnectionToDB();
		}
	}
	
	private User extractUserFromResultSet(ResultSet resultSet)
	{
		try {
			if (resultSet.next()) 
			{
				User user = new User();
				
				user.setId(resultSet.getLong("userID"));
				user.setAdmin(resultSet.getBoolean("is_admin"));
				user.setName(resultSet.getString("name"));
				user.setPassword(resultSet.getString("password"));
				
				return user;
			} 
			else 
			{
				throw new UserNotFoundException();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new UserNotFoundException();
		}
	}
}
