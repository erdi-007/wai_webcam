package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CameraDao;
import dao.PrivilegeDao;
import dao.UserDao;
import exception.CameraNotDeletedException;
import exception.CameraNotFoundException;
import exception.CameraNotSavedException;
import exception.PrivilegeNotSavedException;
import model.User;
import model.Camera;

@WebServlet("/CameraServlet")
public class CameraServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	final CameraDao cameraDao = new CameraDao();
	final UserDao userDao = new UserDao();
	final PrivilegeDao privilegeDao = new PrivilegeDao();
       
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String lastCamera = request.getParameter("selected");
		String status = request.getParameter("status");
			
		List<User> userlist = userDao.list();
		List<Camera> cameralist = cameraDao.list();
		if(id != null && lastCamera != null) {
			if(!lastCamera.equals(id)){
				Camera selectedCamera = cameraDao.find(extractID(id));
				List<Long> privilegeList = privilegeDao.listPrivileges(selectedCamera);
				request.setAttribute("selectedCamera", selectedCamera);
				request.setAttribute("privilegeList", privilegeList);
			} else {
				request.removeAttribute("selectedCamera");
				request.removeAttribute("privilegeList");
			} 
		}		
		if(status != null)
			request.setAttribute("status", status);
		request.setAttribute("userlist", userlist);
		request.setAttribute("cameralist", cameralist);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/CameraList.jsp");
		dispatcher.forward(request, response);	
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String action = request.getParameter("action");
    	
    	switch(action) {
			case "new": actionNew(request, response); break;
			case "edit": actionEdit(request, response); break;
			case "delete": actionDelete(request, response); break;
			case "privilege": actionPrivilege(request, response); break;
			case "save": actionSave(request, response); break;
			case "update": actionUpdate(request, response); break;
    	}
    }
    
    void actionNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	List<User> userlist = userDao.list();
		request.setAttribute("userlist", userlist);        	
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/CameraEdit.jsp");
		dispatcher.forward(request, response);	
    }
    
    void actionEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	String id = request.getParameter("id");
    	Camera selectedCamera = cameraDao.find(Long.parseLong(id));
    	List<User> userlist = userDao.list();
    	List<Long> privilegeList = privilegeDao.listPrivileges(selectedCamera);
		request.setAttribute("privilegeList", privilegeList);
    	request.setAttribute("selectedCamera", selectedCamera);
		request.setAttribute("userlist", userlist);        	
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/CameraEdit.jsp");
		dispatcher.forward(request, response);	
    }
    
    void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String status = null;
    	
    	Long id = extractID(request.getParameter("id"));
		
		try {
			Camera camera = cameraDao.find(id);
			cameraDao.delete(camera);    
			
			status = camera.getName() + " has been deleted!";
			
			List<User> userlist = userDao.list();
			List<Camera> cameralist = cameraDao.list();
			request.setAttribute("userlist", userlist);
			request.setAttribute("cameralist", cameralist);
			request.setAttribute("status", status);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/CameraList.jsp");
			dispatcher.forward(request, response);	
			
		}  catch (CameraNotDeletedException e) {
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}  
    }
    
    void actionPrivilege(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String status = null;
    	Long cameraID = null;
    	
    	if(isKnownCamera(request.getParameter("id")))
    		cameraID = extractID(request.getParameter("id"));
    	else
    		cameraID = getNewCameraID();
		  	
    	Camera camera = cameraDao.find(cameraID);
    	
    	try {
			List<User> userlist = userDao.list();
			for(int i = 0; i<userlist.size(); i++) {
				User user = userlist.get(i);
				String stringId = request.getParameter("user" + user.getId());
				boolean isPrivilege = Boolean.parseBoolean(stringId);
								
				if(isPrivilege == true) {
					privilegeDao.create(user, camera);
				} else {
					if(privilegeDao.isUserAuthorizedForCamera(user, camera));
						privilegeDao.delete(user, camera);
				}
			}
			User admin = userDao.find(Long.valueOf(1));
			privilegeDao.create(admin, camera);
    	} catch (PrivilegeNotSavedException e) {
			//Privilegien konnte nicht gespeichert werden
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
    		return;
    	}
    	
	    if(request.getParameter("action").equals("privilege")) {
			status = "Privileges from "+cameraDao.find(cameraID).getName()+" has been edited!";
			response.sendRedirect("CameraServlet?id="+cameraID+"&selected=&status="+status);
    	}
    }
    
    void actionSave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		String status = null;
		
		if(nameIsEmpty(request.getParameter("name"))) {
			request.setAttribute("error", "No name entered!");
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
    		dispatcher.forward(request, response);
    		return;
		}
		
		if(urlIsEmpty(request.getParameter("url"))) {
			request.setAttribute("error", "No url entered!");
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
    		dispatcher.forward(request, response);
    		return;
		}

		Camera camera = new Camera();		
		camera.setName(request.getParameter("name"));
		camera.setUrl(request.getParameter("url").replace("\r", "").replace("\n", ""));
		camera.setDescription(request.getParameter("description"));
			
		try {		
			cameraDao.create(camera);			
			camera.setId(getNewCameraID());
			
			status = camera.getId() + ":" + camera.getName() + " has been created.";
			
			actionPrivilege(request, response);
			response.sendRedirect("CameraServlet?id=" + camera.getId() + "&selected=&status=" + status);
			
		}  catch (CameraNotSavedException e) {
			//Camera konnte nicht gespeichert werden
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
    		return;
		}
    }  
    
    void actionUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String status = null;
    	 	
    	if(nameIsEmpty(request.getParameter("name"))) {
			request.setAttribute("error", "No name entered!");
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
    		dispatcher.forward(request, response);
    		return;
		}
		
		if(urlIsEmpty(request.getParameter("url"))) {
			request.setAttribute("error", "No url entered!");
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
    		dispatcher.forward(request, response);
    		return;
		}

		Long cameraID = extractID(request.getParameter("id"));
		
		try {
			Camera camera = cameraDao.find(cameraID);
			camera.setName(request.getParameter("name"));
			camera.setUrl(request.getParameter("url"));
			camera.setDescription(request.getParameter("description"));
			
			cameraDao.update(camera);
			status = camera.getId() + ":" + camera.getName() + " has been edited.";
			
			actionPrivilege(request, response);
			response.sendRedirect("CameraServlet?id=" + camera.getId() + "&selected=&status=" + status);
		} catch (CameraNotSavedException e) {
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
    		return;
		} catch (CameraNotFoundException e) {
			System.out.println("Error - camera with id " + cameraID + " not found.");
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}	
    }
    
	private Long extractID(String id) {
		return Long.valueOf(id);
	}
	
	private boolean isKnownCamera(String id) {
    	return id != null && id != "";
	}
	
	private boolean nameIsEmpty(String name) {
    	return name == null || name == "";
	}
	
	private boolean urlIsEmpty(String url) {
    	return url == null || url == "";
	}
	
	private Long getNewCameraID() {
		List<Camera> cameralist = cameraDao.list();
 		return cameralist.get(cameralist.size()-1).getId();
	}
}
