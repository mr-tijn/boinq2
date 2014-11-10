boinqApp.controller("TermTreeController",["$scope",'callEndpoint','Datasource','DatasourceConstants','SPARQLConstants','SPARQLTools','QueryBuilderService',function($scope,callEndpoint,Datasource,DatasourceConstants,SPARQLConstants,SPARQLTools,QueryBuilderService) {
	console.info('Registering controller TermTreeController');

	// from directive: 	<term-tree-picker sourceGraph="selectEndpoint" sourceEndpoint="selectGraph">

	$scope.$watch('sourceEndpoint', function() {
	      $scope.getRootTerms();
	 });

	var processError = function(errorResponse) {
		$scope.sparqlError = true;
		$scope.sparqlErrorText = errorResponse.data;
		console.log(errorResponse);
	};

	$scope.getRootTerms = function() {
		console.log('Getting query from query builder');
		QueryBuilderService.rootNodesQuery().then(function(query) {
			console.log('Fetching root terms');
			callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
					function(successResponse) {
						$scope.rootTerms = successResponse.data.results.bindings;
					},
					processError);
		});
	};
	
	$scope.getChildTerms = function(parentTerm) {
		console.log('Getting query from query builder');
		QueryBuilderService.childNodesQuery(parentTerm.uri.value).then(function(query) {
			console.log('Fetching child terms for ' + parentTerm.label.value);
			callEndpoint($scope.sourceEndpoint,$scope.sourceGraph,query).then(
					function(successResponse) {
						parentTerm.subTerms = successResponse.data.results.bindings;
					},
					function(errorResponse) {
						$scope.sparqlError = true;
						$scope.sparqlErrorText = errorResponse.data;
						console.log(errorResponse);
					});
		});

	};
		
}]);