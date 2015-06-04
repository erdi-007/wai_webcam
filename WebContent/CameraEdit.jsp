<%@ page language="java" contentType="text/html"%>
<%@ page import="model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/screen.css">
<link rel="stylesheet" href="css/User.css">
<%
	User userinfo = (User) (session.getAttribute("userinfo"));
%>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Camera</title>
<script type="text/javascript">
	function validate() {	

		<c:forEach var="privilege_" items="${privilegeList}">
		document.getElementById('user' + "${privilege_}").checked = true;
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
				<li><a href="ImageServlet?loggeduser=<%=userinfo.getId()%>">Images</a></li>
				<li><a href="UserServlet">User</a></li>
				<li><a href="CameraServlet">Cameras</a></li>
				<li><a href="LogoutServlet">Logout</a></li>
				
			</ul>
		</div>
	</nav>	
	<form>
		<table>
			<tr>
				<td valign="top">Camera
						<table id="cameratable">
							<tbody>
								<tr>
									<td>Name:</td>
									<td><input style="width:98%"  type="text" name="name" value="${selectedCamera.name}"></td>
								</tr>
								<tr>
									<td>Description:</td>
									<td><textarea id="text" name="description" cols="35" rows="5">${selectedCamera.description}</textarea></td>
								</tr>
								<tr>
									<td>URL:</td>
									<td><textarea id="text" name="url" cols="35" rows="5">${selectedCamera.url}</textarea></td>
								</tr>
								<tr>
									<td colspan="3">
										<input type="submit" value="Save" onclick="form.action='CameraServlet?action=save'; form.method='post'"class="button">
										<input type="submit" value="Cancel" onclick="form.action='CameraServlet'; form.method='get'" class="button">										
									</td>
								</tr>
							</tbody>
						</table> 
						<input type="hidden" name="id" value="${selectedCamera.id}">
				</td>
				<td valign="top" >				
					<div id="privileges">
						User
						<table id="usertable">
							<tbody>
								<tr>
									<th>Id</th>
									<th>Name</th>
									<th>Privilege</th>
								</tr>
								<c:forEach var="user_" items="${userlist}">
									<tr>
										<td><c:out value="${user_.id}"/></td>
										<td><c:out value="${user_.name}" /></td>
										<td><input type="checkbox" name="user${user_.id}"
											value="true" id="user${user_.id}"></td>
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