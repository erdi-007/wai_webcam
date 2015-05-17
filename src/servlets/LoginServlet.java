package servlets;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			java.io.IOException {

		User user = new User();
		user.setName(request.getParameter("un"));
		user.setPassword(request.getParameter("pw"));
		user = dao.login(user);
		if (user.isValid()) {
			HttpSession session = request.getSession();
			session.setAttribute("userinfo", user);
			session.setMaxInactiveInterval(30 * 60);
			Cookie userName = new Cookie("user", user.getName());
			userName.setMaxAge(30 * 60);
			response.addCookie(userName);
			response.sendRedirect("LoginSuccess.jsp");
		} else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
			PrintWriter out = response.getWriter();
			out.println("<center><font color=red>Either user name or password is wrong.</font></center>");
			rd.include(request, response);
		}
	}
}