<%@ page language="java" contentType="text/html"%>
<%@ page import="model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Cameras</title>
<script type="text/javascript">
	function validate() {
		
		<c:choose>
			<c:when test="${empty selectedCamera}">
				document.getElementById('privileges').style.display = 'none';
				document.getElementById('editButton').style.display = 'none';				
			</c:when>
			<c:otherwise>
				<c:forEach var="privilege_" items="${privilegeList}">
				document.getElementById('user' + "${privilege_}").checked = true;
				</c:forEach>			
			</c:otherwise>
		</c:choose>
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
	<br>
	<p>${status}</p>
	<br>
	<table>
		<tr>
			<td valign="top">Camera-Liste
				<table id="cameratable" border="1">
					<tbody>
						<tr>
							<td>Id</td>
							<td>Name</td>
						</tr>
						<c:forEach var="camera_" items="${cameralist}">
							<tr>
								<c:choose>
									<c:when test="${selectedCamera.id == camera_.id}">										
										<td><c:out value="${camera_.id}" /></td>
										<td title="${camera_.description}"><a href="CameraServlet"><font color=red><c:out value="${camera_.name}" /></font></a></td>
									</c:when>
									<c:otherwise>
										<td><c:out value="${camera_.id}" /></td>
										<td title="${camera_.description}"><a href="CameraServlet?id=${camera_.id}&selected=${selectedCamera.id}"><c:out value="${camera_.name}" /></a></td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>														
						<tr id="editButton">
							<td colspan="3">
								<form>
									<input type="submit" value="Edit" onclick="form.action='CameraServlet?action=edit&id=${selectedCamera.id}';  form.method='post'">
									<input type="submit" value="Delete" onclick="form.action='CameraServlet?action=delete&id=${selectedCamera.id}';  form.method='post'">	
								</form>								
							</td>
						</tr>
					</tbody>
				</table>
			</td>
			<td valign="top" >									
				<div id="privileges">
					<form action="CameraServlet?action=privilege&id=${selectedCamera.id}" method="post">
						User
						<table border="1">
							<tbody>
								<tr>
									<td>Id</td>
									<td>Name</td>
									<td>Privilege</td>
								</tr>
								<c:forEach var="user_" items="${userlist}">
									<tr>
										<td><c:out value="${user_.id}"/></td>
										<td><a href="UserServlet?id=${user_.id}&selected="><c:out value="${user_.name}" /></a></td>
										<td><input type="checkbox" name="user${user_.id}"
											value="true" id="user${user_.id}"></td>
									</tr>
								</c:forEach>
								<tr>
									<td colspan=2></td>
									<td>
										<input type="submit" value="Save">
									</td>
								</tr>
							</tbody>
						</table>
					</form>					
				</div>
			</td>
		</tr>
	</table>
	<br>
	<form action="CameraServlet?action=new" method="post">
		<input type="submit" value="New Camera">
	</form>
</body>
</html>