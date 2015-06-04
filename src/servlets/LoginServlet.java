package servlets;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import crypto.MD5;
import dao.CameraDao;
import dao.PrivilegeDao;
import dao.UserDao;
import model.Camera;
import model.User;
import exception.UserNotFoundException;

/** * Servlet implementation class LoginServlet */

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger jlog = Logger.getLogger(LoginServlet.class);

	final UserDao userDao = new UserDao();
	final CameraDao cameraDao = new CameraDao();
	final PrivilegeDao privilegeDao = new PrivilegeDao();
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {

		
		User loginCredentials = new User();
		loginCredentials.setName(request.getParameter("un"));
		loginCredentials.setPassword(request.getParameter("pw"));
		
		Boolean isValid = false;
		User user = new User();
		
		try
		{
			user = userDao.find(loginCredentials.getName());
			isValid = MD5.validate(loginCredentials.getPassword(), user.getPassword());
		}
		catch (UserNotFoundException e)
		{
			e.printStackTrace();
		}	
		
		if (isValid) {
			HttpSession session = request.getSession();
			session.setAttribute("userinfo", user);
			session.setMaxInactiveInterval(30 * 60);
			Cookie userName = new Cookie("user", user.getName());
			userName.setMaxAge(30 * 60);
			response.addCookie(userName);
//			response.sendRedirect("ImagePage.jsp");
			jlog.info("User: " + user.getName()+" logged in." );
			List<Long> cameraIds = privilegeDao.listPrivileges(user);
			List<Camera> cameras = new ArrayList<Camera>();
			for (int i = 0; i < cameraIds.size(); i++)
			{
				cameras.add(cameraDao.find(cameraIds.get(i)));
			}
			request.setAttribute("cameraList", cameras);
			getServletContext().getRequestDispatcher("/ImagePage.jsp").forward(request, response);
			
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
			PrintWriter out = response.getWriter();
			out.println("<center><font color=red>Either user name or password is wrong.</font></center>");
			rd.include(request, response);
		}
	}
}