package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Camera;
import dao.Dao;
import dao.DaoFactory;

/**
 * Servlet implementation class KameraEdit
 */
public class KameraEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	final Dao kameraDao = DaoFactory.getInstance().getDao();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if(action == null){
			RequestDispatcher dispatcher =getServletContext().getRequestDispatcher("/jsp/error.jsp");
			dispatcher.forward(request, response);
		}
		
		Long id = null;
		
		if(request.getParameter("id")!= null){
			id=Long.valueOf(request.getParameter("id"));
		}
		
		if(action.equals("add")){
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/add.jsp");
			dispatcher.forward(request, response);
		}else if(action.equals("edit")){
//			try{
				Camera kamera = kameraDao.get_camera(id);
				request.setAttribute("kamera", kamera);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/edit.jsp");
				dispatcher.forward(request, response);
		
			
//			catch (BookNotFoundException e) {
//				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
//				dispatcher.forward(request, response);
//			}	
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
Long id = null;
		
		if(request.getParameter("id") != null) {
			id = Long.valueOf(request.getParameter("id"));
		}
		
		String name = request.getParameter("name");
		String url = request.getParameter("url");
		String pfad = request.getParameter("pfad");
		String beschreibung = request.getParameter("beschreibung");
		
		Camera kamera = new Camera();		
		kamera.set_id(id);
		kamera.set_url(url);
		kamera.set_name(name);		
//		kamera.setPfad(pfad);
//		kamera.setBeschreibung(beschreibung);
		
			
			kameraDao.save(kamera);
			response.sendRedirect(request.getContextPath() + "/list");
		  
//		catch (BookNotSavedException e) {
//			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/error.jsp");
//			dispatcher.forward(request, response);
//		}
	}
	

}
