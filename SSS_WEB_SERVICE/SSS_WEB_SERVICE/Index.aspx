<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Index.aspx.cs" Inherits="SSS_WEB_SERVICE.Index" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" lang="zxx" class="no-js">
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
			<link rel="stylesheet" href="css/linearicons.css">
			<link rel="stylesheet" href="css/font-awesome.min.css">
			<link rel="stylesheet" href="css/bootstrap.css">
			<link rel="stylesheet" href="css/magnific-popup.css">
			<link rel="stylesheet" href="css/nice-select.css">							
			<link rel="stylesheet" href="css/animate.min.css">
			<link rel="stylesheet" href="css/jquery-ui.css">			
			<link rel="stylesheet" href="css/owl.carousel.css">
			<link rel="stylesheet" href="css/main.css">
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
				<div style="margin-top:-50px"  class="container">
					<div class="row fullscreen d-flex justify-content-center align-items-center">
						<div class="banner-content col-lg-9 col-md-12 justify-content-center">
							    <form id="form1" runat="server">
									<div>
										<asp:FileUpload ID="FileUpload1" style="width:500px" runat="server" />
										<asp:Button style="width:500px" class="primary-btn header-btn text-uppercase mt-10" ID="Button1" runat="server" OnClick="Button1_Click" Text="IDENTIFY USER" />
									</div>
								</form>
							
						</div>											
					</div>
				</div>
			</section>
			<!-- End banner Area -->

			<!-- Start open-hour Area -->
			<section style="margin-top:-200px"  class="open-hour-area">
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-lg-12 open-hour-wrap">
							<h1>IDENTIFIED USER</h1><br/>
							<h3 runat="server" id="User_Name"> 
								
							</h3><br/>
							<div runat="server" id="img"></div>
						</div>
					</div>
				</div>	
			</section>
			<!-- End open-hour Area -->
			
			
				


			<script src="js/vendor/jquery-2.2.4.min.js"></script>
			<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
			<script src="js/vendor/bootstrap.min.js"></script>			
			<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBhOdIF3Y9382fqJYt5I_sswSrEw5eihAA"></script>
  			<script src="js/easing.min.js"></script>			
			<script src="js/hoverIntent.js"></script>
			<script src="js/superfish.min.js"></script>	
			<script src="js/jquery.ajaxchimp.min.js"></script>
			<script src="js/jquery.magnific-popup.min.js"></script>	
 			<script src="js/jquery-ui.js"></script>			
			<script src="js/owl.carousel.min.js"></script>						
			<script src="js/jquery.nice-select.min.js"></script>							
			<script src="js/mail-script.js"></script>	
			<script src="js/main.js"></script>	
		</body>
	</html>