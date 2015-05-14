<%@ page import="model.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>Kamera</title>
  </head>  
  <body>
	<form name="edit" action="edit" method="post">		
		<table border="1">
			<tbody>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name" value="${kamera.name}"></td>		
				</tr>
				<tr>		
					<td>URL:</td>	
					<td><input type="text" name="url" value="${kamera.url}"></td>
				</tr>
				<tr>		
					<td>Pfad:</td>	
					<td><input type="text" name="pfad" value="${kamera.pfad}"></td>
				</tr>	
				<tr>		
					<td>Beschreibung:</td>	
					<td><input type="text" name="beschreibung" value="${kamera.beschreibung}"></td>
				</tr>					
				<tr>	
					<td colspan="2"><input type="submit" name="btnSave" value="Save"></td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" name="id" value="${kamera.id}">
	</form>
  </body>
</html>
