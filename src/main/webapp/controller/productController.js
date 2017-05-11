productModule.controller("productController",function($scope, productService, $http) {
/*
*take all arguments from UI and pass those to service to get restfull ajax called
**/
	$scope.dopass = function(x){
					for(var i=0;i<x.length;i++){
					console.log("entered values are :"+x[i].value);
					}
					
	}
	$http.get('connection.properties').then(function(response) {
        // console.log('field is ', response.data[0].field);
        // console.log('action is ', response.data[0].action);
        $scope.varA = response.data.rows;
      });
  
});

 productModule.controller('BarCtrl', ['$scope', function ($scope) {
 
 //changed values from CATAGORY_# to CAT_# in categoryReport (reason : space issues in view part)
 //changed values from VERY_HIGH to V_HIGH and VERY_LOW to V_LOW in priorityReport (reason : space issues in view part)
 //changed values from SCHEDULED_FOR_DELEGATION to SCHEDULED_DELEGATION in statusReport (reason : space issues in view part)
   var all_data = {"categoryReport":{"names":["CAT_3","CAT_4","CAT_1","CAT_2","CAT_5"],"counts":[7,5,3,9,1]},"priorityReport":{"names":["V_HIGH","MEDIUM","HIGH","LOW","V_LOW"],"counts":[3,4,5,9,4]},"statusReport":{"names":["SCHEDULED_DELEGATION"],"counts":[25]}};
	
	$scope.allData = JSON.parse(JSON.stringify(all_data));
	$scope.Chart_data1 = Object.keys(all_data)[0];
	$scope.Chart_data2 = Object.keys(all_data)[1];
	$scope.Chart_data3 = Object.keys(all_data)[2];
     $scope.options = { scaleShowVerticalLines: false };
     $scope.labels1 = all_data.categoryReport.names;
	 $scope.labels2 = all_data.priorityReport.names;
	 $scope.labels3 = all_data.statusReport.names;
     $scope.data1 = [
       all_data.categoryReport.counts	   
     ];
	 $scope.data2 = [
       all_data.priorityReport.counts	   
     ];
	 $scope.data3 = [
       all_data.statusReport.counts	   
     ];

   }]);
   
   productModule.controller('tableCtrl',['$scope', function($scope){
   
   var tableData = {"totalCount":25,"currentPage":1,"pageSize":5,"orderBy":null,"task":[{"id":1001,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"HIGH","category":"CATEGORY_1","status":"SCHEDULED_FOR_DELEGATION"},{"id":1002,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"VERY_HIGH","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"},{"id":1003,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_3","status":"SCHEDULED_FOR_DELEGATION"},{"id":1004,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"MEDIUM","category":"CATEGORY_3","status":"SCHEDULED_FOR_DELEGATION"},{"id":1005,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"HIGH","category":"CATEGORY_4","status":"SCHEDULED_FOR_DELEGATION"},{"id":1006,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"VERY_LOW","category":"CATEGORY_3","status":"SCHEDULED_FOR_DELEGATION"},{"id":1007,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"VERY_HIGH","category":"CATEGORY_5","status":"SCHEDULED_FOR_DELEGATION"},{"id":1008,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"},{"id":1009,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_3","status":"SCHEDULED_FOR_DELEGATION"},{"id":1010,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"HIGH","category":"CATEGORY_1","status":"SCHEDULED_FOR_DELEGATION"},{"id":1011,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_4","status":"SCHEDULED_FOR_DELEGATION"},{"id":1012,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"VERY_LOW","category":"CATEGORY_3","status":"SCHEDULED_FOR_DELEGATION"},{"id":1013,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"},{"id":1014,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"HIGH","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"},{"id":1015,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"MEDIUM","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"},{"id":1016,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"VERY_LOW","category":"CATEGORY_3","status":"SCHEDULED_FOR_DELEGATION"},{"id":1017,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_4","status":"SCHEDULED_FOR_DELEGATION"},{"id":1018,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"MEDIUM","category":"CATEGORY_4","status":"SCHEDULED_FOR_DELEGATION"},{"id":1019,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"MEDIUM","category":"CATEGORY_4","status":"SCHEDULED_FOR_DELEGATION"},{"id":1020,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"HIGH","category":"CATEGORY_3","status":"SCHEDULED_FOR_DELEGATION"},{"id":1021,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"VERY_HIGH","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"},{"id":1022,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_1","status":"SCHEDULED_FOR_DELEGATION"},{"id":1023,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"VERY_LOW","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"},{"id":1024,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"},{"id":1025,"shortName":"PASSWORD RESET","description":"RESET TECHM DOMAIN PASSWORD","type":"RESET","priority":"LOW","category":"CATEGORY_2","status":"SCHEDULED_FOR_DELEGATION"}]};
   
   $scope.init = function(){
				//pageSettingService.getData(function(data){//as AJAX call is asynchronous call , we used callback function
				$scope.items = tableData.task;
				$scope.pageSize = 3;// initially row per page is set to 2 which is changable from UI
				//});				
	}
	$scope.page1stPart = function(){
	$scope.pageSize = 5;
	}

/*
*this function allows to show max 10 row will show in table 
**/
	$scope.page2ndPart = function(){
	$scope.pageSize = 10;
	}
	
/*
*this function allows to show max 25 row will show in table 
**/
	$scope.page3rdPart = function(){
	$scope.pageSize = 25;
	}
	
/*
*this function allows to show max 50 row will show in table 
**/
	$scope.page4thPart = function(){
	$scope.pageSize = 50;
	}
   }])