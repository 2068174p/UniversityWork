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

			<h1 style="text-align: center;color: Green" id="round">--</h1>	
			<br>

			<div class="row">
				<div class="col-sm-3" id="div1">
					<ul class="list-group list-group-flush"></ul>
				</div>
			
				<div class="col-sm-3" id="div0">
					<div class="panel panel-info">
			  	        <div class="panel-heading" align="center" style="background-color:#42f442"><b id="p0"></b></div>
						<ul class="list-group list-group-flush"></ul>
					</div>
				</div>		
				<div class="col-sm-5" id="div0">
					<img src="http://localhost:7777/toptrumps/getImage?image=bg" style="background-repeat:no-repeat">
				</div>	
			</div>		
			
			<br>	
			<button onclick="playGame()" type="button" class="btn btn-primary">Play</button>
			<button onclick="automateGame()" type="button" class="btn btn-success" id="fbtn">Forward</button>
			<button onclick="resetGame()" type="button" class="btn btn-danger">Reset</button>
			<button onclick="gameStats()" type="button" class="btn btn-warning">Statistic</button>
		
		</div>
		
		<script type="text/javascript">
			var id;
			$.urlParam = function(name){
					var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
					return results[1] || 0;
			}
			
			id = $.urlParam('id')


			// Method that is called on page load
			function initalize() {
			
			
				gameActivePlayer();			
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
			
			function gameActivePlayer() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/gameActivePlayer?id=" + id); 
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives
				xhr.onload = function(e) {
 					var responseText = xhr.response; 
 					if (responseText > 1){
						gameInitInfo();
						gameDetailInfo();
					} 
					else
						window.location.replace("http://localhost:7777/toptrumps/winner?id=" + id);
				};
				
				// Send CORS request
				xhr.send();		
				
			}
			
			function gameInitInfo() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/gameInitInfo?id=" + id); 
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives
				xhr.onload = function(e) {
 					var response = JSON.parse(xhr.response); 
 					document.getElementById("round").innerHTML = "Round " + response[0];
 					$("#div1 ul").append('<li class="list-group-item" style="background-color:#ffa64d">Active Player : ' +response[1]+ '</li>');							
 					$("#div1 ul").append('<li class="list-group-item list-group-item-warning">Communal Card : ' +response[5]+ '</li>');							
 					$("#div1 ul").append('<li class="list-group-item list-group-item-danger">Category : <select id="categoryList"></select><label id="lbl0"></label></li>');
 					
 					var categoryList = response[3].split(" ");
 					for (i = 1; i < categoryList.length; i++) {
	 					$('#categoryList').append('<option value='+i +'>'+categoryList[i]+'</option>'); 
					}
					
					$("#fbtn").hide();
					if(response[2] == "0"){
						$('#categoryList').val(response[4]); 
	 					$('#categoryList').attr('disabled', true);
	 					$("#fbtn").show();
	 					$("#categoryList").hide();
	 					document.getElementById("lbl0").innerHTML = $("#categoryList option:selected").text();
	 				}
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
 					tmp = JSON.parse(response[0]);
 					if (tmp[tmp.length-1] == 1){
	 					for (j = 0; j < tmp.length-1; j++) {
					 		if (j == 0) document.getElementById("p0").innerHTML = tmp[j];
					 		else if (j == 1)
					 		{
					 			$("#div0 ul").append('<li class="list-group-item list-group-item-danger style="text-align:center;">' +tmp[j+1]+ '<span class="badge">' +tmp[j]+ '</span></li>');
					 		}
					 		else if (j == 2){
								image = "http://localhost:7777/toptrumps/getImage?image="+tmp[j];
								$("#div0 ul").append("<img src=" +image+ " height='100' width='262'>");
							}
							else
								$("#div0 ul").append('<li class="list-group-item list-group-item-success">' +tmp[j]+ '</li>');							
								
						}
					}
					else {
						document.getElementById("p0").innerHTML = "Human";
						image = "http://localhost:7777/toptrumps/getImage?image=lose";
						$("#div0 ul").append("<img src=" +image+ " height='100' width='262'>");
					}
				};
				
				// Send CORS request
				xhr.send();		
			}
			
			function resetGame() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/resetGame?id=" + id + "&option=cancel"); 
				
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
			
			function playGame() {
				window.location.replace("http://localhost:7777/toptrumps/result?id=" + id + "&option=" + $('#categoryList').val());
			}
			
			function automateGame() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/automateGame?id=" + id); 
				
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
			
			function gameStats() {
				window.location.replace("http://localhost:7777/toptrumps/stats?id=" + id + "&option=game");
			}	
			
		</script>
		
	</body>
</html>