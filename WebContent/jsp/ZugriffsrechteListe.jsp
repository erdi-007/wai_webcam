<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>     
    <title>Zugriffsrechte</title>
  </head>
  <body>
		<table border="1">
			<thead>
				<tr>
					<th>User ID:</th>
					<th>Kamera ID:</th>
					<th>&nbsp;</th>
					<th>&nbsp;</th>	
				</tr>					
			</thead>
			
			<tbody>
			<c:forEach var="zugriffsrechte" items="${zugriffsrechte}">
				<tr>
					
					<td><c:out value="${user.id}" /></td>		
					
					
					<td><c:out value="${kamera.id}"/></td>		
							
								
					<td><a> href="edit?action=edit&id=${kamera.id}">Ändern</a></td>
					
					
				</tr>
			</c:forEach>	
			</tbody>
			
			<tfoot>
				<tr>
					<td colspan="3"><a href="edit?action=add">Neue Zugriffsrechte</a></td>
				</tr>	
			</tfoot>	
		</table>
  </body>
</html>

