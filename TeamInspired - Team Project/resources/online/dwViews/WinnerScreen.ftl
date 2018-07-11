<html>

	<head>
		<!-- Web page title -->
    	<title>Top Trumps</title>
    	
    	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

	</head>

    <body onload="initalize()" background="http://localhost:7777/toptrumps/getImage?image=winner" style="background-size: cover"> 
    	<div class="container">
			<br>
			<h1 style="text-align: center;color: Red">...Congratulations...</h1>	
			<h1 style="text-align: center;color: Red" id="winner"></h1>	
			<br><br><br>		
								
			<button onclick="resetGame()" type="button" class="btn btn-success">New Game</button>
			<button onclick="gameStats()" type="button" class="btn btn-warning">Statistic</button>
			
		</div>
		
		<script type="text/javascript">

			var id;
			
			$.urlParam = function(name){
				var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
				return results[1] || 0;
			}
			
			id = $.urlParam('id');

			// Method that is called on page load
			function initalize() {
			
				gameResult();			
			}
			
		
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
			
			function gameResult() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/gameResult?id=" + id + "&option=-1");
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
 					var response = JSON.parse(xhr.response);
					document.getElementById("winner").innerHTML = "The winner is " + response[1];
 					
				};
				
				// Send CORS request
				xhr.send();		
			}
			
			function resetGame() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/resetGame?id=" + id + "&option=new"); 
				
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
				window.location.replace("http://localhost:7777/toptrumps/stats?id=" + id + "&option=stats");
			}

		</script>
		
	</body>
</html>