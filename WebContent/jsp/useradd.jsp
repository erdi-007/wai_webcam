<%@ page import="model.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>User</title>
  </head>  
  <body>
	<form name="useredit" action="useredit" method="post">		
		<table border="1">
			<tbody>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name" value=""></td>		
				</tr>
				<tr>		
					<td>Admin:</td>	
					<td><input type="checkbox" name="admin" value="true"></td>
				</tr>	
				<tr>		
					<td>Passwort:</td>	
					<td><input type="text" name="password" value=""></td>
				</tr>					
				<tr>	
					<td colspan="2"><input type="submit" name="btnSave" value="Save"></td>
				</tr>				
			</tbody>
		</table>
	</form>
  </body>
</html>
