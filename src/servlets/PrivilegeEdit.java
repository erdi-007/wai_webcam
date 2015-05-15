/*
 * Created on 22.11.2004
 */
package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import dao.DaoFactory;
import exception.PrivilegeNotDeletedException;
import control.Controller;

@WebServlet("/privilegeedit")
public class PrivilegeEdit extends HttpServlet {	
	
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
	final Controller controller = new Controller();

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String action = request.getParameter("action");
		
		if (action == null) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
			dispatcher.forward(request, response);
		}
				
		Long userID = null;
		Long cameraID = null;
		
		if (request.getParameter("userid") != null || request.getParameter("cameraid") != null) {
			userID = Long.valueOf(request.getParameter("userid"));
			cameraID = Long.valueOf(request.getParameter("cameraid"));
		}
				
		if(action.equals("privilegedelete")) {			
			try {
				dao.deletePrivilege(userID, cameraID);
				response.sendRedirect(request.getContextPath() + "/privilegelist");
			} catch (PrivilegeNotDeletedException e) {
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
				dispatcher.forward(request, response);
			}
		}
	}
}
