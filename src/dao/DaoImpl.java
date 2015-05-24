package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

import jndi.JndiFactory;
import exception.CameraNotSavedException;
import exception.CameraNotFoundException;
import exception.CameraNotDeletedException;
import exception.ImageNotSavedException;
import exception.ImageNotFoundException;
import exception.ImagesNotDeletedException;
import exception.UserNotSavedException;
import exception.UserNotFoundException;
import exception.UserNotDeletedException;
import exception.PrivilegeNotSavedException;
import exception.PrivilegeNotDeletedException;
import exception.PrivilegeNotFoundException;
import exception.LoginFailedException;
import model.User;
import model.Image;
import model.Camera;

public class DaoImpl implements Dao{
	
	final JndiFactory jndi = JndiFactory.getInstance();

	@Override
	public void save(Image image) {
		
		if (image == null)
			throw new IllegalArgumentException("image can not be null");
		
		Connection connection = null;		
		try {			
			connection = jndi.getConnection("jdbc/libraryDB");	
			PreparedStatement pstmt = connection.prepareStatement("insert into public.images (cameraID, date) values (?,?)");
			pstmt.setLong(1, image.getCameraID());
			pstmt.setTimestamp(2, image.getDate());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new ImageNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	@Override
	public void save(Camera camera) {
		
		if (camera == null)
			throw new IllegalArgumentException("camera can not be null");
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			if (camera.getId() == null) {
				PreparedStatement pstmt = connection.prepareStatement("insert into public.cameras (name, description, url) values (?,?,?)");
				pstmt.setString(1, camera.getName());
				pstmt.setString(2, camera.getDescription());
				pstmt.setString(3, camera.getUrl());
				pstmt.executeUpdate();
			} else {
				PreparedStatement pstmt = connection.prepareStatement("update public.cameras set name = ?, description = ?, url = ? where cameraID = ?");
				pstmt.setString(1, camera.getName());
				pstmt.setString(2, camera.getDescription());
				pstmt.setString(3, camera.getUrl());
				pstmt.setLong(4, camera.getId());
				pstmt.executeUpdate();
			}			
		} catch (Exception e) {
			throw new CameraNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	@Override
	public void save(User user) {
		
		if (user == null)
			throw new IllegalArgumentException("user can not be null");
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");	
					
			if (user.getId() == null) {
				PreparedStatement pstmt = connection.prepareStatement("insert into public.user (is_admin, name, password) values (?,?,?)");
				pstmt.setBoolean(1, user.isAdmin());
				pstmt.setString(2, user.getName());
				pstmt.setString(3, user.getPassword());
				pstmt.executeUpdate();
			} else {
				PreparedStatement pstmt = connection.prepareStatement("update public.user set is_admin = ?, name = ?, password = ? where userID = ?");
				pstmt.setBoolean(1, user.isAdmin());
				pstmt.setString(2, user.getName());
				pstmt.setString(3, user.getPassword());
				pstmt.setLong(4, user.getId());
				pstmt.executeUpdate();
			}	
		} catch (Exception e) {
			throw new UserNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	@Override
	public void savePrivilege(Long userID, Long cameraID) {
		
		if (userID == null || cameraID == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");
		
		Connection connection = null;		
		try {
			if(getPrivilege(userID, cameraID) == false)
			{
				connection = jndi.getConnection("jdbc/libraryDB");
				PreparedStatement pstmt = connection.prepareStatement("insert into public.privileges (userID, cameraID) values (?,?)");
				pstmt.setLong(1, userID);
				pstmt.setLong(2, cameraID);
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			throw new PrivilegeNotSavedException();
		} finally {
			closeConnection(connection);
		}		
	}
	
	@Override
	public void deleteCamera(Long cameraID) {

		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		deletePrivilegeCamera(cameraID);
		deleteImages(cameraID);
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");
			PreparedStatement pstmt = connection.prepareStatement("delete from public.cameras where cameraID = ?");
			pstmt.setLong(1, cameraID);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new CameraNotDeletedException();
		} finally {
			closeConnection(connection);
		}		
	}
	
	@Override
	public void deleteImages(Long cameraID) {

		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
				
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");
			PreparedStatement pstmt = connection.prepareStatement("delete from public.images where cameraID = ?");
			pstmt.setLong(1, cameraID);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new ImagesNotDeletedException();
		} finally {
			closeConnection(connection);
		}		
	}

	@Override
	public void deleteUser(Long userID) {
		
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		deletePrivilegeUser(userID);
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");
			PreparedStatement pstmt = connection.prepareStatement("delete from public.user where userID = ?");
			pstmt.setLong(1, userID);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new UserNotDeletedException();
		} finally {
			closeConnection(connection);
		}	
	}

	@Override
	public void deletePrivilege(Long userID, Long cameraID) {
		
		if (userID == null || cameraID == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");
		
		Connection connection = null;		
		try {
			if(getPrivilege(userID, cameraID) == true)	{
				connection = jndi.getConnection("jdbc/libraryDB");
				PreparedStatement pstmt = connection.prepareStatement("delete from public.privileges where userID = ? and cameraID = ?");
				pstmt.setLong(1, userID);
				pstmt.setLong(2, cameraID);
				pstmt.executeUpdate();
			} else {
				throw new PrivilegeNotFoundException();
			}
		} catch (Exception e) {
			throw new PrivilegeNotDeletedException();
		} finally {
			closeConnection(connection);
		}		
	}

	@Override
	public void deletePrivilegeCamera(Long cameraID) {
		
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		Connection connection = null;		
		try {
			if(getCamera(cameraID) != null)	{
				connection = jndi.getConnection("jdbc/libraryDB");
				PreparedStatement pstmt = connection.prepareStatement("delete from public.privileges where cameraID = ?");
				pstmt.setLong(1, cameraID);
				pstmt.executeUpdate();
			} else {
				throw new CameraNotFoundException();
			}
		} catch (Exception e) {
			throw new PrivilegeNotDeletedException();
		} finally {
			closeConnection(connection);
		}	
	}

	@Override
	public void deletePrivilegeUser(Long userID) {
		
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		Connection connection = null;		
		try {
			if(getUser(userID) != null)	{
				connection = jndi.getConnection("jdbc/libraryDB");
				PreparedStatement pstmt = connection.prepareStatement("delete from public.privileges where userID = ?");
				pstmt.setLong(1, userID);
				pstmt.executeUpdate();
			} else {
				throw new UserNotFoundException();
			}
		} catch (Exception e) {
			throw new PrivilegeNotDeletedException();
		} finally {
			closeConnection(connection);
		}	
	}
	
	@Override
	public User login(User user) {
		if (user == null)
			throw new IllegalArgumentException("user can not be null");
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.user where name = ? and password = ?");
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			
			boolean valid = rs.next();
			
			if(!valid) {
				user.setValid(false);
			} else if(valid) {
				user.setAdmin(rs.getBoolean("is_admin"));
				user.setValid(true);
			}
		
		} catch (Exception e) {
			throw new LoginFailedException();
		} finally {	
			closeConnection(connection);
		}
		
		return user;
	}

	@Override
	public User getUser(Long userID) {
		
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.user where userID = ?");
			pstmt.setLong(1, userID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("userID"));
				user.setAdmin(rs.getBoolean("is_admin"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				return user;
			} else {
				throw new UserNotFoundException();
			}			
		} catch (Exception e) {
			throw new UserNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}
	
	@Override
	public User getUser(String name) {
		
		if (name == null)
			throw new IllegalArgumentException("name can not be null");
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.user where name = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("userID"));
				user.setAdmin(rs.getBoolean("is_admin"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				return user;
			} else {
				throw new UserNotFoundException();
			}			
		} catch (Exception e) {
			throw new UserNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public Camera getCamera(Long cameraID) {
		
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.cameras where cameraID = ?");
			pstmt.setLong(1, cameraID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Camera camera = new Camera();
				camera.setId(rs.getLong("cameraID"));
				camera.setName(rs.getString("name"));
				camera.setDescription(rs.getString("description"));
				camera.setUrl(rs.getString("url"));
				return camera;
			} else {
				throw new CameraNotFoundException();
			}			
		} catch (Exception e) {
			throw new CameraNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public boolean getPrivilege(Long userID, Long cameraID) {
		
		if (userID == null || cameraID == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.privileges where userID = ? and cameraID = ?");
			pstmt.setLong(1, userID);
			pstmt.setLong(2, cameraID);
			ResultSet rs = pstmt.executeQuery();							
			if (rs.next()) {
				return true;
			} else {
				return false;
			}			
		} catch (Exception e) {
			throw new PrivilegeNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public List<User> getUserList() {
			
		List<User> userList = new ArrayList<User>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.user where name != 'admin' order by userID");				
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("userID"));
				user.setAdmin(rs.getBoolean("is_admin"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				userList.add(user);
			}			
			
			return userList;
		} catch (Exception e) {
			throw new UserNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public List<Camera> getCameraList() {

		List<Camera> cameraList = new ArrayList<Camera>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.cameras order by cameraID");				
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				Camera camera = new Camera();
				camera.setId(rs.getLong("cameraID"));
				camera.setName(rs.getString("name"));
				camera.setDescription(rs.getString("description"));
				camera.setUrl(rs.getString("url"));
				cameraList.add(camera);
			}			
			
			return cameraList;
		} catch (Exception e) {
			throw new CameraNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public List<Long> getPrivilegesUser(Long userID) {
		
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		List<Long> cameraIDList = new ArrayList<Long>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.privileges where userID = ? order by cameraID");	
			pstmt.setLong(1, userID);			
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				cameraIDList.add(rs.getLong("cameraID"));
			}			
			
			return cameraIDList;
		} catch (Exception e) {
			throw new UserNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public List<Long> getPrivilegesCamera(Long cameraID) {
		
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		List<Long> userIDList = new ArrayList<Long>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.privileges where cameraID = ? and userID != 1 order by userID");
			pstmt.setLong(1, cameraID);				
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				userIDList.add(rs.getLong("userID"));
			}			
		
			return userIDList;
		} catch (Exception e) {
			throw new CameraNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public Image getImage(Long cameraID, Timestamp date) {

		if (cameraID == null || date == null)
			throw new IllegalArgumentException("cameraID or date can not be null");
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
						
			PreparedStatement pstmt = connection.prepareStatement("select * from public.images where cameraID = ? and date <= ? order by date desc limit 1");
			pstmt.setLong(1, cameraID);
			pstmt.setTimestamp(2, date);			
			ResultSet rs = pstmt.executeQuery();
							
			if (rs.next()) {
				Long cameraId = rs.getLong("cameraID");
				Timestamp timestamp = rs.getTimestamp("date");
				Image image = new Image();
				image.setCameraID(cameraId);
				image.setDate(timestamp);
				image.setPath(controller.getPath(cameraId, timestamp));
				return image;
			} else {
				throw new ImageNotFoundException();
			}
		} catch (Exception e) {
			throw new ImageNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public List<Image> getImages(Long cameraID) {
		
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		List<Image> imageList = new ArrayList<Image>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.images where cameraID = ?");
			pstmt.setLong(1, cameraID);
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				Long cameraId = rs.getLong("cameraID");
				Timestamp timestamp = rs.getTimestamp("date");
				Image image = new Image();
				image.setCameraID(cameraId);
				image.setDate(timestamp);
				image.setPath(controller.getPath(cameraId, timestamp));
				imageList.add(image);
			}			
			
			return imageList;
		} catch (Exception e) {
			throw new CameraNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public List<Image> getImages(Long cameraID, Timestamp date) {
		
		if (cameraID == null || date == null)
			throw new IllegalArgumentException("cameraID or date can not be null");
		
		List<Image> imageList = new ArrayList<Image>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.images where cameraID = ? and date >= ?");
			pstmt.setLong(1, cameraID);
			pstmt.setTimestamp(2, date);
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				Long cameraId = rs.getLong("cameraID");
				Timestamp timestamp = rs.getTimestamp("date");
				Image image = new Image();
				image.setCameraID(cameraId);
				image.setDate(timestamp);
				image.setPath(controller.getPath(cameraId, timestamp));
				imageList.add(image);
			}			
			
			return imageList;
		} catch (Exception e) {
			throw new CameraNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}

	@Override
	public List<Image> getImages(Long cameraID, Timestamp start_date, Timestamp end_date) {
		
		if (cameraID == null || start_date == null || end_date == null)
			throw new IllegalArgumentException("cameraID or date can not be null");
		
		List<Image> imageList = new ArrayList<Image>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");						
			PreparedStatement pstmt = connection.prepareStatement("select * from public.images where cameraID = ? and date >= ? and date <= ?");
			pstmt.setLong(1, cameraID);
			pstmt.setTimestamp(2, start_date);
			pstmt.setTimestamp(3, end_date);
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				Long cameraId = rs.getLong("cameraID");
				Timestamp timestamp = rs.getTimestamp("date");
				Image image = new Image();
				image.setCameraID(cameraId);
				image.setDate(timestamp);
				image.setPath(controller.getPath(cameraId, timestamp));
				imageList.add(image);
			}			
			
			return imageList;
		} catch (Exception e) {
			throw new CameraNotFoundException();
		} finally {	
			closeConnection(connection);
		}
	}
	
	private void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				// nothing to do
				e.printStackTrace();
			}				
		}
	}

}
