package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import dao.DaoFactory;
import exception.AdminCantBeDeleted;
import exception.AdminRightsCantBeRemoved;
import exception.InputNotFilledExeption;
import exception.UserExistsException;
import exception.UserNotSavedException;
import model.User;
import model.Camera;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
       
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String lastUser = request.getParameter("selected");
			
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
		request.setAttribute("userlist", userlist);
		request.setAttribute("cameralist", cameralist);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/UserList.jsp");
		dispatcher.forward(request, response);	
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String button = request.getParameter("button");
    		
		String name = request.getParameter("name");
		boolean admin = Boolean.parseBoolean(request.getParameter("admin"));
		String password = request.getParameter("password");
		    			
		if(button != null) {
			if(button.equals("edit")) {
				if(name != "" && password != "" ) {
					User user = dao.getUser(name);
					user.setName(name);
					user.setAdmin(admin);
					user.setPassword(password);	
					
					if(name.equals("admin") && admin == false) {
						throw new AdminRightsCantBeRemoved();
					}
					
					try {		
						dao.save(user);
						List<Camera> cameralist = dao.getCameraList();
						for(int i = 0; i<cameralist.size(); i++) {
							Long cameraId = cameralist.get(i).getId();
							String stringId = request.getParameter("cam"+cameraId);
							boolean test = Boolean.parseBoolean(stringId);
							
							if(test == true) {
								dao.savePrivilege(dao.getUser(name).getId(), cameraId);
							} else {
								if(dao.getPrivilege(dao.getUser(name).getId(), cameraId))
									dao.deletePrivilege(dao.getUser(name).getId(), cameraId);
							}
						}
					}  catch (UserNotSavedException e) {
						RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
						dispatcher.forward(request, response);
					}
				}
			} else if (button.equals("new")) {
				
				User user = new User();	
				if(name != "" && password != "" ) {
					user.setName(name);
					user.setAdmin(admin);
					user.setPassword(password);	
					
					try {
						if(dao.existsUser(user) == true) {
							throw new UserExistsException();
						} else {
							dao.save(user);
							List<Camera> cameralist = dao.getCameraList();
							for(int i = 0; i<cameralist.size(); i++) {
								Long cameraId = cameralist.get(i).getId();
								String stringId = request.getParameter("cam"+cameraId);
								boolean test = Boolean.parseBoolean(stringId);
								
								if(test == true) {
									dao.savePrivilege(dao.getUser(name).getId(), cameraId);
								} 
							}
						}
					}  catch (UserNotSavedException e) {
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/UserList.jsp");
						rd.include(request, response);
					}
				} else {
					throw new InputNotFilledExeption();
				}
			} else if (button.equals("delete")) {
				if(name.equals("admin")) {
					throw new AdminCantBeDeleted();
				}
				dao.deleteUser(dao.getUser(name).getId());
			}
		} 
		response.sendRedirect(request.getContextPath() + "/UserServlet");
    }
}
