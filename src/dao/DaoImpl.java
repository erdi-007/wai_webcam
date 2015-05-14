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
import exception.UserNotSavedException;
import exception.UserNotFoundException;
import exception.UserNotDeletedException;
import exception.PrivilegeNotSavedException;
import exception.PrivilegeNotDeletedException;
import exception.PrivilegeNotFoundException;
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
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(image.get_Date().getTime());			
			connection = jndi.getConnection("jdbc/libraryDB");	
			PreparedStatement pstmt = connection.prepareStatement("insert into public.images (cameraID, date) values (?,?)");
			pstmt.setLong(1, image.get_cameraID());
			pstmt.setTimestamp(2, sqlTimestamp);
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
			if (camera.get_id() == null) {
				PreparedStatement pstmt = connection.prepareStatement("insert into public.cameras (name, description, url, path) values (?,?,?,?)");
				pstmt.setString(1, camera.get_name());
				pstmt.setString(2, camera.get_description());
				pstmt.setString(3, camera.get_url());
				pstmt.setString(4, camera.get_path());
				pstmt.executeUpdate();
			} else {
				PreparedStatement pstmt = connection.prepareStatement("update public.cameras set name = ?, description = ?, url = ?, path = ? where cameraID = ?");
				pstmt.setString(1, camera.get_name());
				pstmt.setString(2, camera.get_description());
				pstmt.setString(3, camera.get_url());
				pstmt.setString(4, camera.get_path());
				pstmt.setLong(5, camera.get_id());
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
			if (user.get_id() == null) {
				PreparedStatement pstmt = connection.prepareStatement("insert into public.user (is_admin, name, password) values (?,?,?)");
				pstmt.setBoolean(1, user.get_admin_state());
				pstmt.setString(2, user.get_name());
				pstmt.setString(3, user.get_password());
				pstmt.executeUpdate();
			} else {
				PreparedStatement pstmt = connection.prepareStatement("update public.user set is_admin = ?, name = ?, password = ? where userID = ?");
				pstmt.setBoolean(1, user.get_admin_state());
				pstmt.setString(2, user.get_name());
				pstmt.setString(3, user.get_password());
				pstmt.setLong(4, user.get_id());
				pstmt.executeUpdate();
			}			
		} catch (Exception e) {
			throw new UserNotSavedException();
		} finally {
			closeConnection(connection);
		}
	}

	@Override
	public void save_privilege(Long userID, Long cameraID) {
		
		if (userID == null || cameraID == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");
		
		Connection connection = null;		
		try {
			if(get_privilege(userID, cameraID) == false)
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
	public void delete_camera(Long cameraID) {

		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
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
	public void delete_user(Long userID) {
		
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
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
	public void delete_privilege(Long userID, Long cameraID) {
		
		if (userID == null || cameraID == null)
			throw new IllegalArgumentException("userID or cameraID can not be null");
		
		Connection connection = null;		
		try {
			if(get_privilege(userID, cameraID) == true)	{
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
	public void delete_privilege_camera(Long cameraID) {
		
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		Connection connection = null;		
		try {
			if(get_camera(cameraID) != null)	{
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
	public void delete_privilege_user(Long userID) {
		
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		Connection connection = null;		
		try {
			if(get_camera(userID) != null)	{
				connection = jndi.getConnection("jdbc/libraryDB");
				PreparedStatement pstmt = connection.prepareStatement("delete from public.privileges where userID = ?");
				pstmt.setLong(1, userID);
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
	public User get_user(Long userID) {
		
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
				user.set_id(rs.getLong("userID"));
				user.set_admin_state(rs.getBoolean("is_admin"));
				user.set_name(rs.getString("name"));
				user.set_password(rs.getString("password"));
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
	public Camera get_camera(Long cameraID) {
		
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
				camera.set_id(rs.getLong("cameraID"));
				camera.set_name(rs.getString("name"));
				camera.set_description(rs.getString("description"));
				camera.set_url(rs.getString("url"));
				camera.set_path(rs.getString("path"));
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
	public boolean get_privilege(Long userID, Long cameraID) {
		
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
	public List<User> list_user() {
			
		List<User> userList = new ArrayList<User>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.user");				
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				User user = new User();
				user.set_id(rs.getLong("userID"));
				user.set_admin_state(rs.getBoolean("is_admin"));
				user.set_name(rs.getString("name"));
				user.set_password(rs.getString("password"));
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
	public List<Camera> list_camera() {

		List<Camera> cameraList = new ArrayList<Camera>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.cameras");				
			ResultSet rs = pstmt.executeQuery();
							
			while (rs.next()) {
				Camera camera = new Camera();
				camera.set_id(rs.getLong("cameraID"));
				camera.set_name(rs.getString("name"));
				camera.set_description(rs.getString("description"));
				camera.set_url(rs.getString("url"));
				camera.set_path(rs.getString("path"));
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
	public List<Long> privileges_user(Long userID) {
		
		if (userID == null)
			throw new IllegalArgumentException("userID can not be null");
		
		List<Long> cameraIDList = new ArrayList<Long>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.privileges where userID = ?");	
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
	public List<Long> privileges_camera(Long cameraID) {
		
		if (cameraID == null)
			throw new IllegalArgumentException("cameraID can not be null");
		
		List<Long> userIDList = new ArrayList<Long>();
		
		Connection connection = null;		
		try {
			connection = jndi.getConnection("jdbc/libraryDB");			
			
			PreparedStatement pstmt = connection.prepareStatement("select * from public.privileges where cameraID = ?");
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
	public Image get_image(Long cameraID, Timestamp date) {

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
				Image image = new Image();
				image.set_cameraID(rs.getLong("cameraID"));
				image.set_Date(rs.getTimestamp("date"));
				
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
	public List<Image> get_images(Long cameraID) {
		
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
				Image image = new Image();
				image.set_cameraID(rs.getLong("cameraID"));
				image.set_Date(rs.getTimestamp("date"));
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
	public List<Image> get_images(Long cameraID, Timestamp date) {
		
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
				Image image = new Image();
				image.set_cameraID(rs.getLong("cameraID"));
				image.set_Date(rs.getTimestamp("date"));
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
	public List<Image> get_images(Long cameraID, Timestamp start_date, Timestamp end_date) {
		
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
				Image image = new Image();
				image.set_cameraID(rs.getLong("cameraID"));
				image.set_Date(rs.getTimestamp("date"));
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
