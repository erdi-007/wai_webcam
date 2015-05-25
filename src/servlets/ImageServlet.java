package servlets;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Camera;
import model.Image;
import control.Controller;
import dao.Dao;
import dao.DaoFactory;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Controller controller;
	final Dao dao = DaoFactory.getInstance().getDao();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (controller ==null)
			controller = new Controller();
		
		if(request.getParameter("image")!=null){
			List<Camera> cameralist =dao.getCameraList();
			for(int i = 0; i< cameralist.size(); i++) {
				dao.save(controller.saveImage(cameralist.get(i)));
			}
		}
		
		if(request.getParameter("datum")!=null){
			
			String timeStart =request.getParameter("timeStart");
			String timeEnd = request.getParameter("timeEnd");
			String dateStart = request.getParameter("dateStart");
			String dateEnd = request.getParameter("dateEnd");
			
//Vorbereitung um timestamp zu erzeugen
			String startDate = dateStart + " " + timeStart+":00";
			String endDate = dateEnd +" " + timeEnd+":00";
			
			List<Image> images = dao.getImages(dao.getCameraList().get(0).getId(), Timestamp.valueOf(startDate),Timestamp.valueOf(endDate));
//Problem im Pfad ist das erste Zeichen ein / muss entfern werden und auf _resize.jpg ändern um thumbnail anzuzeigen
			for(int i=0;i<images.size();i++){
				images.get(i).setPath(images.get(i).getPath().substring(1));
				images.get(i).setPathThumbnail("file:///"+images.get(i).getPathThumbnail().substring(1));
			}
System.out.println("image pfad: "+images.get(images.size()-1).getPath());
System.out.println("thumb pfad: "+images.get(images.size()-1).getPathThumbnail());
		request.setAttribute("imageList", images);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/ImagePage.jsp");
		dispatcher.forward(request, response);
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

}
