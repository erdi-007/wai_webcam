package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utils.JndiFactory;
import exception.UserNotDeletedException;
import exception.UserNotFoundException;
import exception.UserNotSavedException;
import model.User;

public class UserDao {

	final String DATABASE = "jdbc/libraryDB";
	final JndiFactory jndi = JndiFactory.getInstance();

	private final String SQL_ADD_NEW_USER = "insert into public.user (is_admin, name, password) values (?,?,?)";
	private final String SQL_UPDATE_USER = "update public.user set is_admin = ?, name = ?, password = ? where userID = ?";
	private final String SQL_DELETE_USER = "delete from public.user where userID = ?";
	private final String SQL_SELECT_ALL_USERS = "select * from public.user where name != 'admin' order by userID";
	private final String SQL_SELECT_USER_BY_NAME = "select * from public.user where name = ?";
	private final String SQL_SELECT_USER_BY_ID = "select * from public.user where userID = ?";

	public void create(User user) {
		if (user == null)
			throw new IllegalArgumentException("user can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_NEW_USER);
			preparedStatement.setBoolean(1, user.isAdmin());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getPassword());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - creating new user: " + e.getMessage());
			throw new UserNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	public void update(User user) {
		if (user == null || user.getClass() == null)
			throw new IllegalArgumentException("user can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);
			preparedStatement.setBoolean(1, user.isAdmin());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setLong(4, user.getId());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - updating user: " + e.getMessage());
			throw new UserNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	public void delete(User user) {
		if (user == null || user.getId() == null)
			throw new IllegalArgumentException("userID can not be null");

		PrivilegeDao privilegeDao = new PrivilegeDao();
		privilegeDao.deletePrivilegesFor(user);

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
			preparedStatement.setLong(1, user.getId());

			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - deleting user: " + e.getMessage());
			throw new UserNotDeletedException();
		} finally {
			closeConnection(connection);
		}
	}

	public List<User> list() {
		List<User> users = new ArrayList<User>();

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USERS);
			
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				User user = extractUserFromResultSet(resultSet);
				users.add(user);
			}

			return users;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - listing all users: " + e.getMessage());
			throw new UserNotFoundException();
		} finally {
			closeConnection(connection);
		}
	}

	public User find(String userName) {
		if (userName == null)
			throw new IllegalArgumentException("name can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_NAME);
			preparedStatement.setString(1, userName);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				User user = extractUserFromResultSet(resultSet);
				return user;
			} else {
				throw new UserNotFoundException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - finding user by name: " + e.getMessage());
			throw new UserNotFoundException();
		} finally {
			closeConnection(connection);
		}
	}

	public User find(Long userID) {
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_ID);
			preparedStatement.setLong(1, userID);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				User user = extractUserFromResultSet(resultSet);
				return user;
			} else {
				throw new UserNotFoundException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - finding user by id: " + e.getMessage());
			throw new UserNotFoundException();
		} finally {
			closeConnection(connection);
		}
	}

	private User extractUserFromResultSet(ResultSet resultSet) {
		try {
			User user = new User();

			user.setId(resultSet.getLong("userID"));
			user.setAdmin(resultSet.getBoolean("is_admin"));
			user.setName(resultSet.getString("name"));
			user.setPassword(resultSet.getString("password"));

			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error - extraction user from resultSet: " + e.getMessage());
			throw new UserNotFoundException();
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
