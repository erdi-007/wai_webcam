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
<title>User</title>
<script type="text/javascript">
	function validate() {
		
		<c:choose>
			<c:when test="${empty selectedUser}">
				document.getElementById('privileges').style.display = 'none';
				document.getElementById('editButton').style.display = 'none';				
			</c:when>
			<c:otherwise>
				<c:forEach var="privilege_" items="${privilegeList}">
				document.getElementById('cam' + "${privilege_}").checked = true;
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
				<li><a href="ImageServlet?loggeduser=<%=userinfo.getId()%>">Images</a></li>
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
			<td valign="top" id="thead">User-Liste
			
				<table id="usertable">
				
				<tbody>	
						<tr>
							<th>Id</th>
							<th>Name</th>
							<th>Admin</th>
						</tr>
					
					
						<c:forEach var="user_" items="${userlist}">
							<tr>
								<c:choose>
									<c:when test="${selectedUser.id == user_.id}">										
										<td><c:out value="${user_.id}" /></td>
										<td><a href="UserServlet"><font color=red><c:out value="${user_.name}" /></font></a></td>
										<td><c:out value="${user_.admin}" /></td>
									</c:when>
									<c:otherwise>
										<td><c:out value="${user_.id}" /></td>
										<td><a href="UserServlet?id=${user_.id}&selected=${selectedUser.id}"><c:out value="${user_.name}" /></a></td>
										<td><c:out value="${user_.admin}" /></td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>														
						<tr id="editButton">
							<td colspan="3">
								<form>
									<input type="submit" value="Edit" onclick="form.action='UserServlet?action=edit&id=${selectedUser.id}';  form.method='post'"class="button">
									<input type="submit" value="Delete" onclick="form.action='UserServlet?action=delete&id=${selectedUser.id}';  form.method='post'" class="button">	
								</form>								
							</td>
						</tr>
					</tbody>
				</table>
			</td>
			<td valign="top" >									
				<div id="privileges">
					<form action="UserServlet?action=privilege&id=${selectedUser.id}" method="post">
						Cameras
						<table id="cameratable">
							<tbody>
								<tr>
									<th>Id</th>
									<th>Name</th>
									<th>Privilege</th>
								</tr>
								<c:forEach var="camera_" items="${cameralist}">
									<tr>
										<td><c:out value="${camera_.id}"/></td>
										<td title="${camera_.description}"><a href="CameraServlet?id=${camera_.id}&selected="><c:out value="${camera_.name}" /></a></td>
										<td><input type="checkbox" name="cam${camera_.id}"
											value="true" id="cam${camera_.id}"></td>
									</tr>
								</c:forEach>
								<tr>
									<td colspan=2></td>
									<td>
										<input type="submit" value="Save" class="button">
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
	<form action="UserServlet?action=new" method="post">
		<input type="submit" value="New User" class="button">
	</form>
</body>
</html>