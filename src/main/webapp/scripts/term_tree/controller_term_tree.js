boinqApp.controller("TermTreeController",["$scope",'callEndpoint','Datasource','DatasourceConstants','SPARQLConstants','SPARQLTools','QueryBuilderService',function($scope,callEndpoint,Datasource,DatasourceConstants,SPARQLConstants,SPARQLTools,QueryBuilderService) {
	console.info('Registering controller TermTreeController');

	// from directive: 	<term-tree-picker sourceGraph="selectEndpoint" sourceEndpoint="selectGraph" rootNodesQuery="" >

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
		if ($scope.rootNodesQuery != null) {
			callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
					function(successResponse) {
						$scope.rootTerms = successResponse.data.results.bindings;
					},
					processError);			
		} else {
			QueryBuilderService.rootNodesQuery().then(function(query) {
				console.log('Fetching root terms');
				callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
						function(successResponse) {
							$scope.rootTerms = successResponse.data.results.bindings;
						},
						processError);
			});
		}
	};
	
	$scope.getFilteredTree = function(filter) {
		QueryBuilderService.filteredTreeQuery({filter: filter}).then(function(query) {
			console.log("Fetching matching tree");
			callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
					function (successResponse) {
						$scope.terms = successResponse.data.results.bindings;
						$scope.rootTerms = makeTree($scope.terms);
					});
		});
	};
	
	$scope.makeTree = function(terms) {
		// find rootTerms
		$scope.rootTerms = [];
		for (term in terms) {
			if (term.parent == null) {
				$scope.fillChildren(term, terms);
				$scope.rootTerms.push(term);
			}
		}
		console.info($scope.rootTerms);
	};
	
	$scope.fillChildren = function(parent, terms) {
		for (term in terms) {
			if (term.parent != null && term.parent.value == parent.value) {
				parent.subTerms.add($scope.fillChildren(term, terms));
			}
		}
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