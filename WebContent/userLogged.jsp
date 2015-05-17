<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256" import="model.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<% User currentUser = (User) (session.getAttribute("currentSessionUser"));%>
		
		<meta http-equiv="Content-Type"
			content="text/html; charset=windows-1256">
		<title>User Logged Successfully</title>
		<script type="text/javascript">
		    function validate() {
		        if (<%=currentUser.isAdmin() %> == true) {
		        	document.getElementById('settings').style.display = 'block';
		        } else if (<%=currentUser.isAdmin() %> == false){
		        	document.getElementById('settings').style.display = 'none';
		        }
		    }
	    </script>
	</head>
	<body onload="validate()">
		<center>
			Name: <%=currentUser.getName()%>
			<br>
			Admin-Status: <%=currentUser.isAdmin() %>
			<br>
			<br>
			<div id="settings">
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
			</div>
		</center>
	</body>
</html>