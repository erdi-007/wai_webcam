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
<link rel="stylesheet" href="css/ImagePage.css">
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


<div id="dialog-message" title"Datum">
<div id="datepicker"></div>
<input type="text"id="time_text">
<div id="time_slider"></div>
</div>
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
<!-- 		<img src="/images/15_32.jpg">
		<img src="/images/image-1.jpg"> -->
		<fieldset>
			<legend>Bilder suche</legend>
			<form action="ImageServlet" method="get">
				
				<div class="zeile">
					<label for="dateStart">Start</label>
					<input type="text"name="dateStart"id="dateStart"value=""class="datum">
				</div>
				
				<div class="zeile">
					<label for="dateEnd">Ende</label>
					<input type="text"name="dateEnd"id="dateEnd"value=""class="datum">
				</div>
				
							
				<div class="zeile">
					<label for="cameras">Kamera</label>
				</div>
				
				<div class="zeile">	
					<select name="cameras"id="cameras">
						<c:forEach var="camera" items="${cameraList}">
							<option>${camera.id}</option>
						</c:forEach>
					</select>
				</div>
				<input type="submit"name="datum" value="Suchen">
			</form>
		</fieldset>
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
						<a class="example-image-link" href="${images.path}" data-lightbox="example-set" data-title="Click the right half of the image to move forward."><img class="example-image" src="${images.pathThumbnail}" alt=""/></a>
					</c:forEach>
				</div>
			</div>
	</div>
	</section>		
				
	<script src="jquery/jquery.js"></script>
<script src="jquery/jquery-ui.js"></script>
	<script src="jquery/lightbox.js"></script>
<script>


$("#cameras").selectmenu();  

$( "#dateStart" ).click(function(){
	$("#datepicker").datepicker('setDate','2015-06-03');
	$("#dialog-message").data("textfield",'dateStart');
	$("#dialog-message").dialog( "open" );
});

$( "#dateEnd" ).click(function(){
	$("#datepicker").datepicker('setDate','2015-06-03');
	$("#dialog-message").data("textfield",'dateEnd');
	$("#dialog-message").dialog( "open" );
});

$(function() {
    $( "#dialog-message" ).dialog({
      	autoOpen:false,
    	modal: true,
    	height:600,
    	width:500,
      	buttons: {
	        Ok: function() {
	        	var date = $("#datepicker").datepicker().val();
	        	var time = $("#time_text").val();
	        	
	        	var text = $("#dialog-message").data("textfield");
	        	$("#"+text).val(date+' '+time+':00');
	          $( this ).dialog( "close" );
	        }
      }
    });
  });

$(function() {
    $( "#datepicker" ).datepicker({
    	dateFormat:"yy-mm-dd"
    });
  });

$(function() {
$( "#time_slider" ).slider({
   orientation:"vertical",
    min: 0,
    max: 1440,
    value:  600,
    step: 15,
    slide: function( event, ui ) {
    	var hours = Math.floor(ui.value/ 60);
        var minutes = ui.value - (hours * 60);

        if(hours.length < 10) hours= '0' + hours;
        if(minutes.length < 10) minutes = '0' + minutes;

        if(minutes == 0) minutes = '00';

       $("#time_text").val(hours+':'+minutes);
    /*   $( "#timeStart" ).val(hours1+':'+minutes1 ); */
     
    }
  });
});  
/* $( "#timeStart" ).val($( "#time-range" ).slider( "values", 0 ));
$( "#timeEnd" ).val($( "#time-range" ).slider( "values", 1 )); */

</script>

</body>
</html>