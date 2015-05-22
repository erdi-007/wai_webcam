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
				<li><a href="UserServlet">User</a></li>
				<li><a href="CameraServlet">Cameras</a></li>
				<li><a href="PrivilegeServlet">Privileges</a></li>
			</ul>
		</div>
	</nav>
	<form method="post">
		<table>
			<tr>
				<td valign="top">User-Liste
					<table border="1">
						<tbody>
							<tr>
								<td>Id</td>
								<td>Name</td>
								<td>Admin</td>
							</tr>
							<c:forEach var="user_" items="${userlist}">
								<tr>
									<td><c:out value="${user_.id}" /></td>
									<td><a href="UserServlet?id=${user_.id}"><c:out
												value="${user_.name}" /></a></td>
									<td><c:out value="${user_.admin}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</td>
				<td valign="top">User
					<table border="1">
						<tbody>
							<tr>
								<td>Name:</td>
								<td><input type="text" name="name"
									value="${selectedUser.name}"></td>
							</tr>
							<tr>
								<td>Admin:</td>
								<td><input type="checkbox" name="admin" value="true"
									id="admin"></td>
							</tr>
							<tr>
								<td>Passwort:</td>
								<td><input type="password" name="password"
									value="${selectedUser.password}"></td>
							</tr>
							<tr>
								<td colspan="3">
									<input type="submit" name="btnNeu" value="New" onclick="form.action='UserServlet?button=new';">
									<input type="submit" name="btnEdit" value="Edit" onclick="form.action='UserServlet?button=edit';">
									<input type="submit" name="btnClear" value="Clear" onclick="form.action='UserServlet';">
									<input type="submit" name="btnDelete" value="Delete" onclick="form.action='UserServlet?button=delete';">
								</td>
							</tr>
						</tbody>
					</table> <input type="hidden" id="admin_state"
					value="${selectedUser.admin}">
				</td>

				<td valign="top">Cameras
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
				</td>
			</tr>
		</table>
	</form>
	<br>
	<table>
		<tr>
			<td>
				<form action="LoginSuccess.jsp" method="post">
					<input type="submit" value="Zurück">
				</form>
			</td>
			<td>
				<form action="LogoutServlet" method="post">
					<input type="submit" value="Logout">
				</form>
			</td>
		</tr>
	</table>
</body>
</html>