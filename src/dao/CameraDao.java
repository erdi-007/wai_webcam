package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.JndiFactory;
import exception.CameraNotDeletedException;
import exception.CameraNotFoundException;
import exception.CameraNotSavedException;
import model.Camera;

public class CameraDao {

	final String DATABASE = "jdbc/libraryDB";
	final JndiFactory jndi = JndiFactory.getInstance();

	private final String SQL_ADD_NEW_CAMERA = "insert into public.cameras (name, description, url) values (?,?,?)";
	private final String SQL_UPDATE_CAMERA = "update public.cameras set name = ?, description = ?, url = ? where cameraID = ?";
	private final String SQL_DELETE_CAMERA = "delete from public.cameras where cameraID = ?";
	private final String SQL_SELECT_ALL_CAMERAS = "select * from public.cameras order by cameraID";
	private final String SQL_SELECT_CAMERA_BY_ID = "select * from public.cameras where cameraID = ?";

	public void create(Camera camera) {
		if (camera == null)
			throw new IllegalArgumentException("camera can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection
					.prepareStatement(SQL_ADD_NEW_CAMERA);
			preparedStatement.setString(1, camera.getName());
			preparedStatement.setString(2, camera.getDescription());
			preparedStatement.setString(3, camera.getUrl());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - creating new camera:" + e.getMessage());
			throw new CameraNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	public void update(Camera camera) {
		if (camera == null || camera.getId() == null)
			throw new IllegalArgumentException("camera can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection
					.prepareStatement(SQL_UPDATE_CAMERA);
			preparedStatement.setString(1, camera.getName());
			preparedStatement.setString(2, camera.getDescription());
			preparedStatement.setString(3, camera.getUrl());
			preparedStatement.setLong(4, camera.getId());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - updating new camera:" + e.getMessage());
			throw new CameraNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	public void delete(Camera camera) {
		if (camera == null || camera.getId() == null)
			throw new IllegalArgumentException("cameraID can not be null");

		PrivilegeDao privilegeDao = new PrivilegeDao();
		privilegeDao.deletePrivilegesFor(camera);

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection
					.prepareStatement(SQL_DELETE_CAMERA);
			preparedStatement.setLong(1, camera.getId());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - deleting camera:" + e.getMessage());
			throw new CameraNotDeletedException();
		} finally {
			closeConnection(connection);
		}
	}

	public List<Camera> list() {
		List<Camera> cameras = new ArrayList<Camera>();

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection
					.prepareStatement(SQL_SELECT_ALL_CAMERAS);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				Camera camera = extractCameraFromResultSet(resultSet);
				cameras.add(camera);
			}

			return cameras;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - listing all cameras:" + e.getMessage());
			throw new CameraNotFoundException();
		} finally {
			closeConnection(connection);
		}
	}

	public Camera find(Long cameraID) {
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection
					.prepareStatement(SQL_SELECT_CAMERA_BY_ID);
			preparedStatement.setLong(1, cameraID);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				Camera camera = extractCameraFromResultSet(resultSet);
				return camera;
			} else {
				throw new CameraNotFoundException();
			}			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - listing all cameras:" + e.getMessage());
			throw new CameraNotFoundException();
		} finally {
			closeConnection(connection);
		}
	}

	private Camera extractCameraFromResultSet(ResultSet resultSet) {
		try {
			Camera camera = new Camera();

			camera.setId(resultSet.getLong("cameraID"));
			camera.setName(resultSet.getString("name"));
			camera.setDescription(resultSet.getString("description"));
			camera.setUrl(resultSet.getString("url"));

			return camera;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error - extraction camera from resultSet: " + e.getMessage());
			throw new CameraNotFoundException();
		}
	}

	private void closeConnection(Connection connection) {
		try {
			if (connection == null || !connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error - closing connection: " + e.getMessage());
		}
	}
}
