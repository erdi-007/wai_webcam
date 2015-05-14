<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>     
    <title>Kamera</title>
  </head>
  <body>
		<table border="1">
			<thead>
				<tr>
					<th>ID:</th>
					<th>Name:</th>
					<th>URL:</th>					
					<th>Pfad:</th>
					<th>Beschreibung:</th>
					<th>&nbsp;</th>
					<th>&nbsp;</th>	
				</tr>					
			</thead>
			
			<tbody>
			<c:forEach var="kamera" items="${kameras}">
				<tr>
					
					<td><c:out value="${kamera.id}" /></td>		
					
					
					<td><c:out value="${kamera.name}"/></td>		
							
						
					<td><c:out value="${kamera.url}"/></td>
					
						
					<td><c:out value="${kamera.pfad}"/></td>
					
						
					<td><c:out value="${kamera.beschreibung}"/></td>
								
					<td><a> href="edit?action=edit&id=${kamera.id}">Ändern</a></td>
					
					
				</tr>
			</c:forEach>	
			</tbody>
			
			<tfoot>
				<tr>
					<td colspan="5"><a href="edit?action=add">Neue Kamera anlegen</a></td>
				</tr>	
			</tfoot>	
		</table>
  </body>
</html>

