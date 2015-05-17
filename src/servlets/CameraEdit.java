/*
 * Created on 22.11.2004
 */
package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import dao.DaoFactory;
import exception.CameraNotDeletedException;
import exception.CameraNotFoundException;
import exception.CameraNotSavedException;
import exception.ImageNotSavedException;
import model.Camera;
import control.Controller;

public class CameraEdit extends HttpServlet {	
	
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
				
		if(action.equals("cameraadd")){
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/cameraadd.jsp");
			dispatcher.forward(request, response);		
		} else if(action.equals("cameraedit")) {			
			try {
				Camera camera = dao.getCamera(id);
				request.setAttribute("camera", camera);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/cameraedit.jsp");
				dispatcher.forward(request, response);
			} catch (CameraNotFoundException e) {
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
				dispatcher.forward(request, response);
			}				
		} else if(action.equals("cameradelete")) {			
			try {
				dao.deleteCamera(id);
				controller.deleteDirectory(id);
				response.sendRedirect(request.getContextPath() + "/cameralist");
			} catch (CameraNotDeletedException e) {
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
				dispatcher.forward(request, response);
			}
		} else if(action.equals("imagesadd")) {			
			try {
				List<Camera> cameraList = dao.getCameraList();
				for(Integer i = 0; i < cameraList.size(); i++) {
					controller.saveImage(cameraList.get(i));
				}
				response.sendRedirect(request.getContextPath() + "/cameralist");
			} catch (ImageNotSavedException e) {
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
		String description = request.getParameter("description");
		String url = request.getParameter("url");
				
		Camera camera = new Camera();
		if(name != "" && description != "" && url != ""){
			camera.setId(id);
			camera.setName(name);
			camera.setDescription(description);
			camera.setUrl(url);	;	
		}	
		
		try {		
			dao.save(camera);
			response.sendRedirect(request.getContextPath() + "/cameralist");
		}  catch (CameraNotSavedException e) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
			dispatcher.forward(request, response);
		}
	}
}
