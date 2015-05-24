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
			
			String datumStart =request.getParameter("datumStart");
			System.out.println("StartDatum: "+datumStart);
			String datumEnde = request.getParameter("datumEnde");
			Timestamp timeStart = new Timestamp(115, 4, 24, Integer.parseInt(datumStart), 0, 0, 0);
			Timestamp timeEnde = new Timestamp(115, 4, 24, Integer.parseInt(datumEnde), 0, 0, 0);

			List<Image> images = dao.getImages(dao.getCameraList().get(0).getId(), timeStart, timeEnde);

		Image image = images.get(0);
		image.setPath(image.getPath().substring(1));
		System.out.println("Image pfad: "+image.getPath());
		request.setAttribute("image", image);
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
