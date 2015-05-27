package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.CameraNotDeletedException;
import exception.CameraNotFoundException;
import exception.CameraNotSavedException;
import exception.DataBaseAccessException;
import exception.UserNotDeletedException;
import model.Camera;

public class CameraDao extends Dao {
	
	private final String SQL_ADD_NEW_CAMERA = "insert into public.cameras (name, description, url) values (?,?,?)";
	private final String SQL_UPDATE_CAMERA = "update public.cameras set name = ?, description = ?, url = ? where cameraID = ?";
	private final String SQL_DELETE_CAMERA = "delete from public.cameras where cameraID = ?";
	private final String SQL_SELECT_ALL_CAMERAS = "select * from public.cameras order by cameraID";
	private final String SQL_SELECT_CAMERA_BY_ID = "select * from public.cameras where cameraID = ?";

	public void saveCamera(Camera camera)
	{
		if (camera == null)
			throw new IllegalArgumentException("camera can not be null");
		
		try
		{
			// Camera is currently known
			if(camera.getId() != null)
			{
				Object[] values = new Object[4];
				values[0] = camera.getName();
				values[1] = camera.getDescription();
				values[2] = camera.getUrl();
				values[3] = camera.getId();
				
				executeUpdate(SQL_UPDATE_CAMERA, values);
			}
			// New Camera
			else
			{
				Object[] values = new Object[3];
				values[0] = camera.getName();
				values[1] = camera.getDescription();
				values[2] = camera.getUrl();
				
				executeUpdate(SQL_ADD_NEW_CAMERA, values);
			}
		}
		catch (DataBaseAccessException e)
		{
			throw new CameraNotSavedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public void deleteCamera(Long cameraID)
	{
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		PrivilegeDao privilegeDao = new PrivilegeDao();
		privilegeDao.deletePrivilegesFromCamera(cameraID);
		
		try
		{
			Object values[] = new Object[1];
			values[0] = cameraID;
			
			executeUpdate(SQL_DELETE_CAMERA, values);
		}
		catch (DataBaseAccessException e)
		{
			throw new CameraNotDeletedException();
		}
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public List<Camera> getListOfAllCameras()
	{
		List<Camera> cameras = new ArrayList<Camera>();
		
		try 
		{
			ResultSet resultSet = executeQuery(SQL_SELECT_ALL_CAMERAS);
		
			while (resultSet.next()) 
			{
				Camera camera = new Camera();
				
				camera.setId(resultSet.getLong("cameraID"));
				camera.setName(resultSet.getString("name"));
				camera.setDescription(resultSet.getString("description"));
				camera.setUrl(resultSet.getString("url"));
				cameras.add(camera);
			}
			
			return cameras;
		} 
		catch (DataBaseAccessException e)
		{
			throw new CameraNotFoundException();
		}
		catch (SQLException e) 
		{
			e.getStackTrace();
			throw new CameraNotFoundException();
		}	
		finally
		{
			closeConnectionToDB();
		}
	}
	
	public Camera getCamera(Long cameraID)
	{
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		try
		{
			Object values[] = new Object[1];
			values[0] = cameraID;
			
			ResultSet resultSet = executeQuery(SQL_SELECT_CAMERA_BY_ID, values);
			
			return extractCameraFromResultSet(resultSet);	
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
	
	private Camera extractCameraFromResultSet(ResultSet resultSet)
	{	
		try 
		{
			if (resultSet.next()) 
			{
				Camera camera = new Camera();
				
				camera.setId(resultSet.getLong("cameraID"));
				camera.setName(resultSet.getString("name"));
				camera.setDescription(resultSet.getString("description"));
				camera.setUrl(resultSet.getString("url"));
				
				return camera;
			}
			else
			{
				throw new CameraNotFoundException();
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new CameraNotFoundException();
		}	
	}
}
