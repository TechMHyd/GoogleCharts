(function () {
  'use strict';

  var app = angular.module('examples', ['chart.js']);

  app.config(function (ChartJsProvider) {
    // Configure all charts
    ChartJsProvider.setOptions({
      colours: ['#97BBCD', '#DCDCDC', '#F7464A', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
      responsive: true
    });
    // Configure all doughnut charts
    ChartJsProvider.setOptions('Bar', {
      animateScale: true
    });
  });

  
  
  app.controller('DoughnutCtrl', ['$scope', '$timeout','$http', function ($scope, $timeout,$http) {

	  $scope.datasetOverride = [{
	      fill: true,
	      backgroundColor: [
	        "#66ff33",
	        "#36A2EB",
	        "#FFCE56"
	      ]
	    }, {
	      fill: true,
	      backgroundColor: [
	        "#ffff00",
	        "#46BFBD",
	        "#FDB45C"
	      ]
	    }];
	  
	  $scope.options = {
				tooltipEvents : [],
				showTooltips : true,
				tooltipCaretSize : 0,
				onAnimationComplete : function() {
					this.showTooltip(this.segments, true);
				},
			};
	  	/*$scope.labels = ['Download Sales', 'In-Store Sales', 'Mail-Order Sales','test','test2','test3'];
	    $scope.data = [0, 0, 0, 0, 0, 0];*/
	  $scope.data = [0];
	    $http.get('./aggdriversmodel').
	    success(function(data, status, headers, config) {
	    	console.log("aggdriversmodel success:data :"+data);
	    	console.log("aggdriversmodel success:status :"+status);
	      $scope.labels = data;
	    }).
	    error(function(data, status, headers, config) {
	      // log error
	    	console.log("aggdriversmodel error:data :"+data);
	    	console.log("aggdriversmodel error:status :"+status);
	    });
	    
	    $timeout(function () {
	    	$http.get('./aggressdrivers').
		    success(function(data, status, headers, config) {
		    	console.log("aggressdrivers success:data :"+data);
		    	console.log("aggressdrivers success:status :"+status);
		      $scope.data = data;
		    }).
		    error(function(data, status, headers, config) {
		      // log error
		    	console.log("aggressdrivers error:data :"+data);
		    	console.log("aggressdrivers error:status :"+status);
		    });
	      /*$scope.data = [[350, 450, 100, 200, 35, 40],[250, 350, 100, 100, 45, 50],
	                     [360, 430, 200, 100, 30, 42],[252, 340, 300, 200, 44, 43]];*/
	    }, 500);
	 }]);
  
  app.controller('StackedBarCtrl', ['$scope','$http', function ($scope,$http) {
	  
	    $http.get('./aggressdrivers').
	    success(function(data, status, headers, config) {
	    	//console.log("aggressdrivers success:data :"+data);
	      $scope.data = data;
	    }).
	    error(function(data, status, headers, config) {
	      // log error
	    	//console.log("aggressdrivers error:data :"+data);
	    	
	    });
	    
	    $http.get('./aggdriversmodel').
	    success(function(data, status, headers, config) {
	    	//console.log("aggdriversmodel success:data :"+data);
	    	//console.log("aggdriversmodel success:status :"+status);
	      $scope.labels = data;
	    }).
	    error(function(data, status, headers, config) {
	      // log error
	    	//console.log("aggdriversmodel error:data :"+data);
	    });
	    
	    $http.get('./aggdriverscity').
	    success(function(data, status, headers, config) {
	    	//console.log("aggdriverscity success:data :"+data);
	      $scope.series = data;
	    }).
	    error(function(data, status, headers, config) {
	      // log error
	    	//console.log("aggdriverscity error:data :"+data);
	    });
	    
	    //$scope.labels = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
	    $scope.type = 'StackedBar';
	    //$scope.series = ['2015', '2016'];
	    
	    $scope.options = {
	    	      scales: {
	    	        xAxes: [{
	    	          stacked: true,
	    	        }],
	    	        yAxes: [{
	    	          stacked: true
	    	        }]
	    	      }
	       };
	
	    /*$scope.data = [
	      [65, 59, 90, 81, 56, 55, 40],
	      [18, 46, 60, 29, 96, 20, 100],
	      [22, 40, 50, 29, 76, 17, 150],
	      [25, 30, 30, 20, 86, 27, 106]
	    ];*/
	  }]);
  
  

  app.controller('DataTablesCtrl', function ($scope,$http) {
	  
	  $http.get('./aggressdrivers').
	    success(function(data, status, headers, config) {
	      $scope.data = data;
	    }).
	    error(function(data, status, headers, config) {
	    });
	  
	  $http.get('./aggdriversmodel').
	    success(function(data, status, headers, config) {
	      $scope.labels = data;
	    }).
	    error(function(data, status, headers, config) {
	    });
	    
	    $http.get('./aggdriverscity').
	    success(function(data, status, headers, config) {
	      $scope.series = data;
	    }).
	    error(function(data, status, headers, config) {
	    });
	  
    /*$scope.labels = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
    $scope.series = ['Series A', 'Series B'];*/
    $scope.colours = [
      { // grey
        fillColor: 'rgba(148,159,177,0.2)',
        strokeColor: 'rgba(148,159,177,1)',
        pointColor: 'rgba(148,159,177,1)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(148,159,177,0.8)'
      },
      { // dark grey
        fillColor: 'rgba(77,83,96,0.2)',
        strokeColor: 'rgba(77,83,96,1)',
        pointColor: 'rgba(77,83,96,1)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(77,83,96,1)'
      }
    ];
    
    //random values as data 
    /*$scope.randomize = function () {
      $scope.data = $scope.data.map(function (data) {
    	  
        return data.map(function (y) {
          y = y + Math.random() * 10 - 5;
          return parseInt(y < 0 ? 0 : y > 100 ? 100 : y);
        });
      });
    };*/
  });
  
  // 
})();
