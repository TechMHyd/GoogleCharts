/*
* created a route module which is applicable through out whole html tag of index.html
* */
var productModule = angular.module('productModule', ['ngRoute','chart.js','angularUtils.directives.dirPagination']);

productModule.config(['$routeProvider',
        function($routeProvider) {
            $routeProvider.
                when('/route1', {
                    templateUrl: 'partial/route1.html'
                   
                }).
                when('/route2', {
                    templateUrl: 'partial/bars.html'
                }).
                otherwise({
                    redirectTo: '/route1'
                });
        }]);
		
productModule.config(function (ChartJsProvider) {
     // Configure all charts
     ChartJsProvider.setOptions({
			colours: ['#97BBCD', '#DCDCDC', '#F7464A', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
			responsive: true
     });
    
   });
/*productModule.directive('href', function() {
  return {
    compile: function(element) {
      element.attr('target', '_blank');
    }
  };
});*/
  