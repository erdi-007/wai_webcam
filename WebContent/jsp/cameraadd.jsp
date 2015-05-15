<%@ page import="model.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>Kamera</title>
  </head>  
  <body>
	<form name="cameraedit" action="cameraedit" method="post">		
		<table border="1">
			<tbody>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name" value=""></td>		
				</tr>
				<tr>		
					<td>Beschreibung:</td>	
					<td><input type="text" name="description" value=""></td>
				</tr>	
				<tr>		
					<td>Url:</td>	
					<td><input type="text" name="url" value=""></td>
				</tr>					
				<tr>	
					<td colspan="2"><input type="submit" name="btnSave" value="Save"></td>
				</tr>				
			</tbody>
		</table>
	</form>
  </body>
</html>
