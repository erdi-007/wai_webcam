<%@ page language="java" contentType="text/html" %>
<%@ page import="model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=windows-1256">
		<title>Login Page</title>
	</head>
	<body>
		<form action="LoginServlet">
			<center>
			 	<table>
			 	<tr>
				 	<td>Username</td>
				 	<td><input type="text" name="un" /></td>	
				</tr>
			 	<tr>				
					<td>Password</td>
					<td><input type="password" name="pw" /></td>
				</tr>
			 	<tr>
			 		<td></td>
					<td><input type="submit" value="submit" /></td>
				</tr>
				</table>
			</center>
		</form>
	</body>
</html>