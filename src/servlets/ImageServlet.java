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
import model.User;
import control.Controller;
import dao.CameraDao;
import dao.ImageDao;
import dao.UserDao;
import dao.PrivilegeDao;

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
	final PrivilegeDao privilegeDao = new PrivilegeDao();
	
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
			Camera camera = cameraDao.find(Long.valueOf(cam));
			
			List<Image> images = imageDao.find(camera, Timestamp.valueOf(dateStart),Timestamp.valueOf(dateEnd));
			
			for(int i = 0; i < images.size(); i++) {
				String path = controller.generatePath(camera, images.get(i).getDate()) + controller.generateName(images.get(i).getDate());
				String pathThumbnail = controller.generatePathThumbnail(camera, images.get(i).getDate()) + controller.generateName(images.get(i).getDate());
				
				images.get(i).setPath(path);
				images.get(i).setPathThumbnail(pathThumbnail);
			}
			
			
			String loggeduser = request.getParameter("loggeduser");
			User user = userDao.find(Long.valueOf(loggeduser));
			List<Long> cameras = privilegeDao.listPrivileges(user);
			request.setAttribute("cameraList", cameras);			
			request.setAttribute("imageList", images);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ImagePage.jsp");
			dispatcher.forward(request, response);
		} else {
			String loggeduser = request.getParameter("loggeduser");
			User user = userDao.find(Long.valueOf(loggeduser));
			List<Long> cameras = privilegeDao.listPrivileges(user);
			request.setAttribute("cameraList", cameras);
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
