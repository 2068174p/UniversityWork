<html>

	<head>
		<!-- Web page title -->
    	<title>Top Game Results</title>
    	
       	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	</head>

    <body onload="initalize()" background="http://localhost:7777/toptrumps/getImage?image=bg2" style="background-repeat:no-repeat;background-size: cover"> 
    	
    	<div class="container">

			<br>
			<p style="text-align:center;"><img src="http://localhost:7777/toptrumps/getImage?image=result" width="500" height="100"></p>
				
			<label id="status"></label> <br>
			<label id="communalCard"></label>
		
				<div class="row">
					<div class="col-sm-2" id="divinfo">
				     <div class="panel panel-info">
				      <div class="panel-heading" align="center" style="background-color:#ffa64d"><b>Round Info</b></div>
				      <ul class="list-group list-group-flush"></ul>
				    </div>
				    </div>
				
				    <div class="col-sm-2" id="div0">
				     <div class="panel panel-info">
				      <div class="panel-heading" align="center" style="background-color:#66ff33"><b id="p0"></b></div>
				      <ul class="list-group list-group-flush"></ul>
				    </div>
				    </div>
				    
				    <div class="col-sm-2" id="div1">
				      <div class="panel panel-info">
				      <div class="panel-heading" align="center" style="background-color:#66ff33"><b id="p1"></b></div>
				      <ul class="list-group list-group-flush"></ul>
				    </div>
				    </div>
				    
				    <div class="col-sm-2" id="div2">
				     <div class="panel panel-info">
				      <div class="panel-heading" align="center" style="background-color:#66ff33"><b id="p2"></b></div>
				      <ul class="list-group list-group-flush"></ul>
				    </div>
				    </div>
				    
				    <div class="col-sm-2" id="div3">
				      <div class="panel panel-info">
				      <div class="panel-heading" align="center" style="background-color:#66ff33"><b id="p3"></b></div>
				      <ul class="list-group list-group-flush"></ul>
				    </div>
				    </div>
				    
				    <div class="col-sm-2" id="div4">
				     <div class="panel panel-info">
				      <div class="panel-heading" align="center" style="background-color:#66ff33"><b id="p4"></b></div>
				      <ul class="list-group list-group-flush"></ul>
				    </div>
				    </div>
				    
				</div>
				
			    
			<br>		
			<button onclick="nextRound()" type="button" class="btn btn-danger">Next</button>			
		
		</div>
		
		<script type="text/javascript">
			
			var option,id;
			
			$.urlParam = function(name){
				var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
				return results[1] || 0;
			}
			
			option = $.urlParam('option');
			id = $.urlParam('id');

			// Method that is called on page load
			function initalize() {
			
				playGame();				
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
			
			function playGame() {
			
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/playGame?id=" + id + "&option=" + option);
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}
				
				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
					gameResult();
 				}
				
				// Send CORS request
				xhr.send();
			}
			
			function gameResult() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/gameResult?id=" + id + "&option=" + option);
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
 					var response = JSON.parse(xhr.response);
 					
					if (response[0] == "true")
						$("#divinfo ul").append('<li class="list-group-item" style="background-color:#ffff99">DRAW</li>');
 					else
 						$("#divinfo ul").append('<li class="list-group-item" style="background-color:#ffff99">WIN by '+ response[1] +'</li>');
					
 					$("#divinfo ul").append('<li class="list-group-item" style="background-color:#ffff99">Communal Card : '+ response[2] + '</li>');
 					$("#divinfo ul").append('<li class="list-group-item" style="background-color:#ffff99">Criteria : '+ response[3] + '</li>');
 					gameDetailInfo();
				};
				
				// Send CORS request
				xhr.send();		
			}
			
			function gameDetailInfo() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/gameDetailInfo?id=" + id); 
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
 					var tmp,image;
 					var response = JSON.parse(xhr.response); 
					hideCard();
 					for (i = 0; i < response.length; i++) {
						row = $("<tr/>");
						$("#div"+i).show();
    					tmp = JSON.parse(response[i]);
					 	for (j = 0; j < tmp.length-1; j++) {
					 		if (j == 0) document.getElementById("p"+i).innerHTML = tmp[j];
					 		else if (j == 1)
					 		{
					 			$("#div"+i+" ul").append('<li class="list-group-item list-group-item-danger style="text-align:center;">' +tmp[j+1]+ '<span class="badge">' +tmp[j]+ '</span></li>');
					 		}
					 		else if (j == 2){
								image = "http://localhost:7777/toptrumps/getImage?image="+tmp[j];
								$("#div"+i+" ul").append("<img src=" +image+ " height='100' width='163'>");
							}
							else
								$("#div"+i+" ul").append('<li class="list-group-item list-group-item-info">' +tmp[j]+ '</li>');							
						}
					}
	
				};
				
				// Send CORS request
				xhr.send();		
			}


			function nextRound() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/eliminateLostPlayer?id=" + id); 
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
					window.location.replace("http://localhost:7777/toptrumps/game?id=" + id);
				};
				
				// Send CORS request
				xhr.send();		
			}
			
			function hideCard() {
				$("#div1").hide();$("#div2").hide();
				$("#div3").hide();$("#div4").hide();
				$("#div0").hide();
			}
			
		</script>

		</body>
</html>