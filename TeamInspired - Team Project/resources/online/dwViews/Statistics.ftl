<html>

	<head>
		<!-- Web page title -->
    	<title>Top Trumps</title>
    	
       	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<script src="http://localhost:7777/toptrumps/getFile"></script>

	</head>

    <body onload="initalize()"> 
    	
    	<div class="container">
			<br>
			<p style="text-align:center;"><img src="http://localhost:7777/toptrumps/getImage?image=stats" width="500" height="100"></p>
			<br>	
			
			<div class="row">
				<div class="col-sm-4">
					<div class="panel panel-primary">
			  	        <div class="panel-heading" align="center" style="background-color:#66ff33"><font color="#0000b3"><b>All Game Summary</b></font></div>
							<table class="table">							    
							    <tbody id="GameStats"></tbody>
							</table>																				
					</div>
				</div>	
			
				<div class="col-sm-8">
					<div class="panel panel-primary">
			  	        <div class="panel-heading" align="center" style="background-color:#66ff33"><font color="#0000b3"><b>Previous Game Stats</b></font></div>
						<div class="panel-body">
							<div class="col-sm-6">
								<table class="table">							
								    <tbody id="GameStats2"></tbody>
								</table>
							</div>
							
							<div class="col-sm-6">
								<table class="table">							
								    <tbody id="GameStats3"></tbody>
								</table>
							</div>
					</div>																										
					</div>
				</div>	
			</div>	
		     
			<div class="row">
				<div class="col-sm-6">
					<div class="panel panel-primary">
				  	    <div class="panel-heading" align="center" style="background-color:#66ff33"><font color="#0000b3"><b>Previous Game Round Winner</b></font></div>
							<div class="panel-body"> 
								<canvas id="chart-area" />													
							</div>																										
					</div>
			    </div>
			    	
		    	<div class="col-sm-6">
					<div class="panel panel-primary">
				  	    <div class="panel-heading" align="center" style="background-color:#66ff33"><font color="#0000b3"><b>Latest 5 Previous Game Round Winner</b></font></div>
							<div class="panel-body"> 
								<canvas id="canvas"></canvas>											
							</div>																										
					</div>
			    </div>
			    
    		</div>	
		
		<button onclick="back()" type="button" class="btn btn-primary">Back</button>
		</div>
		
		<script type="text/javascript">
	
			var chartData = [];
			var chartLabel = [];
			var lineHuman = [];
			var line1 = [];
			var line2 = [];
			var line3 = [];
			var line4 = [];
						
			// Method that is called on page load
			function initalize() {
            
				gameStats();
				var ctx = document.getElementById("chart-area").getContext("2d");
				myDoughnut = new Chart(ctx, config);
				
				var ctx2 = document.getElementById("canvas").getContext("2d");
            	myLine = new Chart(ctx2, config2);
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
			
		
			function gameStats() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/gameStats");
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
					var row,tmp,tmp2;
 					var response = JSON.parse(xhr.response);
 					$('#GameStats tr').remove();
 					tmp = JSON.parse(response[0]);
 					tmp2 = JSON.parse(response[1]);
    				for (j = 0; j < tmp2.length; j++) {
	    				row = $("<tr/>"); 
    					$("#GameStats").append(row);
    					row.append($("<td bgcolor='#e6ffe6' align='center'>" +tmp[j]+ "</td>"));
    					row.append($("<td bgcolor='#e6ffe6' align='center'>" +tmp2[j]+ "</td>"));
    					if ( j == 3)
    					{
	    					row = $("<tr/>"); 
	    					$("#GameStats").append(row);
	    					row.append($("<td bgcolor='#e6ffe6' align='center'>Avg Draw</td>"));
	    					row.append($("<td bgcolor='#e6ffe6' align='center'>" +parseFloat(Math.round( (parseInt(tmp2[3])/parseInt(tmp2[0])) * 100) / 100).toFixed(2) + "</td>"));
    					}
    					
    				} 	
    				gamePrevStats();				
				};
				
				// Send CORS request
				xhr.send();		
			}
			
			
			function gamePrevStats() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/getPrevGameStats");
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
					var row,tmp,tmp2;
 					var response = JSON.parse(xhr.response);
 					$('#GameStats2 tr').remove();
 					tmp = JSON.parse(response[0]);
 					tmp2 = JSON.parse(response[1]);
    				for (j = 0; j < tmp2.length; j++) {
	    				row = $("<tr/>"); 
    					$("#GameStats2").append(row);
    					if (j==0){
	    					row.append($("<td bgcolor='#1ad1ff' align='center'>" +tmp[j]+ "</td>"));
	    					row.append($("<td bgcolor='#1ad1ff' align='center'>" +tmp2[j]+ "</td>"));
    					}else{
	    					row.append($("<td bgcolor='#ccffee' align='center'>" +tmp[j]+ "</td>"));
	    					row.append($("<td bgcolor='#ccffee' align='center'>" +tmp2[j]+ "</td>"));
    					}
    					
    				} 				
    				getPrevGameWinnerStats();	
				};
				
				// Send CORS request
				xhr.send();		
			}


			function getPrevGameWinnerStats() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/getPrevGameWinnerStats");
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
					var row,tmp;
 					var response = JSON.parse(xhr.response);
 					$('#GameStats3 tr').remove();
 					row = $("<tr/>"); 
    				$("#GameStats3").append(row);
    				row.append($("<td bgcolor='#ffa64d' align='center'>Round Winner</td>"));
    				row.append($("<td bgcolor='#ffa64d' align='center'>Total Win</td>"));
 					for (i = 0; i < response.length; i++) {
	    				row = $("<tr/>"); 
    					$("#GameStats3").append(row);
	 					tmp = JSON.parse(response[i]);
	 					chartLabel.push(tmp[0]);
	 					chartData.push(parseInt(tmp[1]));
	    				for (j = 0; j < tmp.length; j++) 
		    		    	row.append($("<td bgcolor='#ffff99' align='center'>" +tmp[j]+ "</td>"));
	    			}
	    			generateChart(chartData,chartLabel);
	    			getPlayerStats();						
				};
				
				// Send CORS request
				xhr.send();		
			}		
			
			function getPlayerStats() {
				// Create a CORS request
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/getPlayerStats");
				
				// Check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// Define an action to do when the response arrives 
				xhr.onload = function(e) {
 					var response = JSON.parse(xhr.response);
 					var tmp0 = JSON.parse(response[0]);
 					var tmp1 = JSON.parse(response[1]);
 					var tmp2 = JSON.parse(response[2]);
 					var tmp3 = JSON.parse(response[3]);
 					var tmp4 = JSON.parse(response[4]);
 					
 					for (i = 0; i < tmp0.length; i++) {
	 					lineHuman.push(parseInt(tmp0[i]));
	 					line1.push(parseInt(tmp1[i]));
	 					line2.push(parseInt(tmp2[i]));
	 					line3.push(parseInt(tmp3[i]));
	 					line4.push(parseInt(tmp4[i]));
 					}
	    			generateChart2();						
				};
				
				// Send CORS request
				xhr.send();		
			}			
			
			function back() {
				$.urlParam = function(name){
					var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
					return results[1] || 0;
				}
			
				if ($.urlParam('option') == "menu")
					window.location.replace("http://localhost:7777/toptrumps");
				else if ($.urlParam('option') == "game")
					window.location.replace("http://localhost:7777/toptrumps/game?id="+$.urlParam('id'));
				else
					window.location.replace("http://localhost:7777/toptrumps/winner?id="+$.urlParam('id'));											
			}
			
			function generateChart() {
				config.data.datasets.forEach(function(dataset) {
		            dataset.data = chartData;
	        	});
				
				config.data.labels = chartLabel;
				myDoughnut.update();
	
			}
			
			function generateChart2() {
				config2.data.datasets[0].data = lineHuman;
				config2.data.datasets[1].data = line1;
				config2.data.datasets[2].data = line2;
				config2.data.datasets[3].data = line3;
				config2.data.datasets[4].data = line4;
				myLine.update();							
			}
				
			  	var config = {
			        type: 'doughnut',
			        data: {
			            datasets: [{
			                data: [],
			                backgroundColor: [
			                    'rgb(255, 99, 132)',
								'rgb(153, 102, 255)',
								'rgb(54, 162, 235)',
								'rgb(66, 244, 66)',
								'rgb(255, 159, 64)'
			                ]       
			            }],
			            labels: []
			        },
			        options: {
			            responsive: true,
			            legend: {
			                position: 'left',
			            },
			            animation: {
			                animateScale: true,
			                animateRotate: true
			            }
			        }
		    	};
	
				
				
        var config2 = {
            type: 'line',
            data: {
                labels: ["Game 1", "Game 2", "Game 3", "Game 4", "Game 5"],
                datasets: [{
                    label: "Human",
                    backgroundColor: 'rgb(255, 99, 132)',
                    borderColor: 'rgb(255, 99, 132)',
                    data: [],
                    fill: false,
                }, {
                    label: "AI-1",
                    fill: false,
                    backgroundColor: 'rgb(153, 102, 255)',
                    borderColor: 'rgb(153, 102, 255)',
                    data: [],                       
                }, {
                    label: "AI-2",
                    fill: false,
                    backgroundColor: 'rgb(54, 162, 235)',
                    borderColor: 'rgb(54, 162, 235)',
                    data: [],                       
                }, {
                    label: "AI-3",
                    fill: false,
                    backgroundColor: 'rgb(66, 244, 66)',
                    borderColor: 'rgb(66, 244, 66)',
                    data: [],                       
                }, {
                    label: "AI-4",
                    fill: false,
                    backgroundColor: 'rgb(255, 159, 64)',
                    borderColor: 'rgb(255, 159, 64)',
                    data: [],                       
                }]
            },
            options: {
                responsive: true,
                legend: {
					position: 'right',
				},
                tooltips: {
                    mode: 'index',
                    intersect: false,
                },
                hover: {
                    mode: 'nearest',
                    intersect: true
                },
                scales: {
                    xAxes: [{
                        display: true
                    }],
                    yAxes: [{
                        display: true
                    }]
                }
            }
        };
				
				
				
				
					
		</script>
		
		</body>
</html>