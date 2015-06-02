package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import utils.JndiFactory;
import exception.ImageNotSavedException;
import model.Image;

public class ImageDao {
	
	final String DATABASE = "jdbc/libraryDB";
	final JndiFactory jndi = JndiFactory.getInstance();
	
	private final String SQL_ADD_NEW_IMAGE = "insert into public.images (cameraID, date) values (?,?)";
	
	public void save(Image image)
	{
		if (image == null)
			throw new IllegalArgumentException("image can not be null");
		
		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_NEW_IMAGE);
			preparedStatement.setLong(1, image.getCameraID());
			preparedStatement.setTimestamp(2, image.getDate());
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - saving new image: " + e.getMessage());
			throw new ImageNotSavedException();
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
