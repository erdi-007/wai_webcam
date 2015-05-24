<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII" import="model.User"%>
<%@ page import="model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	User userinfo = (User) (session.getAttribute("userinfo"));
%>

<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>User Logged Successfully</title>
<script type="text/javascript">
	function validate() {
		if (<%=userinfo.isAdmin()%> == true) {
			document.getElementById('settings').style.display = 'block';
		} else if (<%=userinfo.isAdmin()%> == false) {
			document.getElementById('settings').style.display = 'none';
		}
	}
</script>
</head>
<body onload="validate()">
	<%
		//allow access only if session exists
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
		}
	%>
	<center>
		Name:
		<%=userinfo.getName()%>
		<br> Admin-Status:
		<%=userinfo.isAdmin()%>
		<br> <br>
		
		<form action="ImageServlet" method="get">
		<input type="text" name="datumStart">
		<input type="text" name="datumEnde">
		<input type="submit"name="datum" value="Suchen">
		</form>
		<c:forEach var="images" items="${imageList}">
		<img src="${images.path}">
		</c:forEach>
		Bildinformationen:
		<br>
		Kamera: ${image.cameraID}
		<br>
		Pfad: ${image.path}
		<br>
		Datum: ${image.date}
		<br> <br>
		<table>
			<tr>
				<td>
					<form action="ImageServlet" method="get">
						<input type="submit" name ="image"value="Image">
					</form>
				</td>
				<td>
					<div id="settings">
						<form action="UserServlet" method="get">
							<input type="submit" value="Settings">
						</form>
					</div>
				</td>
				<td>
					<form action="LogoutServlet" method="post">
						<input type="submit" value="Logout">
					</form>
				</td>
			</tr>
		</table>
	</center>
</body>
</html>