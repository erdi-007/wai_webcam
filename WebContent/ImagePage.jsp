<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII" import="model.User"%>
<%@ page import="model.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/jquery-ui.css">
<link rel="stylesheet" href="css/screen.css">
<link rel="stylesheet" href="css/lightbox.css">
<%
	User userinfo = (User) (session.getAttribute("userinfo"));
%>

<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>User Logged Successfully</title>
<script type="text/javascript">
	function validate() {
		if (<%=userinfo.isAdmin()%> == true) {
			document.getElementById('settings').style.display = 'block';
		} else if (<%=userinfo.isAdmin()%> == false) {
			document.getElementById('settings').style.display = 'none';
		}
	}
</script>
</head>
<body onload="validate()">
	<%
		//allow access only if session exists
		String user = null;
		if (session.getAttribute("userinfo") == null) {
			response.sendRedirect("login.html");
		} else
			user = (String) session.getAttribute("user");
		String userName = null;
		String sessionID = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user"))
					userName = cookie.getValue();
				if (cookie.getName().equals("JSESSIONID"))
					sessionID = cookie.getValue();
			}
		}
	%>
	<center>
		Name:
		<%=userinfo.getName()%>
		<br> Admin-Status:
		<%=userinfo.isAdmin()%>
		<br> <br>
		
		<form action="ImageServlet" method="get">
			Datum: <input type="text"name="dateStart"id="dateStart"value="">bis <input type="text"name="dateEnd"id="dateEnd"value=""><br>
			
			Uhrzeit: <input type="text" id="timeStart" name="timeStart"readonly value="10:00">
			<input type="text"id="timeEnd" name="timeEnd"readonly value="20:00"> <br>
			<div id="time-range"></div> 
			
			<input type="submit"name="datum" value="Suchen">
		</form>
		
		<c:forEach var="images" items="${imageList}">
			<img src="${images.pathThumbnail}">
		</c:forEach>
		
		<br> <br>
		<table>
			<tr>
				<td>
					<form action="ImageServlet" method="get">
						<input type="submit" name ="image"value="Image">
					</form>
				</td>
				<td>
					<div id="settings">
						<form action="UserServlet" method="get">
							<input type="submit" value="Settings">
						</form>
					</div>
				</td>
				<td>
					<form action="LogoutServlet" method="get">
						<input type="submit" value="Logout">
					</form>
				</td>
			</tr>
		</table>
	</center>

<section id="examples" class="examples-section">
<div class="container">
			<div class="image-row">
				<div class="image-set">
					<c:forEach var="images" items="${imageList}">
						<a class="example-image-link" href="images.path" data-lightbox="example-set" data-title="Click the right half of the image to move forward."><img class="example-image" src="img/demopage/thumb-3.jpg" alt=""/></a>
					</c:forEach>
				</div>
			</div>
	</div>
	</section>		
				
	<script src="jquery/jquery.js"></script>
<script src="jquery/jquery-ui.js"></script>
	<script src="jquery/lightbox.js"></script>
<script>
$( "#dateStart" ).datepicker({
	dateFormat: "yy-mm-dd"
});
$( "#dateEnd" ).datepicker({
	dateFormat: "yy-mm-dd"
});
$( "#time-range" ).slider({
    range: true,
    min: 0,
    max: 1440,
    values: [ 600, 1200 ],
    step: 15,
    slide: function( event, ui ) {
    	var hours1 = Math.floor(ui.values[0] / 60);
        var minutes1 = ui.values[0] - (hours1 * 60);

        if(hours1.length < 10) hours1= '0' + hours;
        if(minutes1.length < 10) minutes1 = '0' + minutes;

        if(minutes1 == 0) minutes1 = '00';

        var hours2 = Math.floor(ui.values[1] / 60);
        var minutes2 = ui.values[1] - (hours2 * 60);

        if(hours2.length < 10) hours2= '0' + hours;
        if(minutes2.length < 10) minutes2 = '0' + minutes;

        if(minutes2 == 0) minutes2 = '00';
      $( "#timeStart" ).val(hours1+':'+minutes1 );
      $("#timeEnd").val(hours2+":"+minutes2);
    }
  });
  
/* $( "#timeStart" ).val($( "#time-range" ).slider( "values", 0 ));
$( "#timeEnd" ).val($( "#time-range" ).slider( "values", 1 )); */

</script>

</body>
</html>