<%@ page language="java" contentType="text/html"%>
<%@ page import="model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>User</title>
<script type="text/javascript">
	function validate() {	
		if (document.getElementById('admin_state').value == "true") {
			document.getElementById('admin').checked = true;
		} else {
			document.getElementById('admin').checked = false;
		}
		<c:forEach var="privilege_" items="${privilegeList}">
		document.getElementById('cam' + "${privilege_}").checked = true;
		</c:forEach>	
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
	<nav>
		<div class="nav">
			<ul>
				<li><a href="ImagePage.jsp">Images</a></li>
				<li><a href="UserServlet">User</a></li>
				<li><a href="CameraServlet">Cameras</a></li>
				<li><a href="LogoutServlet">Logout</a></li>
				
			</ul>
		</div>
	</nav>	
	<form>
		<table>
			<tr>
				<td valign="top">User
						<table border="1">
							<tbody>
								<tr>
									<td>Name:</td>
									<td><input type="text" name="name" value="${selectedUser.name}"></td>
								</tr>
								<tr>
									<td>Admin:</td>
									<td><input type="checkbox" name="admin" value="true" id="admin"></td>
								</tr>
								<tr>
									<td>Passwort:</td>
									<td><input type="password" name="password" value=""></td>
								</tr>
								<tr>
									<td colspan="3">
										<input type="submit" value="Save" onclick="form.action='UserServlet?action=save'; form.method='post'">
										<input type="submit" value="Cancel" onclick="form.action='UserServlet'; form.method='get'">										
									</td>
								</tr>
							</tbody>
						</table> 
						<input type="hidden" id="admin_state" value="${selectedUser.admin}">
						<input type="hidden" name="id" value="${selectedUser.id}">
				</td>
				<td valign="top" >				
					<div id="privileges">
						Cameras
						<table border="1">
							<tbody>
								<tr>
									<td>Id</td>
									<td>Name</td>
									<td>Privilege</td>
								</tr>
								<c:forEach var="camera_" items="${cameralist}">
									<tr>
										<td><c:out value="${camera_.id}"/></td>
										<td title="${camera_.description}"><c:out value="${camera_.name}" /></td>
										<td><input type="checkbox" name="cam${camera_.id}"
											value="true" id="cam${camera_.id}"></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>					
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>