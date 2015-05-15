package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Camera;

import dao.Dao;
import dao.DaoFactory;

public class CameraList extends HttpServlet {	
	
	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		List<Camera> collection = dao.getCameraList();
		request.setAttribute("cameras", collection);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/cameralist.jsp");
		dispatcher.forward(request, response);		
	}
}
