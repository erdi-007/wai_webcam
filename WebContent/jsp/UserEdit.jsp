<%@ page import="model.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>User</title>    
	<script type="text/javascript">
    function validate() {
        if (document.getElementById('admin_state').value == "true") {
        	document.getElementById('admin').checked = true;
        } else {
        	document.getElementById('admin').checked = false;
        }
    }
    </script>
  </head>  
  <body onload="validate()">
	<form name="useredit" action="useredit" method="post">		
		<table border="1">
			<tbody>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name" value="${user.name}"></td>		
				</tr>
				<tr>		
					<td>Admin:</td>						
					<td><input type="checkbox" name="admin" value="true" id="admin"></td>
				</tr>	
				<tr>		
					<td>Passwort:</td>	
					<td><input type="text" name="password" value="${user.password}"></td>
				</tr>					
				<tr>	
					<td colspan="2"><input type="submit" name="btnSave" value="Save"></td>
				</tr>				
			</tbody>
		</table>
		<input type="hidden" name="id" value="${user.id}">
		<input type="hidden" id="admin_state" value="${user.admin}">
	</form>
  </body>
</html>
