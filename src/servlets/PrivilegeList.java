package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.Controller;
import model.Privilege;
public class PrivilegeList extends HttpServlet {	
	
	private static final long serialVersionUID = 1L;
	
	final Controller controller = new Controller();
		
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		
		List<Privilege> privileges = controller.getPrivilegeList();
		request.setAttribute("privileges", privileges);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/privilegelist.jsp");
		dispatcher.forward(request, response);		
	}
}
