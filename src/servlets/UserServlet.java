package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.Controller;
import crypto.MD5;
import dao.CameraDao;
import dao.PrivilegeDao;
import dao.UserDao;
import exception.PrivilegeNotSavedException;
import exception.UserNotDeletedException;
import exception.UserNotFoundException;
import exception.UserNotSavedException;
import model.User;
import model.Camera;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final UserDao userDao = new UserDao();
	final CameraDao cameraDao = new CameraDao();
	final PrivilegeDao privilegeDao = new PrivilegeDao();

	Controller controller = new Controller();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		String lastUser = request.getParameter("selected");
		String status = request.getParameter("status");

		List<User> userlist = userDao.list();
		List<Camera> cameralist = cameraDao.list();
		if (id != null && lastUser != null) {
			if (!lastUser.equals(id)) {
				User selectedUser = userDao.find(extractID(id));
				List<Long> privilegeList = privilegeDao.listPrivileges(selectedUser);
				request.setAttribute("selectedUser", selectedUser);
				request.setAttribute("privilegeList", privilegeList);
			}
		} else {
			request.removeAttribute("selectedUser");
			request.removeAttribute("privilegeList");
		}
		if (status != null)
			request.setAttribute("status", status);
		request.setAttribute("userlist", userlist);
		request.setAttribute("cameralist", cameralist);
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/UserList.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");

		switch (action) {
		case "new":
			actionNew(request, response);
			break;
		case "update":
			actionUpdate(request, response);
			break;
		case "edit":
			actionEdit(request, response);
			break;
		case "delete":
			actionDelete(request, response);
			break;
		case "privilege":
			actionPrivilege(request, response);
			break;
		case "save":
			actionSave(request, response);
			break;
		}
	}

	void actionNew(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Camera> cameralist = cameraDao.list();
		request.setAttribute("cameralist", cameralist);
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/UserEdit.jsp");
		dispatcher.forward(request, response);
	}

	void actionEdit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		User selectedUser = userDao.find(Long.parseLong(id));
		List<Camera> cameralist = cameraDao.list();
		List<Long> privilegeList = privilegeDao.listPrivileges(selectedUser);
		request.setAttribute("privilegeList", privilegeList);
		request.setAttribute("selectedUser", selectedUser);
		request.setAttribute("cameralist", cameralist);
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/UserEdit.jsp");
		dispatcher.forward(request, response);
	}

	void actionDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long id = extractID(request.getParameter("id"));
		String status = null;

		try {
			User user = userDao.find(id);
			userDao.delete(user);

			status = user.getName() + " has been deleted!";

			List<User> userlist = userDao.list();
			List<Camera> cameralist = cameraDao.list();
			request.setAttribute("userlist", userlist);
			request.setAttribute("cameralist", cameralist);
			request.setAttribute("status", status);
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/UserList.jsp");
			dispatcher.forward(request, response);

		} catch (UserNotDeletedException e) {
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}
	}

	void actionPrivilege(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String status = null;
		Long userID = null;

		if (isKnownUser(request.getParameter("id")))
			userID = extractID(request.getParameter("id"));
		else
			userID = getNewUserID();
		
		User user = userDao.find(userID);

		try {
			List<Camera> cameralist = cameraDao.list();
			for (int i = 0; i < cameralist.size(); i++) {
				Camera camera = cameralist.get(i);
				String stringId = request.getParameter("cam" + camera.getId());
				boolean isPrivilege = Boolean.parseBoolean(stringId);

				if (isPrivilege == true) {
					privilegeDao.create(user, camera);
				} else {
					if (privilegeDao.isUserAuthorizedForCamera(user, camera))
						privilegeDao.delete(user, camera);
				}
			}
		} catch (PrivilegeNotSavedException e) {
			// Privilegien konnte nicht gespeichert werden
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}

		if (request.getParameter("action").equals("privilege")) {
			status = "Privileges from " + userDao.find(userID).getName()
					+ " has been edited!";
			response.sendRedirect("UserServlet?id=" + userID + "&selected=&status="
					+ status);
		}
	}

	void actionSave(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String status = null;

		if (nameIsEmpty(request.getParameter("name"))
				|| isKnownUser(request.getParameter("id"))) {
			request.setAttribute("error", "invalid Data");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}

		User user = new User();
		user.setName(request.getParameter("name"));
		user.setAdmin(Boolean.parseBoolean(request.getParameter("admin")));
		user.setPassword(hashIfNecessary(request.getParameter("password")));

		if(usernameExists(user.getName()))	{
			request.setAttribute("error", "User already exists!");
    		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
    		dispatcher.forward(request, response);
    		return;
		}
		
		if (user.getPassword().isEmpty()) {
			request.setAttribute("error", "No password entered!");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}

		try {
			userDao.create(user);
			user.setId(userDao.find(user.getName()).getId());

			status = user.getId() + ":" + user.getName() + " has been created.";

			actionPrivilege(request, response);
			response.sendRedirect("UserServlet?id=" + user.getId() + "&selected=&status=" + status);

		} catch (UserNotSavedException e) {
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}
	}

	void actionUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String status = null;

		Long id = extractID(request.getParameter("id"));

		if (id == null || nameIsEmpty(request.getParameter("name"))) {
			request.setAttribute("error", "invalid Data");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}

		try {
			User user = userDao.find(id);
			user.setName(request.getParameter("name"));
			user.setPassword(hashIfNecessary(request.getParameter("password")));
			user.setAdmin(Boolean.parseBoolean(request.getParameter("admin")));

			if (user.getPassword().isEmpty())
				user.setPassword(userDao.find(user.getId()).getPassword());

			userDao.update(user);
			status = user.getId() + ":" + user.getName() + " has been edited.";
			
			actionPrivilege(request, response);
			response.sendRedirect("UserServlet?id=" + user.getId() + "&selected=&status=" + status);
		} catch (UserNotSavedException e) {
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		} catch (UserNotFoundException e) {
			System.out.println("Error - user with id " + id + " not found.");
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error.jsp");
			dispatcher.forward(request, response);
			return;
		}
	}

	private String hashIfNecessary(String password) {
		if (!password.isEmpty())
			password = MD5.create(password);

		return password;
	}

	private boolean nameIsEmpty(String name) {
		return name == null || name == "";
	}

	private Long extractID(String id) {
		return Long.valueOf(id);
	}

	private boolean isKnownUser(String id) {
		return id != null && id != "";
	}
	
	private boolean usernameExists(String name) {
		try {
			userDao.find(name);
			return true;
		} 
		catch (UserNotFoundException e) {
			return false;
		}
	}

	private Long getNewUserID() {
		List<User> userlist = userDao.list();
		return userlist.get(userlist.size() - 1).getId();
	}
}
