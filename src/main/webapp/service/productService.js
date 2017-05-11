productModule.service('productService', function($http) {
/*
*take arguments from controller and make a ajax call(it may be GET/POST/DELETE/PUT)
*/
	this.restCall = function(building, floor, wing, space_id){
		var data = building+","+floor+","+wing+","+"space_id";
					 var responsePromise = 	 $http({	url: 'http://localhost:8080/somerestfullwebserviceendpoint', 
															method: "POST", // In this case it is POST
															headers: { 'Content-Type': 'text/plain' },
															data: data
														 });
					responsePromise.success(function(data, status, headers, config) {
						 alert("data added");
					 });
					 responsePromise.error(function(data, status, headers, config) {
						 alert("AJAX failed! because no webservice is attached yet");      					 
					 });
	};

});