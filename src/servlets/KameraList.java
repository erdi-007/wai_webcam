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

/**
 * Servlet implementation class KameraList
 */
public class KameraList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	final Dao kameraDao = DaoFactory.getInstance().getDao();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Camera> collection = kameraDao.list_camera();
		request.setAttribute("kameras", collection);
		RequestDispatcher dispatcher =getServletContext().getRequestDispatcher("/jsp/kamera.jsp");
		dispatcher.forward(request, response);
	}



}
