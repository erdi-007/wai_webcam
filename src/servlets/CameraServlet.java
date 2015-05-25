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
		
		String id = request.getParameter("id");
		String lastCamera = request.getParameter("selected");
		String status = request.getParameter("status");
			
		List<User> userlist = dao.getUserList();
		List<Camera> cameralist = dao.getCameraList();
		if(id != null && lastCamera != null) {
			if(!lastCamera.equals(id)){
				Camera selectedCamera = dao.getCamera(Long.parseLong(id));
				List<Long> privilegeList = dao.getPrivilegesCamera(Long.parseLong(id));
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
    	String status = null;
    	
    	if(action.equals("new")) {
    		
        	List<User> userlist = dao.getUserList();
    		request.setAttribute("userlist", userlist);        	
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/CameraEdit.jsp");
    		dispatcher.forward(request, response);	
    	} 
    	
    	if(action.equals("edit")) {
    		
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
    	
    	if(action.equals("delete")) {
    		Long id = Long.valueOf(request.getParameter("id"));
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
    	
    	if(action.equals("privilege")) {
    		Long id = Long.valueOf(request.getParameter("id"));  
			savePrivileges(request, response, id);
			status = "Privileges from "+dao.getCamera(id).getName()+" has been edited!";
			response.sendRedirect("CameraServlet?id="+id+"&selected=&status="+status);
    	}
    	
    	if(action.equals("save")) {
    		
    		Long id = null;
    		String name = null;
    		String url = null;
    		
    		if(request.getParameter("id") != null && request.getParameter("id") != "") {
    			id = Long.valueOf(request.getParameter("id"));
    		} 
    		
    		if(request.getParameter("name") != null && request.getParameter("name") != "") {
        		name = request.getParameter("name");
    		} else {
				//kein Name eingegeben
				request.setAttribute("error", "No name entered!");
	    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
	    		dispatcher.forward(request, response);
	    		return;
    		}
    		
    		if(request.getParameter("url") != null && request.getParameter("url") != "") {
        		url = request.getParameter("url").replace("\r", "").replace("\n", "");
    		} else {
				//keine Url eingegeben
				request.setAttribute("error", "No url entered!");
	    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
	    		dispatcher.forward(request, response);
	    		return;
    		}
    		
    		String description = request.getParameter("description");
    		
			Camera camera = new Camera();
			camera.setId(id);
			camera.setName(name);
			camera.setDescription(description);
			camera.setUrl(url);
			
			if(id == null) {
				status = name + " has been added!";
			} else {
				status = name + " has been edited!";
			}
			
			try {		
				dao.save(camera);
				List<Camera> cameralist = dao.getCameraList();
				if(id == null)
					id = cameralist.get(cameralist.size()-1).getId();
				savePrivileges(request, response, id);
				response.sendRedirect("CameraServlet?id="+id+"&selected=&status="+status);
				
			}  catch (CameraNotSavedException e) {
				//Camera konnte nicht gespeichert werden
				request.setAttribute("error", e.getMessage());
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
				dispatcher.forward(request, response);
	    		return;
			}
    	}
    }
    
    public void savePrivileges(HttpServletRequest request, HttpServletResponse response, Long id) throws ServletException, IOException {
    	
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
	}	
}
