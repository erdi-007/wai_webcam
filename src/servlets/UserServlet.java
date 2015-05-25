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
		if(id != null && lastUser != null) {
			if(!lastUser.equals(id)){
				User selectedUser = dao.getUser(Long.parseLong(id));
				List<Long> privilegeList = dao.getPrivilegesUser(Long.parseLong(id));
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
    	String status = null;
    	
    	if(action.equals("new")) {
    		
        	List<Camera> cameralist = dao.getCameraList();
    		request.setAttribute("cameralist", cameralist);        	
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserEdit.jsp");
    		dispatcher.forward(request, response);	
    	}    	
    	else if(action.equals("edit")) 
    	{
    		
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
    	else if(action.equals("delete")) 
    	{
    		Long id = extractID(request.getParameter("id"));
    		String name = dao.getUser(id).getName();
    		
			try {		
	    		dao.deleteUser(id);
	    		
	    		status = name + " has been deleted!";
	    		
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
    	else if(action.equals("privilege")) 
    	{
    		Long id = extractID(request.getParameter("id")); 
			savePrivileges(request, response, id);
			status = "Privileges from "+dao.getUser(id).getName()+" has been edited!";
			response.sendRedirect("UserServlet?id="+id+"&selected=&status="+status);
    	}    	
    	else if(action.equals("save")) 
    	{   		
    		if(nameIsEmpty(request.getParameter("name")))
    		{
    			request.setAttribute("error", "No name entered!");
	    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
	    		dispatcher.forward(request, response);
	    		return;
    		}
    		
    		User user = new User();
    		   		
    		if(isKnownUser(request.getParameter("id")))
    			user.setId(extractID(request.getParameter("id")));
    	
    		user.setName(request.getParameter("name"));	
    		user.setAdmin(Boolean.parseBoolean(request.getParameter("admin")));
    		user.setPassword(hashIfNecessary(request.getParameter("password")));
        					
			if(newUser(user)) 
			{	
				if(usernameExists(user.getName()))
				{
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
			else // User exists -> edit
			{
				if(user.getPassword().isEmpty())
					user.setPassword(dao.getUser(user.getId()).getPassword());
				
				status = user.getName() + " has been edited!";
			}			
			
			try {		
				dao.save(user);
				Long id = dao.getUser(user.getName()).getId();
				savePrivileges(request, response, id);
				response.sendRedirect("UserServlet?id="+id+"&selected=&status="+status);
				
			}  catch (UserNotSavedException e) {
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
			//Privilegien konnte nicht gespeichert werden
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
    		return;
    	}
	}	

	private boolean usernameExists(String name) {
		try 
		{
			dao.getUser(name);
			return true;

		} 
		catch (UserNotFoundException e) 
		{
			return false;
		}
	}

	private boolean newUser(User user) {
		if(user.getId() == null)
			return true;
		return false;
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
}
