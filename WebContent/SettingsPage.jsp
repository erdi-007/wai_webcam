<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII" import="model.User"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
	<title>Settings</title>
</head>
<body>
	<%//allow access only if session exists
		String user = null;
		if (session.getAttribute("userinfo") == null) {
			response.sendRedirect("login.html");
		} else
			user = (String) session.getAttribute("user");
		String userName = null;
		String sessionID = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user"))
					userName = cookie.getValue();
				if (cookie.getName().equals("JSESSIONID"))
					sessionID = cookie.getValue();
			}
		}%>
	<nav>
		<div class="nav">
			<ul>
				<li><a href="SettingsServlet?action=user">User</a></li>
				<li><a href="SettingsServlet?action=cameras">Cameras</a></li>
				<li><a href="SettingsServlet?action=privileges">Privileges</a></li>
			</ul>
		</div>		
	</nav>
</body>
</html>