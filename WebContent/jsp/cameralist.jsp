<%@ page language="java" contentType="text/html" %>
<%@ page import="model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>      
    <title>Kamera-Liste</title>     
  </head>
  <body>
  	<form name="show" action="show" method="post">
	  	<table border="1">
	  		<tbody>
		  		<tr>
		  			<td>Id</td>				
					<td>Name</td>
					<td>Description</td>
					<td>Url</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>			
				<c:forEach var="camera" items="${cameras}">
					<tr>
						<td><c:out value="${camera.id}"/></td>					
						<td><c:out value="${camera.name}"/></td>
						<td><c:out value="${camera.description}"/></td>
						<td><c:out value="${camera.url}"/></td>
						<td><a href="cameraedit?action=cameraedit&id=${camera.id}">Ändern</a></td>
						<td><a href="cameraedit?action=cameradelete&id=${camera.id}">Löschen</a></td>
					</tr>
				</c:forEach>	
	  		</tbody>
	  	</table>
	  </form>
  	<br>
  	<a href="cameraedit?action=cameraadd">Neues Kamera hinzufügen</a>  	
  	<br> 	
  	<br>
  	<a href="cameraedit?action=imagesadd">Neue Bilder hinzufügen</a>
  </body>
</html>
