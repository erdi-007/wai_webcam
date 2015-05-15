package servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import dao.DaoFactory;
import control.Controller;
import model.User;
import model.Camera;
import model.Image;

@WebServlet("/test")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
	Controller controller = new Controller();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		
		Integer int_id = 1;
		Long cameraID = int_id.longValue();
		
		save_test();
		get_test();
		
		save_image_test();
		get_image_test(cameraID);
		
		delete_test();
		
	}
	
	public void save_test() {
		
		Integer int_id = 1;
		Long cameraID = int_id.longValue();
		Long userID = cameraID;
		
		Camera camera = new Camera();
		camera.setName("Mannheim");
		camera.setUrl("https://www.mvv-energie.de/webcam_maritim/MA-Wasserturm.jpg");
		camera.setDescription("Webcam mit Blick auf den Wasserturm");	
		
		User user = new User();
		user.setAdmin(false);
		user.setName("Klaus Peter");
		user.setPassword("123");
		
		dao.save(camera);
		dao.save(user);
		
		dao.savePrivilege(userID, cameraID);		// 1 1	x
		dao.savePrivilege(userID, cameraID+1);		// 1 2	x	
		dao.savePrivilege(userID+1, cameraID);		// 2 1	x
		dao.savePrivilege(userID+2, cameraID);		// 3 1	x
		dao.savePrivilege(userID+3, cameraID+2);	// 4 3
		dao.savePrivilege(userID+3, cameraID+3);	// 4 4		
	}
	
	public void get_test() {
		
		Integer int_id = 1;
		Long cameraID = int_id.longValue();
		Long userID = cameraID;
		
		System.out.println(dao.getCamera(cameraID).getUrl());			// www.mannheim.de/uni		
		System.out.println(dao.getUser(userID).getAdmin());		// false
		System.out.println(dao.getPrivilege(userID+2, cameraID+2));	// true
		
		List<Long> privilege_user = dao.getPrivilegesUser(userID+2);
		List<Long> privilege_camera = dao.getPrivilegesCamera(cameraID+1);
		
		System.out.println("Alle Privilegien von User"+ (userID+2) + ":");		
		for(int i = 0; i<privilege_user.size(); i++) {
			System.out.println(dao.getCamera(privilege_user.get(i)).getId());					// 3 + 4
		}
		
		System.out.println("Alle Nutzer von Camera"+ (cameraID+1) + ":");		
		for(int i = 0; i<privilege_camera.size(); i++) {
			System.out.println(dao.getUser(privilege_camera.get(i)).getName());				// 4
		}
		
		List<User> userList = dao.getUserList();
		List<Camera> cameraList = dao.getCameraList();
		
		System.out.println("Alle User:");		
		for(int i = 0; i<userList.size(); i++) {
			System.out.println(userList.get(i).getName());
		}
		
		System.out.println("Alle Cameras:");		
		for(int i = 0; i<cameraList.size(); i++) {
			System.out.println(cameraList.get(i).getDescription());
		}
	}
	
	public void save_image_test() throws IOException {
		
		for(Integer i = 1; i <= 3; i++ ) {
			Camera camera = dao.getCamera(i.longValue());
			Image image = controller.save_image(camera);
			dao.save(image);
		}
	}
	
	public void get_image_test(Long id) {
		
		Long cameraID = id;
		
		Timestamp currentTimestamp = new Timestamp(new Date().getTime());
		Timestamp yesterdayTimestamp = new Timestamp(new Date(System.currentTimeMillis() - 24 * 60  * 60 * 1000L).getTime());
		Timestamp twenty_min_Timestamp = new Timestamp(new Date(System.currentTimeMillis() - 20  * 60 * 1000L).getTime());
		
		System.out.println("Ein Bild:");
		System.out.println(dao.getImage(cameraID, currentTimestamp).getDate());
		
		List<Image> all_List = dao.getImages(cameraID);
		List<Image> since_List = dao.getImages(cameraID, twenty_min_Timestamp);
		List<Image> timespan_List = dao.getImages(cameraID, yesterdayTimestamp, twenty_min_Timestamp);
		
		System.out.println("Alle Bilder:");		
		for(int i = 0; i<all_List.size(); i++) {
			System.out.println(all_List.get(i).getDate());
		}
		
		System.out.println("Alle Bilder ab " + twenty_min_Timestamp + ": ");	
		for(int i = 0; i<since_List.size(); i++) {
			System.out.println(since_List.get(i).getDate());
		}
		
		System.out.println("Alle Bilder zwischen " + yesterdayTimestamp + " und " + twenty_min_Timestamp + ": ");	
		for(int i = 0; i<timespan_List.size(); i++) {
			System.out.println(timespan_List.get(i).getDate());
		}
	}
	
	public void delete_test() {
		
		Integer int_id = 1;
		Long cameraID = int_id.longValue();
		Long userID = cameraID;
		
		dao.deletePrivilege(userID, cameraID);
		dao.deletePrivilegeCamera(cameraID);
		dao.deletePrivilegeUser(userID);
		
		List<User> userList = dao.getUserList();
		List<Camera> cameraList = dao.getCameraList();
		
		dao.deleteCamera(cameraList.get(cameraList.size()-2).getId());
		dao.deleteUser(userList.get(userList.size()-2).getId());
	}
}
