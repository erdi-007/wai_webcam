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
import crypto.MD5;
import dao.CameraDao;
import dao.PrivilegeDao;
import dao.UserDao;
import exception.PrivilegeNotSavedException;
import exception.UserNotDeletedException;
import exception.UserNotFoundException;
import exception.UserNotSavedException;
import model.User;
import model.Camera;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	final UserDao userDao = new UserDao();
	final CameraDao cameraDao = new CameraDao();
	final PrivilegeDao privilegeDao = new PrivilegeDao();
	
	Controller controller = new Controller();
       
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String lastUser = request.getParameter("selected");
		String status = request.getParameter("status");
			
		List<User> userlist = userDao.getListOfAllUsers();
		List<Camera> cameralist = cameraDao.getListOfAllCameras();
		if(id != null && lastUser != null) {
			if(!lastUser.equals(id)){
				User selectedUser = userDao.getUser(extractID(id));
				List<Long> privilegeList = privilegeDao.getPrivilegesForUser(extractID(id));
				request.setAttribute("selectedUser", selectedUser);
				request.setAttribute("privilegeList", privilegeList);
			}
		} else {
			request.removeAttribute("selectedUser");
			request.removeAttribute("privilegeList");
		}
		if(status != null)
			request.setAttribute("status", status);
		request.setAttribute("userlist", userlist);
		request.setAttribute("cameralist", cameralist);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserList.jsp");
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
    	}
    }
    
    void actionNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	List<Camera> cameralist = cameraDao.getListOfAllCameras();
		request.setAttribute("cameralist", cameralist);        	
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserEdit.jsp");
		dispatcher.forward(request, response);	
    }
    
    void actionEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	String id = request.getParameter("id");
    	User selectedUser = userDao.getUser(Long.parseLong(id));
    	List<Camera> cameralist = cameraDao.getListOfAllCameras();
		List<Long> privilegeList = privilegeDao.getPrivilegesForUser(Long.parseLong(id));
		request.setAttribute("privilegeList", privilegeList);
    	request.setAttribute("selectedUser", selectedUser);
		request.setAttribute("cameralist", cameralist);        	
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserEdit.jsp");
		dispatcher.forward(request, response);	
    }
    
    void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Long id = extractID(request.getParameter("id"));
		String name = userDao.getUser(id).getName();
    	String status = null;
		
		try {		
    		userDao.deleteUser(id);
    		
    		status = name + " has been deleted!";
    		
    		List<User> userlist = userDao.getListOfAllUsers();
    		List<Camera> cameralist = cameraDao.getListOfAllCameras();
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
    
    void actionPrivilege(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String status = null;
    	Long id = null;
    	
    	if(isKnownUser(request.getParameter("id")))
    		id = extractID(request.getParameter("id"));
    	else
    		id = getNewUserID();

    	try {
			List<Camera> cameralist = cameraDao.getListOfAllCameras();
			for(int i = 0; i<cameralist.size(); i++) {
				Long cameraId = cameralist.get(i).getId();
				String stringId = request.getParameter("cam"+cameraId);
				boolean isPrivilege = Boolean.parseBoolean(stringId);
				
				if(isPrivilege == true) {
					privilegeDao.savePrivilege(id, cameraId);
				} else {
					if(privilegeDao.isUserAuthorizedForCamera(id, cameraId))
						privilegeDao.deletePrivilege(id, cameraId);
				}
			}
    	} catch (PrivilegeNotSavedException e) {
			//Privilegien konnte nicht gespeichert werden
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
    		return;
    	}
		
    	if(request.getParameter("action").equals("privilege")) {
			status = "Privileges from "+userDao.getUser(id).getName()+" has been edited!";
			response.sendRedirect("UserServlet?id="+id+"&selected=&status="+status);
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
		
		User user = new User();		
		user.setName(request.getParameter("name"));	
		user.setAdmin(Boolean.parseBoolean(request.getParameter("admin")));
		user.setPassword(hashIfNecessary(request.getParameter("password")));
    					
		if(isKnownUser(request.getParameter("id"))) {	
			
			user.setId(extractID(request.getParameter("id")));
			if(user.getPassword().isEmpty())
				user.setPassword(userDao.getUser(user.getId()).getPassword());
			
			status = user.getName() + " has been edited!";
			
		} else {
			if(usernameExists(user.getName()))	{
				request.setAttribute("error", "User already exists!");
	    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
	    		dispatcher.forward(request, response);
	    		return;
			}

			if(user.getPassword().isEmpty()) {
				request.setAttribute("error", "No password entered!");
	    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
	    		dispatcher.forward(request, response);
	    		return;
			} 

			status = user.getName() + " has been added!";
		}			
		
		try {		
			userDao.saveUser(user);
			Long id = userDao.getUser(user.getName()).getId();
			actionPrivilege(request, response);
			response.sendRedirect("UserServlet?id="+id+"&selected=&status="+status);
			
		}  catch (UserNotSavedException e) {
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
    		return;
		}
    }  

	private boolean usernameExists(String name) {
		try {
			userDao.getUser(name);
			return true;
		} 
		catch (UserNotFoundException e) {
			return false;
		}
	}

	private String hashIfNecessary(String password) {
		if(!password.isEmpty())
    		password = MD5.create(password);
		
		return password;
	}

	private boolean nameIsEmpty(String name) {
    	return name == null || name == "";
	}

	private Long extractID(String id) {
			return Long.valueOf(id);
	}

	private boolean isKnownUser(String id) {
    	return id != null && id != "";
	}
	
	private Long getNewUserID() {
		List<User> userlist = userDao.getListOfAllUsers();
 		return userlist.get(userlist.size()-1).getId();
	}
}
