﻿<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Index.aspx.cs" Inherits="SSS_WEB_APPLICATION.Index" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
<!-- Mobile Specific Meta -->
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<!-- Favicon-->
		<link rel="shortcut icon" href="img/fav.png">
		<!-- Author Meta -->
		<meta name="author" content="colorlib">
		<!-- Meta Description -->
		<meta name="description" content="">
		<!-- Meta Keyword -->
		<meta name="keywords" content="">
		<!-- meta character set -->
		<meta charset="UTF-8">
		<!-- Site Title -->
		<title>Smartphone Security System</title>

		<link href="https://fonts.googleapis.com/css?family=Poppins:100,200,400,300,500,600,700" rel="stylesheet"> 
			<!--
			CSS
			============================================= -->
			<link rel="stylesheet" href="STYLE/css/linearicons.css">
			<link rel="stylesheet" href="STYLE/css/font-awesome.min.css">
			<link rel="stylesheet" href="STYLE/css/bootstrap.css">
			<link rel="stylesheet" href="STYLE/css/magnific-popup.css">
			<link rel="stylesheet" href="STYLE/css/nice-select.css">							
			<link rel="stylesheet" href="STYLE/css/animate.min.css">
			<link rel="stylesheet" href="STYLE/css/jquery-ui.css">			
			<link rel="stylesheet" href="STYLE/css/owl.carousel.css">
			<link rel="stylesheet" href="STYLE/css/main.css">
		</head>
		<body>	
			  <header id="header" id="home">
				  <div class="header-top">
		  			<div class="container">
				  		<div class="row align-items-center">
				  			<div class="col-lg-6 col-sm-6 col-4 header-top-left no-padding">
				        		<a href="index.html"><img src="img/Capture.png" alt="" title="" /></a>			
				  			</div>
				  			
				  		</div>			  					
		  			</div>
				</div>
			    <div class="container main-menu">
			    	<div class="row align-items-center justify-content-between d-flex">
				      <nav id="nav-menu-container">
				        <ul class="nav-menu">
				          <li class="menu-active"><a href="Index.aspx">IDENTIFY USER - TESTING</a></li>
				           <li><a href="View_Users.aspx">VIEW AVAILABLE USERS</a></li>
							<li><a href="Register_User.aspx">REGISTER USER</a></li>
				          <li><a href="Upload_Image.aspx">UPLOAD IMAGE</a></li>
				         
				        </ul>
				      </nav><!-- #nav-menu-container -->
  		
			    	</div>
			    </div>
			  </header><!-- #header -->

			<!-- start banner Area -->
			<section class="banner-area relative" id="home">
				<div class="overlay overlay-bg"></div>
				<div class="container">
					<div style="height:10px" class="row fullscreen d-flex justify-content-center align-items-center">
												
					</div>
				</div>
			</section>
			<!-- End banner Area -->


			
						<!-- Start open-hour Area -->
			<section style="margin-top:-400px" class="open-hour-area">
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-lg-12 open-hour-wrap">
							<h1>Traning Images</h1><br/><br/>
							<div id="div" runat="server"></div>
						</div>
					</div>
				</div>	
			</section>
			<!-- End open-hour Area -->
			


			<script src="STYLE/js/vendor/jquery-2.2.4.min.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
			<script src="STYLE/js/vendor/bootstrap.min.js"></script>			
			<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBhOdIF3Y9382fqJYt5I_sswSrEw5eihAA"></script>
  			<script src="STYLE/js/easing.min.js"></script>			
			<script src="STYLE/js/hoverIntent.js"></script>
			<script src="STYLE/js/superfish.min.js"></script>	
			<script src="STYLE/js/jquery.ajaxchimp.min.js"></script>
			<script src="STYLE/js/jquery.magnific-popup.min.js"></script>	
 			<script src="STYLE/js/jquery-ui.js"></script>			
			<script src="STYLE/js/owl.carousel.min.js"></script>						
			<script src="STYLE/js/jquery.nice-select.min.js"></script>							
			<script src="STYLE/js/mail-script.js"></script>	
			<script src="STYLE/js/main.js"></script>	
		</body>
</html>