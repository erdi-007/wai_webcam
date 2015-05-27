package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.CameraNotFoundException;
import exception.DataBaseAccessException;
import exception.PrivilegeNotDeletedException;
import exception.PrivilegeNotFoundException;
import exception.PrivilegeNotSavedException;
import exception.UserNotFoundException;

public class PrivilegeDao extends Dao {
	
	private final String SQL_ADD_NEW_PRIVILEGE = "insert into public.privileges (userID, cameraID) values (?,?)";
	private final String SQL_DELETE_PRIVILEGE = "delete from public.privileges where userID = ? and cameraID = ?";
	private final String SQL_DELETE_PRIVILEGES_FROM_USER = "delete from public.privileges where userID = ?";
	private final String SQL_DELETE_PRIVILEGES_FROM_CAMERA = "delete from public.privileges where cameraID = ?";
	private final String SQL_SELECT_PRIVILEGE = "select * from public.privileges where userID = ? and cameraID = ?";
	private final String SQL_SELECT_PRIVILEGES_FOR_USER = "select * from public.privileges where userID = ? order by cameraID";
	private final String SQL_SELECT_PRIVILEGES_FOR_CAMERA = "select * from public.privileges where cameraID = ? and userID != 1 order by userID";

	public void savePrivilege(Long userID, Long cameraID)
	{
		if (userID == null || cameraID == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");
		
		try
		{
			Object[] values = new Object[2];
			values[0] = userID;
			values[1] = cameraID;
			
			executeUpdate(SQL_ADD_NEW_PRIVILEGE, values);
		}
		catch (DataBaseAccessException e)
		{
			throw new PrivilegeNotSavedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public void deletePrivilege(Long userID, Long cameraID)
	{
		if (userID == null || cameraID == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");
		
		try
		{
			Object[] values = new Object[2];
			values[0] = userID;
			values[1] = cameraID;
			
			executeUpdate(SQL_DELETE_PRIVILEGE, values);
		}
		catch (DataBaseAccessException e)
		{
			throw new PrivilegeNotDeletedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public void deletePrivilegesFromUser(Long userID)
	{
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		try
		{
			Object[] values = new Object[1];
			values[0] = userID;
			
			executeUpdate(SQL_DELETE_PRIVILEGES_FROM_USER, values);
		}
		catch (DataBaseAccessException e)
		{
			throw new PrivilegeNotDeletedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public void deletePrivilegesFromCamera(Long cameraID)
	{
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		try
		{
			Object[] values = new Object[1];
			values[0] = cameraID;
			
			executeUpdate(SQL_DELETE_PRIVILEGES_FROM_CAMERA, values);
		}
		catch (DataBaseAccessException e)
		{
			throw new PrivilegeNotDeletedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public boolean isUserAuthorizedForCamera(Long userID, Long cameraID)
	{
		if (userID == null || cameraID == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");
		
		try
		{
			Object[] values = new Object[2];
			values[0] = userID;
			values[1] = cameraID;
			
			ResultSet resultSet = executeQuery(SQL_SELECT_PRIVILEGE, values);
			
			if (resultSet.next()) 
				return true;
			else 
				return false;
		}
		catch (DataBaseAccessException e)
		{
			throw new PrivilegeNotFoundException();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new PrivilegeNotFoundException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public List<Long> getPrivilegesForUser(Long userID)
	{
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		try
		{
			Object[] values = new Object[1];
			values[0] = userID;
			
			ResultSet resultSet = executeQuery(SQL_SELECT_PRIVILEGES_FOR_USER, values);
			
			List<Long> cameras = new ArrayList<Long>();
			
			while (resultSet.next()) 
			{
				cameras.add(resultSet.getLong("cameraID"));
			}			
			
			return cameras;
		}
		catch (DataBaseAccessException e)
		{
			throw new UserNotFoundException();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new UserNotFoundException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public List<Long> getPrivilegesForCamera(Long cameraID)
	{
		if (cameraID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		try
		{
			Object[] values = new Object[1];
			values[0] = cameraID;
			
			ResultSet resultSet = executeQuery(SQL_SELECT_PRIVILEGES_FOR_CAMERA, values);
			
			List<Long> users = new ArrayList<Long>();
			
			while (resultSet.next()) 
			{
				users.add(resultSet.getLong("cameraID"));
			}			
			
			return users;
		}
		catch (DataBaseAccessException e)
		{
			throw new CameraNotFoundException();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new CameraNotFoundException();
		}	
		finally
		{
			closeConnectionToDB();
		}
	}
}
