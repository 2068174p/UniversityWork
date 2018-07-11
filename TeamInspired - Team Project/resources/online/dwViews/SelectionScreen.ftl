<html>

	<head>
		<!-- Web page title -->
    	<title>Top Trumps</title>
    	
       	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


	</head>

    <body onload="initalize()"> 
    	
    	<div class="container">

			<h1>Welcome to Top Card Game</h1>
			<br>
			Please Choose :
			<br><br>
			<button onclick="resetGame()" type="button" class="btn btn-primary"  style="width:40%">Play</button>
			<br><br>
			<button onclick="gameStats()" type="button" class="btn btn-success" style="width:40%">Statistic</button>
			<br><br>
			<img src="http://localhost:7777/toptrumps/getImage?image=mainbg" style="background-repeat:no-repeat">
		</div>
		
		<script type="text/javascript">
		
			// Method that is called on page load
			function initalize() {
			
			}
			
			// -----------------------------------------
			// Add your other Javascript methods Here
			// -----------------------------------------
		
			// This is a reusable method for creating a CORS request. Do not edit this.
			function createCORSRequest(method, url) {
  				var xhr = new XMLHttpRequest();
  				if ("withCredentials" in xhr) {

    				// Check if the XMLHttpRequest object has a "withCredentials" property.
    				// "withCredentials" only exists on XMLHTTPRequest2 objects.
    				xhr.open(method, url, true);

  				} else if (typeof XDomainRequest != "undefined") {

    				// Otherwise, check if XDomainRequest.
    				// XDomainRequest only exists in IE, and is IE's way of making CORS requests.
    				xhr = new XDomainRequest();
    				xhr.open(method, url);

 				 } else {

    				// Otherwise, CORS is not supported by the browser.
    				xhr = null;

  				 }
  				 return xhr;
			}
			
			function resetGame() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/createGame"); 
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
					window.location.replace("http://localhost:7777/toptrumps/game?id=" + xhr.response);
				};
				
				// Send CORS request
				xhr.send();
			}
			
			function gameStats() {
				window.location.replace("http://localhost:7777/toptrumps/stats?option=menu");
			}
		
		</script>
		
		</body>
</html>