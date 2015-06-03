package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import utils.JndiFactory;
import exception.ImageNotFoundException;
import exception.ImageNotSavedException;
import model.Camera;
import model.Image;

public class ImageDao {

	final String DATABASE = "jdbc/libraryDB";
	final JndiFactory jndi = JndiFactory.getInstance();

	private final String SQL_ADD_NEW_IMAGE = "insert into public.images (cameraID, date) values (?,?)";
	private final String SQL_SELECT_IMAGE_FOR_DATE = "select * from public.images where cameraID = ? and date <= ? order by date desc limit 1";
	private final String SQL_SELECT_IMAGES_FOR_PERIOD = "select * from public.images where cameraID = ? and date >= ? and date <= ?";

	public void save(Image image) {
		if (image == null)
			throw new IllegalArgumentException("image can not be null");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection
					.prepareStatement(SQL_ADD_NEW_IMAGE);
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

	public Image find(Camera camera, Timestamp date) {
		if (camera == null || camera.getId() == null || date == null)
			throw new IllegalArgumentException("invalid arguments");

		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_IMAGE_FOR_DATE);
			preparedStatement.setLong(1, camera.getId());
			preparedStatement.setTimestamp(2, date);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Image image = new Image();
				
				image.setCameraID(resultSet.getLong("cameraID"));
				image.setDate(resultSet.getTimestamp("date"));
				//image.setName(resultSet.getString("name"));
				
				return image;
			} else {
				throw new ImageNotFoundException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - finding image from cameraID " + camera.getId() + "for " + date + ": " + e.getMessage());
			throw new ImageNotFoundException();
		} finally {
			closeConnection(connection);
		}
	}
	
	public List<Image> find(Camera camera, Timestamp startDate, Timestamp endDate) {
		if (camera == null || camera.getId() == null || startDate == null || endDate == null)
			throw new IllegalArgumentException("invalid arguments");
		
		List<Image> images = new ArrayList<Image>();
		
		Connection connection = null;
		try {
			connection = jndi.getConnection(DATABASE);
			PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_IMAGES_FOR_PERIOD);
			preparedStatement.setLong(1, camera.getId());
			preparedStatement.setTimestamp(2, startDate);
			preparedStatement.setTimestamp(3, endDate);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				Image image = new Image();
				
				image.setCameraID(resultSet.getLong("cameraID"));
				image.setDate(resultSet.getTimestamp("date"));
				//image.setName(resultSet.getString("name"));
				
				images.add(image);
			}
			
			return images;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error - finding images from cameraID " + camera.getId() + "for " + startDate + " - " + endDate + ": " + e.getMessage());
			throw new ImageNotFoundException();
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
