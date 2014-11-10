boinqApp.controller("SparqlBrowserController",['$scope','callEndpoint','updateEndpoint','Datasource','QueryBuilderService',function($scope,callEndpoint,updateEndpoint,Datasource,QueryBuilderService) {
	console.info('Registering controller SparqlBrowserController');
	$scope.sparqlQuery = "select * where { graph ?graph { ?subject ?predicate ?object . } } limit 10";
	$scope.endpoints = [];
	$scope.selectEndpoint = [];
	$scope.updateEndpoints = [];
	$scope.selectUpdateEndpoint = null;
	$scope.datasources = Datasource.query(function() {
		for (var i = 0; i < $scope.datasources.length; i++) {
			var datasource = $scope.datasources[i];
			$scope.endpoints.push(datasource.endpointUrl);
			if (datasource.endpointUpdateUrl != null) {
				$scope.updateEndpoints.push(datasource.endpointUpdateUrl);
			}
		}
	});
	var processError = function(errorResponse) {
		$scope.sparqlError = true;
		$scope.sparqlSuccess = false;
		$scope.sparqlErrorText = errorResponse.data;
		console.log(errorResponse);
	};
	$scope.callLocalEndpoint = function(queryString) {
		$scope.successResponse = null;
		$scope.sparqlSuccess = false;
		callEndpoint($scope.selectEndpoint,null,queryString)
		.then(
			function(successResponse) {
				console.log("Got response: "+successResponse.data);
				$scope.sparqlError = false;
				$scope.sparqlResult = successResponse.data;
			},
			processError);
		};
	$scope.uploadData = function() {
		$scope.updateResult = "";
		$scope.sparqlSuccess = false;
		QueryBuilderService.insertQuery($scope.submitGraph,$scope.submitSubject,$scope.submitPredicate,$scope.submitObject).then(function (queryString) {
			updateEndpoint($scope.selectUpdateEndpoint,$scope.submitGraph,queryString)
			.then(
					function(successResponse) {
						console.log("Got response: "+ successResponse.data);
						var el = document.createElement( 'div' );
						el.innerHTML = successResponse.data;
						$scope.sparqlError = false;
						$scope.sparqlSuccess = true;
						$scope.sparqlSuccessText = el.innerHTML;
					},
					processError);
		},processError);
				
	};
	
	//brol
	$scope.termPicked = function(term) {
		console.log("term picked: " + term.uri.value);
	};
	$scope.rootNode = {
		name: "Node",
		type: "MatchAll",
		nodes: [],
	};

}]);