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
import model.User;
import model.Camera;
import model.Image;

@WebServlet("/test")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		
		save_test();
		delete_test();
		get_test();
		image_test();
	}
	
	public void save_test() {

		Integer int_id = 2;
		Long cameraID = int_id.longValue();
		Long userID = cameraID;
		
		Camera camera = new Camera();		
		camera.set_name("Mannheim");
		camera.set_url("www.mannheim.de/uni");
		camera.set_description("Kamera an der Universität Mannheim");
		camera.set_path("c:/Mannheim/test");	
		
		User user = new User();
		user.set_admin_state(false);
		user.set_name("Klaus Peter");
		user.set_password("123");
		
		Image image = new Image();
		image.set_cameraID(cameraID);
		image.set_Date(new Timestamp(new Date().getTime()));
			
		dao.save(camera);
		dao.save(image);
		dao.save(user);	
		
		dao.save_privilege(userID, cameraID);		// 2 2 x
		dao.save_privilege(userID, cameraID+1);		// 2 3 x	
		dao.save_privilege(userID+1, cameraID);		// 3 2 x
		dao.save_privilege(userID+2, cameraID);		// 4 2 x
		dao.save_privilege(userID+2, cameraID+1);	// 4 3
		dao.save_privilege(userID+2, cameraID+2);	// 4 4	
	}
	
	public void delete_test() {
				
		Integer int_id = 1;
		Long cameraID = int_id.longValue();
		Long userID = cameraID;
		
		dao.delete_camera(cameraID);
		dao.delete_user(userID);
		dao.delete_privilege(userID+1, cameraID+1);	// 2 2
		dao.delete_privilege_camera(cameraID+1);	// 2 2 + 3 2 + 4 2 
		dao.delete_privilege_user(userID+1);		// 2 2 + 2 3	
	}
	
	public void get_test() {
		
		Integer int_id = 2;
		Long cameraID = int_id.longValue();
		Long userID = cameraID;
		
		System.out.println(dao.get_camera(cameraID).get_url());			// www.mannheim.de/uni		
		System.out.println(dao.get_user(userID).get_admin_state());		// false
		System.out.println(dao.get_privilege(userID+2, cameraID+2));	// true
		
		List<Long> privilege_user = dao.privileges_user(userID+2);
		List<Long> privilege_camera = dao.privileges_camera(cameraID+1);
		
		System.out.println("Alle Privilegien von User"+ (userID+2) + ":");		
		for(int i = 0; i<privilege_user.size(); i++) {
			System.out.println(privilege_user.get(i));					// 3 + 4
		}
		
		System.out.println("Alle Nutzer von Camera"+ (cameraID+1) + ":");		
		for(int i = 0; i<privilege_camera.size(); i++) {
			System.out.println(privilege_camera.get(i));				// 4
		}
		
		List<User> userList = dao.list_user();
		List<Camera> cameraList = dao.list_camera();
		
		System.out.println("Alle User:");		
		for(int i = 0; i<userList.size(); i++) {
			System.out.println(userList.get(i).get_id());
		}
		
		System.out.println("Alle Cameras:");		
		for(int i = 0; i<cameraList.size(); i++) {
			System.out.println(cameraList.get(i).get_id());
		}
	}
	
	public void image_test() {
		
		Integer int_id = 2;
		Long cameraID = int_id.longValue();
		
		Timestamp currentTimestamp = new Timestamp(new Date().getTime());
		Timestamp yesterdayTimestamp = new Timestamp(new Date(System.currentTimeMillis() - 24 * 60  * 60 * 1000L).getTime());
		Timestamp twenty_min_Timestamp = new Timestamp(new Date(System.currentTimeMillis() - 20  * 60 * 1000L).getTime());
		
		System.out.println("Ein Bild:");
		System.out.println(dao.get_image(cameraID, currentTimestamp).get_Date());
		
		List<Image> all_List = dao.get_images(cameraID);
		List<Image> since_List = dao.get_images(cameraID, twenty_min_Timestamp);
		List<Image> timespan_List = dao.get_images(cameraID, yesterdayTimestamp, twenty_min_Timestamp);
		
		System.out.println("Alle Bilder:");		
		for(int i = 0; i<all_List.size(); i++) {
			System.out.println(all_List.get(i).get_Date());
		}
		
		System.out.println("Alle Bilder ab " + twenty_min_Timestamp + ": ");	
		for(int i = 0; i<since_List.size(); i++) {
			System.out.println(since_List.get(i).get_Date());
		}
		
		System.out.println("Alle Bilder zwischen " + yesterdayTimestamp + " und " + twenty_min_Timestamp + ": ");	
		for(int i = 0; i<timespan_List.size(); i++) {
			System.out.println(timespan_List.get(i).get_Date());
		}
	}
}
