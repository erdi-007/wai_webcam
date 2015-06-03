package servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Camera;
import model.Image;
import control.Controller;
import dao.CameraDao;
import dao.ImageDao;
import dao.UserDao;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Controller controller;
	
	
	final CameraDao cameraDao = new CameraDao();
	final UserDao userDao = new UserDao();
	final ImageDao imageDao = new ImageDao();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (controller ==null)
			controller = new Controller();
		
		
		if(request.getParameter("image")!=null){
			List<Camera> cameralist = cameraDao.list();
			for(int i = 0; i< cameralist.size(); i++) {
				imageDao.save(controller.saveImage(cameralist.get(i)));
			}
		}else if(request.getParameter("datum")!=null){
			
			String dateStart = request.getParameter("dateStart");
			String dateEnd = request.getParameter("dateEnd");
			String cam = request.getParameter("cameras");
			
			List<Image> images = imageDao.find(cameraDao.find(Long.valueOf(cam)), Timestamp.valueOf(dateStart),Timestamp.valueOf(dateEnd));
			request.setAttribute("imageList", images);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ImagePage.jsp");
			dispatcher.forward(request, response);
		} 
		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	System.out.println("doPost");
	}

}
