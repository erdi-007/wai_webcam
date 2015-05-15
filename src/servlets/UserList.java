package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

import dao.Dao;
import dao.DaoFactory;

public class UserList extends HttpServlet {	
	
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {		
		List<User> collection = dao.getUserList();
		request.setAttribute("user", collection);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/userlist.jsp");
		dispatcher.forward(request, response);		
	}
}
