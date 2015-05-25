package dao;

import java.sql.Timestamp;
import java.util.List;

import control.Controller;
import model.User;
import model.Image;
import model.Camera;

public interface Dao {

	Controller controller = new Controller();
	
	//speichern
	public void save(Image image);
	public void save(Camera camera);
	public void save(User user);
	public void savePrivilege(Long userID, Long cameraID);	
	
	//löschen
	public void deleteCamera(Long cameraID);
	public void deleteUser(Long userID);	
	public void deleteImages(Long cameraID);
	public void deletePrivilege(Long userID, Long cameraID);	
	public void deletePrivilegeCamera(Long cameraID);	
	public void deletePrivilegeUser(Long userID);
	
	//einzelne Objekte
	public User getUser(Long userID);
	public User getUser(String name);
	public Camera getCamera(Long cameraID);
	public boolean getPrivilege(Long userID, Long cameraID);
	
	//allgemeine Listen
	public List<User> getUserList();
	public List<Camera> getCameraList();
	public List<Long> getPrivilegesUser(Long userID);	
	public List<Long> getPrivilegesCamera(Long cameraID);

	//ein Bild von einer camera
	public Image getImage(Long cameraID, Timestamp date);
	//alle Bilder einer camera
	public List<Image> getImages(Long cameraID);
	//alle Bilder ab einem Datum
	public List<Image> getImages(Long cameraID, Timestamp date);
	//alle Bilder in einem Zeitraum
	public List<Image> getImages(Long cameraID, Timestamp start_date, Timestamp end_date);
}
