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
				<li><a href="ImagePage.jsp">Images</a></li>
				<li><a href="UserServlet">User</a></li>
				<li><a href="CameraServlet">Cameras</a></li>
				<li><a href="LogoutServlet">Logout</a></li>
				
			</ul>
		</div>
	</nav>
		<table>
			<tr>
				<td valign="top">User-Liste
					<table id="usertable" border="1">
						<tbody>
							<tr>
								<td>Id</td>
								<td>Name</td>
								<td>Admin</td>
							</tr>
							<c:forEach var="user_" items="${userlist}">
								<tr>
									<c:choose>
										<c:when test="${selectedUser.name == user_.name}">										
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
								<td colspan=2></td>
								<td>
									<form action="UserEdit.jsp" method="post">
										<input type="submit" value="Edit">
									</form>
								</td>
							</tr>
						</tbody>
					</table>
					<table>
						<tr>
							<td>					

							</td>
							<td>						

							</td>
						</tr>
					</table>
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
								<tr>
									<td colspan=2></td>
									<td>					
										<form action="UserEdit.jsp" method="post">
											<input type="submit" value="Save">
										</form>
									</td>
								</tr>
							</tbody>
						</table>					
					</div>
				</td>
			</tr>
		</table>
		
		<form action="UserEdit.jsp" method="post">
			<input type="submit" value="New User">
		</form>
</body>
</html>