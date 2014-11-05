boinqApp.controller("TermTreeController",["$scope",'callEndpoint','Datasource','DatasourceConstants','SPARQLConstants','SPARQLTools',function($scope,callEndpoint,Datasource,DatasourceConstants,SPARQLConstants,SPARQLTools) {
	console.info('Registering controller TermTreeController');

	// from directive: 	<term-tree-picker sourceGraph="selectEndpoint" sourceEndpoint="selectGraph">

	$scope.getRootTerms = function() {
		console.log('Fetching root terms');
		var query = [SPARQLConstants.DEFAULT_PREFIXES,
		             SPARQLConstants.QUERY_GET_ROOT_TERMS].join('\n');
		callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
				function(successResponse) {
					$scope.rootTerms = successResponse.data.results.bindings;
				},
				function(errorResponse) {
					$scope.sparqlError = true;
					$scope.sparqlErrorText = errorResponse.data;
					console.log(errorResponse);
				});
	};
	
	$scope.getChildTerms = function(parentTerm) {
		console.log('Fetching child terms for ' + parentTerm.label.value);
		var query = [SPARQLConstants.DEFAULT_PREFIXES,
		             "SELECT ?uri ?label WHERE {",
		             "  ?uri rdfs:label ?label.",
		             "  ?uri rdfs:subClassOf " + SPARQLTools.angle_bracket(parentTerm.uri.value), "}"].join('\n');
		callEndpoint($scope.sourceEndpoint,$scope.sourceGraph,query).then(
				function(successResponse) {
					parentTerm.subTerms = successResponse.data.results.bindings;
				},
				function(errorResponse) {
					$scope.sparqlError = true;
					$scope.sparqlErrorText = errorResponse.data;
					console.log(errorResponse);
				});

	};
		
}]);