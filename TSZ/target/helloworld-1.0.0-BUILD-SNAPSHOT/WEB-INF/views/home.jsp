<%-- 
    Document   : index
    Created on : 2016-03-01, 21:01:02
    Author     : radon
--%>

<%@page contentType="text/html" pageEncoding="windows-1250"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pl">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="assets/img/favicon.ico">

    <title>Projekt</title>

    <!-- Bootstrap core CSS -->
    <link href="resources/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="bootstrap/assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="resources/css/main.css" rel="stylesheet" type="text/css">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="bootstrap/assets/js/ie-emulation-modes-warning.js"></script>
    
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Projekt</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">Action</a></li>
                <li><a href="#">Another action</a></li>
                <li><a href="#">Something else here</a></li>
                <li role="separator" class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href="#">Separated link</a></li>
                <li><a href="#">One more separated link</a></li>
              </ul>
            </li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><a href="#">Default</a></li>
            <li><a href="#">Static top</a></li>
            <li class="active"><a href="#">Fixed top <span class="sr-only">(current)</span></a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container-fluid">
		<div class="row">
		
			<div class="col-xs-12 col-sm-4 sidebar">

				<div class="row">
					<input class="btn btn-default pull-right sign-out" type="button" value="Sign Out">
				</div>
			
				<div class="row">
					<div class="col-xs-12">
						<ul class="list-unstyled profile-info">
							<li><img class="img-circle img-responsive center-block" src="data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==" alt="Generic placeholder image" width="140" height="140"></li>
							<li class="text-center"><h3>${name} ${surname}</h3></li>
							<li class="school-address">
								<address>
									<strong>Gimnazjum nr 1</strong><br>
									ul. Adres ulicy 12, Toruñ<br>
									woj. Kujawsko-Pomorskie<br>
								</address>
							</li>
							<li class="email-address">
								<address>
									<strong>Adres email</strong><br>
									<a href="mailto:michal.kapron@gmail.com">michal.kapron@gmail.com</a>
								</address>
							</li>
						</ul>
					</div>
				</div>

			</div>
			
			<div class="col-xs-12 col-sm-8 col-sm-offset-4 main">
				
				<div class="row">
					<div class="col-xs-12 suggestion-title"><h2><small>Nasze propozycje szkó³:</small></h2></div>
				</div>
				
				<div class="row">
					<div class="col-xs-12">
						<select multiple class="form-control suggestion-list">
							<option>1. VII Liceum Ogólnokszta³c¹ce im. Wandy Szuman</option>
							<option>2. III Liceum Ogólnokszta³c¹ce im. S.B.Lindego</option>
							<option>3. VIII Liceum Ogólnokszta³c¹ce</option>
							<option>4. Ogólnokszta³c¹ca Szko³a Muzyczna II Stopnia</option>
							<option>5. II Liceum Ogólnokszta³c¹ce im. Królowej Jadwigi</option>
						</select>
					</div>
				</div>
				
			</div>
			
		</div>
    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>');</script>
    <script src="bootstrap/dist/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="bootstrap/assets/js/ie10-viewport-bug-workaround.js"></script>
  </body>
</html>
