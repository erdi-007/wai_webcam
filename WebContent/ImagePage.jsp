<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="model.User"%>
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
			document.getElementById('menu1').style.display = 'block';
		} else if (<%=userinfo.isAdmin()%> == false) {
			document.getElementById('menu1').style.display = 'none';
		}
	}
	
	 
</script>
</head>
<body onload="validate()">


<div id="dialog-message" title="Datum">
	<div id="datepicker"></div>
	<div id="time_div">
		<input type="text"id="time_text" value="10:00">
		<div id="time_slider"></div>
	</div>
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

<div id="leftside">
	
	<div id="head">
		Name:
		<%=userinfo.getName()%>
		<br> Admin-Status:
		<%=userinfo.isAdmin()%>
	</div>


		
					<div id="menu">
					
					<div id="menu1">
						<form action="UserServlet" method="get">
							<input type="submit" value="Settings" class="button">
						</form>
					</div>
				
					<div id=menu2>
					<form action="LogoutServlet" method="get">
						<input type="submit" value="Logout" class="button">
					</form>
					</div>
				</div>
				
				
		<fieldset>
			<legend>Bilder suche</legend>
			<form action="ImageServlet" method="get">
				
				<div class="row">
					<label for="dateStart">Start</label>
				</div>	
				<div class="row">	
					<input type="text"name="dateStart"id="dateStart"value="${dateStart}" required>
				</div>
				
				<div class="row">
					<label for="dateEnd">Ende</label>
				</div>	
				<div class="row">	
					<input type="text"name="dateEnd"id="dateEnd"value="${dateEnd }"required>
				</div>
				
							
				<div class="row">
					<label for="cameras">Kamera</label>
				</div>
				
				<div class="row">	
					<select name="cameras" id="cameras">
						<c:forEach var="camera" items="${cameraList}">
							<option>
								${camera.id}: ${camera.name}
							</option>
						</c:forEach>
					</select>
				</div>
				<input type="submit"name="date" value="Suchen" class="button" id="fieldButton">
				<input type="hidden"name="loggeduser" value="<%=userinfo.getId()%>">
			</form>
		</fieldset>
		
</div>
	
<div id=images>	

					<c:forEach var="images" items="${imageList}">
						<a class="example-image-link" href="${images.path}" data-lightbox="example-set" data-title="Navigieren über die Pfeile oder Tastatur"><img class="example-image" src="${images.pathThumbnail}" alt="Bild nicht gefunden"/></a>
					
					</c:forEach>
			
</div>				
	<script src="jquery/jquery.js"></script>
	<script src="jquery/jquery-ui.js"></script>
	<script src="jquery/lightbox.js"></script>
<script>


$("#cameras").selectmenu();  

$( "#dateStart" ).click(function(){
	$("#datepicker").datepicker('setDate',new Date());
	$("#dialog-message").data("textfield",'dateStart');
	$("#dialog-message").dialog( "open" );
});

$( "#dateEnd" ).click(function(){
	$("#datepicker").datepicker('setDate',new Date());
	$("#dialog-message").data("textfield",'dateEnd');
	$("#dialog-message").dialog( "open" );
});


    $( "#dialog-message" ).dialog({
      	autoOpen:false,
    	modal: true,
    	height:460,
    	width:500,
    	closeOnEscape:true,
      	buttons: {
	        Ok: function() {
	        	var date = $("#datepicker").datepicker().val();
	        	var time = $("#time_text").val();
	        	
	        	var text = $("#dialog-message").data("textfield");
	        	$("#"+text).val(date+' '+time+':00');
	          $( this ).dialog( "close" );
	        },
    		Abbrechen:function() {
    		  $( this ).dialog( "close" );	
    		}
      }
    });



    $( "#datepicker" ).datepicker({
    	dateFormat:"yy-mm-dd"
    	
    });



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
 


	  $(".ui-dialog-titlebar").hide();    

</script>

</body>
</html>