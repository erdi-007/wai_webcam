<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>     
    <title>User</title>
  </head>
  <body>
		<table border="1">
			<thead>
				<tr>
					<th>ID:</th>
					<th>Name:</th>
					<th>Password:</th>					
					<th>Admin:</th>
					<th>&nbsp;</th>
					<th>&nbsp;</th>	
				</tr>					
			</thead>
			
			<tbody>
			<c:forEach var="user" items="${users}">
				<tr>
					
					<td><c:out value="${user.id}" /></td>		
					
					
					<td><c:out value="${user.name}"/></td>		
							
						
					<td><c:out value="${user.password}"/></td>
					
						
					<td><c:out value="${user.admin}"/></td>
					
						
								
					<td><a> href="edit?action=edit&id=${user.id}">Ändern</a></td>
					
					
				</tr>
			</c:forEach>	
			</tbody>
			
			<tfoot>
				<tr>
					<td colspan="5"><a href="edit?action=add">Neuen User hinzufügen</a></td>
				</tr>	
			</tfoot>	
		</table>
  </body>
</html>

