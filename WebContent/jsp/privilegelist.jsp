<%@ page language="java" contentType="text/html" %>
<%@ page import="model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>      
    <title>Privilege-Liste</title>     
  </head>
  <body>
  	<form name="show" action="show" method="post">
	  	<table border="1">
	  		<tbody>
		  		<tr>
		  			<td>BenutzerID</td>				
					<td>KameraID</td>
					<td>&nbsp;</td>
				</tr>			
				<c:forEach var="privilege" items="${privileges}">
					<tr>
						<td><c:out value="${privilege.userId}"/></td>					
						<td><c:out value="${privilege.cameraId}"/></td>
						<td><a href="privilegeedit?action=privilegedelete&userid=${privilege.userId}&cameraid=${privilege.cameraId}">Löschen</a></td>
					</tr>
				</c:forEach>
	  		</tbody>
	  	</table>
	  </form>
  </body>
</html>
