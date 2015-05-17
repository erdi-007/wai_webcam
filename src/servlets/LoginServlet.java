package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import dao.Dao;
import dao.DaoFactory;

/** * Servlet implementation class LoginServlet */

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	final Dao dao = DaoFactory.getInstance().getDao();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		try {
			User user = new User();
			user.setName(request.getParameter("un"));
			user.setPassword(request.getParameter("pw"));
			user = dao.login(user);
			if (user.isValid()) {
				HttpSession session = request.getSession(true);
				session.setAttribute("currentSessionUser", user);
				response.sendRedirect("userLogged.jsp"); // logged-in page
			} else
				response.sendRedirect("invalidLogin.jsp"); // error page
		} catch (Throwable theException) {
			System.out.println(theException);
		}
	}
}