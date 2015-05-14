<%@ page language="java" contentType="text/html" %>
<%@ page import="model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>      
    <title>Bibliothek</title>     
  </head>
  <body>
  	<table border="1">
  		<tbody>
	  		<tr>
	  			<td>Id</td>				
				<td>Titel</td>
				<td>Autor</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>			
			<c:forEach var="book" items="${books}">
				<tr>
					<td><c:out value="${book.id}"/></td>					
					<td><c:out value="${book.title}"/></td>
					<td><c:out value="${book.author}"/></td>
					<td><a href="edit?action=edit&id=${book.id}">Ändern</a></td>
					<td><a href="edit?action=delete&id=${book.id}">Löschen</a></td>
				</tr>
			</c:forEach>	
  		</tbody>
  	</table>
  	<br>
  	<a href="edit?action=add">Neues Buch hinzufügen</a>
  </body>
</html>
