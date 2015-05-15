<%@ page import="model.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>Listen</title>
  </head>  
  <body>		
	<table border="1">
		<tbody>
			<tr>
				<td>Kamera-Liste:</td>
				<td><a href="cameralist?action=show">zeigen</a></td>		
			</tr>
			<tr>		
				<td>User-Liste:</td>
				<td><a href="userlist?action=show">zeigen</a></td>	
			</tr>
			<tr>		
				<td>Privilege-Liste:</td>
				<td><a href="privilegelist?action=show">zeigen</a></td>	
			</tr>				
		</tbody>
	</table>
  </body>
</html>
