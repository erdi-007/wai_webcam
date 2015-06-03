package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Camera;
import model.User;
import utils.JndiFactory;
import exception.PrivilegeNotDeletedException;
import exception.PrivilegeNotFoundException;
import exception.PrivilegeNotSavedException;

public class PrivilegeDao {

	final String DATABASE = "jdbc/libraryDB";
	final JndiFactory jndi = JndiFactory.getInstance();

	private final String SQL_ADD_NEW_PRIVILEGE = "insert into public.privileges (userID, cameraID) values (?,?)";
	private final String SQL_DELETE_PRIVILEGE = "delete from public.privileges where userID = ? and cameraID = ?";
	private final String SQL_DELETE_PRIVILEGES_FROM_USER = "delete from public.privileges where userID = ?";
	private final String SQL_DELETE_PRIVILEGES_FROM_CAMERA = "delete from public.privileges where cameraID = ?";
	private final String SQL_SELECT_PRIVILEGE = "select * from public.privileges where userID = ? and cameraID = ?";
	private final String SQL_SELECT_PRIVILEGES_FOR_USER = "select * from public.privileges where userID = ? order by cameraID";
	private final String SQL_SELECT_PRIVILEGES_FOR_CAMERA = "select * from public.privileges where cameraID = ? and userID != 1 order by userID";

	public void create(User user, Camera camera) {
		if (user == null || camera == null || user.getId() == null || camera.getId() == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");

		if(isUserAuthorizedForCamera(user, camera))
			return;
		
		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_NEW_PRIVILEGE);
			preparedStatement.setLong(1, user.getId());
			preparedStatement.setLong(2, camera.getId());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - creating new privilege: " + e.getMessage());
			throw new PrivilegeNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	public void delete(User user, Camera camera) {
		if (user == null || camera == null || user.getId() == null || camera.getId() == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_PRIVILEGE);
			preparedStatement.setLong(1, user.getId());
			preparedStatement.setLong(2, camera.getId());
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - deleting privilege: " + e.getMessage());
			throw new PrivilegeNotDeletedException();
		} finally {
			closeConnection(connection);
		}
	}

	public void deletePrivilegesFor(User user) {
		if (user == null || user.getId() == null)
			throw new IllegalArgumentException("userID can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_PRIVILEGES_FROM_USER);
			preparedStatement.setLong(1, user.getId());
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - deleting privileges from userid " + user.getId() + ": " + e.getMessage());
			throw new PrivilegeNotDeletedException();
		} finally {
			closeConnection(connection);
		}
	}

	public void deletePrivilegesFor(Camera camera) {
		if (camera == null || camera.getId() ==  null)
			throw new IllegalArgumentException("cameraID can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_PRIVILEGES_FROM_CAMERA);
			preparedStatement.setLong(1, camera.getId());
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - deleting privileges from cameraid " + camera.getId() + ": " + e.getMessage());
			throw new PrivilegeNotDeletedException();
		} finally {
			closeConnection(connection);
		}
	}

	public boolean isUserAuthorizedForCamera(User user, Camera camera) {
		if (user == null || camera == null || user.getId() == null || camera.getId() == null)
			throw new IllegalArgumentException(
					"userID or cameraID can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PRIVILEGE);
			preparedStatement.setLong(1, user.getId());
			preparedStatement.setLong(2, camera.getId());

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - deleting privileges from cameraid " + camera.getId() + ": " + e.getMessage());
			throw new PrivilegeNotFoundException();
		} finally {
			closeConnection(connection);
		}
	}

	public List<Long> listPrivileges(User user) {
		if (user == null || user.getId() == null)
			throw new IllegalArgumentException("user can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PRIVILEGES_FOR_USER);
			preparedStatement.setLong(1, user.getId());
			
			ResultSet resultSet = preparedStatement.executeQuery();

			List<Long> cameraIDs = new ArrayList<Long>();
			while (resultSet.next()) {
				cameraIDs.add(resultSet.getLong("cameraID"));
			}

			return cameraIDs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - listing privileges for userid " + user.getId() + ": " + e.getMessage());
			throw new PrivilegeNotFoundException();
		} finally {
			closeConnection(connection);
		}
	}

	public List<Long> listPrivileges(Camera camera) {
		if (camera == null || camera.getId() == null)
			throw new IllegalArgumentException("camera can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PRIVILEGES_FOR_CAMERA);
			preparedStatement.setLong(1, camera.getId());
			
			ResultSet resultSet = preparedStatement.executeQuery();

			List<Long> userIDs = new ArrayList<Long>();
			while (resultSet.next()) {
				userIDs.add(resultSet.getLong("userID"));
			}

			return userIDs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - listing privileges for cameraid " + camera.getId() + ": " + e.getMessage());
			throw new PrivilegeNotFoundException();
		} finally {
			closeConnection(connection);
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
