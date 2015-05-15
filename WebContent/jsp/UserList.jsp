<%@ page language="java" contentType="text/html" %>
<%@ page import="model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>      
    <title>User-Liste</title>     
  </head>
  <body>
  	<form name="show" action="show" method="post">
	  	<table border="1">
	  		<tbody>
		  		<tr>
		  			<td>Id</td>				
					<td>Name</td>
					<td>Admin</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>			
				<c:forEach var="user" items="${user}">
					<tr>
						<td><c:out value="${user.id}"/></td>					
						<td><c:out value="${user.name}"/></td>
						<td><c:out value="${user.admin}"/></td>
						<td><a href="useredit?action=useredit&id=${user.id}">Ändern</a></td>
						<td><a href="useredit?action=userdelete&id=${user.id}">Löschen</a></td>
					</tr>
				</c:forEach>	
	  		</tbody>
	  	</table>
  	</form>
  	<br>
  	<a href="useredit?action=useradd">Neuer User hinzufügen</a>
  </body>
</html>
