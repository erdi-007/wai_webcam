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
import exception.PrivilegeNotSavedException;
import exception.UserNotDeletedException;
import exception.UserNotFoundException;
import exception.UserNotSavedException;
import model.User;
import model.Camera;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
	Controller controller = new Controller();
       
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String lastUser = request.getParameter("selected");
		String status = request.getParameter("status");
			
		List<User> userlist = dao.getUserList();
		List<Camera> cameralist = dao.getCameraList();
		if(id != null && !lastUser.equals(id)) {
			User selectedUser = dao.getUser(Long.parseLong(id));
			List<Long> privilegeList = dao.getPrivilegesUser(Long.parseLong(id));
			request.setAttribute("selectedUser", selectedUser);
			request.setAttribute("privilegeList", privilegeList);
		} else {
			request.removeAttribute("selectedUser");
			request.removeAttribute("privilegeList");
		}		
		request.setAttribute("status", status);
		request.setAttribute("userlist", userlist);
		request.setAttribute("cameralist", cameralist);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserList.jsp");
		dispatcher.forward(request, response);	
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String action = request.getParameter("action");
    	String status = null;
    	
    	if(action.equals("new")) {
    		
        	List<Camera> cameralist = dao.getCameraList();
    		request.setAttribute("cameralist", cameralist);        	
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserEdit.jsp");
    		dispatcher.forward(request, response);	
    	} 
    	
    	if(action.equals("edit")) {
    		
        	String id = request.getParameter("id");
        	User selectedUser = dao.getUser(Long.parseLong(id));
        	List<Camera> cameralist = dao.getCameraList();
			List<Long> privilegeList = dao.getPrivilegesUser(Long.parseLong(id));
			request.setAttribute("privilegeList", privilegeList);
        	request.setAttribute("selectedUser", selectedUser);
    		request.setAttribute("cameralist", cameralist);        	
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserEdit.jsp");
    		dispatcher.forward(request, response);	
    	} 
    	
    	if(action.equals("delete")) {
    		Long id = Long.valueOf(request.getParameter("id"));

			status = "User has been deleted!";
    		
			try {		
	    		dao.deleteUser(id);    		
	    		List<User> userlist = dao.getUserList();
	    		List<Camera> cameralist = dao.getCameraList();
	    		request.setAttribute("userlist", userlist);
	    		request.setAttribute("cameralist", cameralist);
	    		request.setAttribute("status", status);
	    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserList.jsp");
	    		dispatcher.forward(request, response);	
	    		
			}  catch (UserNotDeletedException e) {
				request.setAttribute("error", e.getMessage());
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
				dispatcher.forward(request, response);
	    		return;
			}  
    	} 
    	
    	if(action.equals("privilege")) {
    		Long id = Long.valueOf(request.getParameter("id"));  
			savePrivileges(request, response, id);
			status = "Privileges from "+dao.getUser(id).getName()+" has been edited!";
			response.sendRedirect("UserServlet?id="+id+"&selected=&status="+status);
    	}
    	
    	if(action.equals("save")) {
    		
    		Long id = null;
    		String name = null;
    		
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
    		
    		boolean admin = Boolean.parseBoolean(request.getParameter("admin"));
    		String password = request.getParameter("password");
    		
			User user = new User();
			user.setId(id);
			user.setName(name);
			user.setAdmin(admin);
			
			if(id == null) {
				try {
					dao.getUser(name);
					
					//Username bereits vorhanden
					request.setAttribute("error", "User already exists!");
		    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
		    		dispatcher.forward(request, response);
		    		return;
				} catch (UserNotFoundException e) {
					if(password == "") {
						//kein Passwort eingegeben
						request.setAttribute("error", "No password entered!");
			    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			    		dispatcher.forward(request, response);
			    		return;
					} else {					
						user.setPassword(password);	
					}
					status = "User has been added!";
				}
			} else {
				if(password != "") {
					user.setPassword(password);	
				} else {
					user.setPassword(dao.getUser(id).getPassword());
				}

				status = "User has been edited!";
			}
			
			try {		
				dao.save(user);
				id = dao.getUser(name).getId();	
				savePrivileges(request, response, id);
				response.sendRedirect("UserServlet?id="+id+"&selected=&status="+status);
				
			}  catch (UserNotSavedException e) {
				//User konnte nicht gespeichert werden
				request.setAttribute("error", e.getMessage());
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
				dispatcher.forward(request, response);
	    		return;
			}
    	}
    }
    
    public void savePrivileges(HttpServletRequest request, HttpServletResponse response, Long id) throws ServletException, IOException {
    	
    	try {
			List<Camera> cameralist = dao.getCameraList();
			for(int i = 0; i<cameralist.size(); i++) {
				Long cameraId = cameralist.get(i).getId();
				String stringId = request.getParameter("cam"+cameraId);
				boolean isPrivilege = Boolean.parseBoolean(stringId);
				
				if(isPrivilege == true) {
					dao.savePrivilege(id, cameraId);
				} else {
					if(dao.getPrivilege(id, cameraId))
						dao.deletePrivilege(id, cameraId);
				}
			}
    	} catch (PrivilegeNotSavedException e) {
			//User konnte nicht gespeichert werden
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
    		return;
    	}
	}	
}
