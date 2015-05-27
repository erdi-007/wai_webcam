package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.Controller;
import dao.Dao;
import dao.DaoFactory;
import exception.CameraNotDeletedException;
import exception.CameraNotSavedException;
import exception.PrivilegeNotSavedException;
import model.User;
import model.Camera;

@WebServlet("/CameraServlet")
public class CameraServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
	Controller controller = new Controller();
       
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Long id = extractID(request.getParameter("id"));
		String lastCamera = request.getParameter("selected");
		String status = request.getParameter("status");
			
		List<User> userlist = dao.getUserList();
		List<Camera> cameralist = dao.getCameraList();
		if(id != null && lastCamera != null) {
			if(!lastCamera.equals(id)){
				Camera selectedCamera = dao.getCamera(id);
				List<Long> privilegeList = dao.getPrivilegesCamera(id);
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
			case "new": actionNew(request, response);
			case "edit": actionEdit(request, response);
			case "delete": actionDelete(request, response);
			case "privilege": actionPrivilege(request, response);
			case "save": actionSave(request, response);
    	}
    }
    
    void actionNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	List<User> userlist = dao.getUserList();
		request.setAttribute("userlist", userlist);        	
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/CameraEdit.jsp");
		dispatcher.forward(request, response);	
    }
    
    void actionEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	String id = request.getParameter("id");
    	Camera selectedCamera = dao.getCamera(Long.parseLong(id));
    	List<User> userlist = dao.getUserList();
    	List<Long> privilegeList = dao.getPrivilegesCamera(Long.parseLong(id));
		request.setAttribute("privilegeList", privilegeList);
    	request.setAttribute("selectedCamera", selectedCamera);
		request.setAttribute("userlist", userlist);        	
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/CameraEdit.jsp");
		dispatcher.forward(request, response);	
    }
    
    void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String status = null;
    	
    	Long id = extractID(request.getParameter("id"));
		String name = dao.getCamera(id).getName();
	
		status = name + " has been deleted!";
		
		try {		
			dao.deleteCamera(id);    		
			List<User> userlist = dao.getUserList();
			List<Camera> cameralist = dao.getCameraList();
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
    	Long id = null;
    	
    	if(isKnownCamera(request.getParameter("id")))
    		id = extractID(request.getParameter("id"));
    	else
    		id = getNewCameraID();
		
    	try {
			List<User> userlist = dao.getUserList();
			for(int i = 0; i<userlist.size(); i++) {
				Long userId = userlist.get(i).getId();
				String stringId = request.getParameter("user"+userId);
				boolean isPrivilege = Boolean.parseBoolean(stringId);
				
				if(isPrivilege == true) {
					dao.savePrivilege(userId, id);
				} else {
					if(dao.getPrivilege(userId, id))
						dao.deletePrivilege(userId, id);
				}
			}
			dao.savePrivilege(Long.valueOf(1), id);
    	} catch (PrivilegeNotSavedException e) {
			//Privilegien konnte nicht gespeichert werden
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
    		return;
    	}
    	
		status = "Privileges from "+dao.getCamera(id).getName()+" has been edited!";
		response.sendRedirect("CameraServlet?id="+id+"&selected=&status="+status);
    }
    
    void actionSave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		String status = null;
		
		if(nameIsEmpty(request.getParameter("name"))) {
			request.setAttribute("error", "No name entered!");
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
    		dispatcher.forward(request, response);
    		return;
		}
		
		if(urlIsEmpty(request.getParameter("name"))) {
			request.setAttribute("error", "No url entered!");
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
    		dispatcher.forward(request, response);
    		return;
		}

		Camera camera = new Camera();		
		camera.setName(request.getParameter("name"));
		camera.setUrl(request.getParameter("url").replace("\r", "").replace("\n", ""));
		camera.setDescription(request.getParameter("description"));
		
		if(isKnownCamera(request.getParameter("id"))) {
			camera.setId(extractID(request.getParameter("id")));
			status = camera.getName() + " has been edited!";
		} else {
			status = camera.getName() + " has been added!";
		}
		
		try {		
			dao.save(camera);
			actionPrivilege(request, response);
			
			Long id = null;
	    	if(isKnownCamera(request.getParameter("id")))
	    		id = extractID(request.getParameter("id"));
	    	else
	    		id = getNewCameraID();
	    	
			response.sendRedirect("CameraServlet?id="+id+"&selected=&status="+status);
			
		}  catch (CameraNotSavedException e) {
			//Camera konnte nicht gespeichert werden
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
		List<User> cameralist = dao.getUserList();
 		return cameralist.get(cameralist.size()-1).getId();
	}
}
