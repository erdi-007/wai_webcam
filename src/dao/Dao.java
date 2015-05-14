package dao;

import java.sql.Timestamp;
import java.util.List;

import model.User;
import model.Image;
import model.Camera;

public interface Dao {
	
	//speichern
	public void save(Image image);
	public void save(Camera camera);
	public void save(User user);
	public void save_privilege(Long userID, Long cameraID);	
	
	//löschen
	public void delete_camera(Long cameraID);
	public void delete_user(Long userID);	
	public void delete_privilege(Long userID, Long cameraID);	
	public void delete_privilege_camera(Long cameraID);	
	public void delete_privilege_user(Long userID);
	
	//einzelne Objekte
	public User get_user(Long userID);
	public Camera get_camera(Long cameraID);
	public boolean get_privilege(Long userID, Long cameraID);
	
	//allgemeine Listen
	public List<User> list_user();
	public List<Camera> list_camera();
	public List<Long> privileges_user(Long userID);	
	public List<Long> privileges_camera(Long cameraID);

	//ein Bild von einer camera
	public Image get_image(Long cameraID, Timestamp date);
	//alle Bilder einer camera
	public List<Image> get_images(Long cameraID);
	//alle Bilder ab einem Datum
	public List<Image> get_images(Long cameraID, Timestamp date);
	//alle Bilder in einem Zeitraum
	public List<Image> get_images(Long cameraID, Timestamp start_date, Timestamp end_date);
}
