/*
 * Created on 22.11.2004
 */
package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import dao.DaoFactory;
import exception.UserNotDeletedException;
import exception.UserNotFoundException;
import exception.UserNotSavedException;
import model.User;
import control.Controller;

public class UserEdit extends HttpServlet {	
	
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
	final Controller controller = new Controller();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String action = request.getParameter("action");
		
		if (action == null) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
			dispatcher.forward(request, response);
		}
				
		Long id = null;
		
		if (request.getParameter("id") != null) {
			id = Long.valueOf(request.getParameter("id"));
		}
				
		if(action.equals("useradd")){
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/useradd.jsp");
			dispatcher.forward(request, response);		
		} else if(action.equals("useredit")) {			
			try {
				User user = dao.getUser(id);
				request.setAttribute("user", user);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/useredit.jsp");
				dispatcher.forward(request, response);
			} catch (UserNotFoundException e) {
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
				dispatcher.forward(request, response);
			}				
		} else if(action.equals("userdelete")) {			
			try {
				dao.deleteUser(id);
				response.sendRedirect(request.getContextPath() + "/userlist");
			} catch (UserNotDeletedException e) {
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
				dispatcher.forward(request, response);
			}
		} 
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		Long id = null;
		
		if(request.getParameter("id") != null) {
			id = Long.valueOf(request.getParameter("id"));
		}
		
		String name = request.getParameter("name");
		boolean admin = Boolean.parseBoolean(request.getParameter("admin"));
		String password = request.getParameter("password");
				
		User user = new User();	
		if(name != "" && password != "") {
			user.setId(id);
			user.setName(name);
			user.setAdmin(admin);
			user.setPassword(password);	
		}
		
		try {		
			dao.save(user);
			response.sendRedirect(request.getContextPath() + "/userlist");
		}  catch (UserNotSavedException e) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
			dispatcher.forward(request, response);
		}
	}
}
