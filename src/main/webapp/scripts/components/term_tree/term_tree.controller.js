angular.module('boinqApp').controller("TermTreeController",["$scope",'callEndpoint','Datasource','DatasourceConstants','SPARQLConstants','SPARQLTools','QueryBuilderService',function($scope,callEndpoint,Datasource,DatasourceConstants,SPARQLConstants,SPARQLTools,QueryBuilderService) {
	console.info('Registering controller TermTreeController');

	// from directive: 	<term-tree-picker sourceGraph="selectEndpoint" sourceEndpoint="selectGraph" rootNodesQuery="" multiValued="" >

	
	
	$scope.selectedTerms = [];
	
//	$scope.$watch('sourceEndpoint', function() {
//	      $scope.getRootTerms();
//	 });
	
//	$scope.$watch('searchFilter', function(value) {
//		console.info(value);
//		if (value && value.length > 3) {
//			$scope.getFilteredTree(value);
//		}
//	});

	$scope.searchFilterKeyPress = function(event) {
		console.info(event);
		if (event.which === 13) {
			$scope.getFilteredTree();
		}
	};
	
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
	
	$scope.getSelectedTerms = function(terms) {
		for (var idx in terms) {
			var term = terms[idx];
			if ('selected' in term && term.selected) {
				$scope.selectedTerms.push(term);
			}
			$scope.getSelectedTerms(term.subTerms);
		}
	};
	
	$scope.computeSelection = function() {
		$scope.selectedTerms = [];
		$scope.getSelectedTerms($scope.rootTerms);
		$scope.selectHandler($scope.selectedTerms);
		$scope.close();
	};
	
	$scope.getFilteredTree = function() {
		var filter = $scope.searchFilter;
		QueryBuilderService.filteredTreeQuery(filter).then(function(query) {
			console.log("Fetching matching tree");
			callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
					function (successResponse) {
						$scope.terms = successResponse.data.results.bindings;
						$scope.rootTerms = $scope.makeTree($scope.terms);
					});
		});
	};
	
	$scope.makeTree = function(terms) {
		// find rootTerms
		$scope.rootTerms = [];
		for (var idx in terms) {
			if (terms[idx].parenturi == null) {
				$scope.rootTerms.push($scope.fillChildren(terms[idx], terms));
			}
		}
		return $scope.rootTerms;
		console.info($scope.rootTerms);
	};
	
	$scope.fillChildren = function(parent, terms) {
		parent.subTerms = [];
		for (var idx in terms) {
			if (terms[idx].parenturi != null && terms[idx].parenturi.value == parent.uri.value) {
				parent.subTerms.push($scope.fillChildren(terms[idx], terms));
			}
		}
		return parent;
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
	
    $scope.pickTerm = function() {
    	console.info("getting root terms now");
    	$scope.getRootTerms();
    	$('#selectTermModal').modal('show');
    };

    $scope.close = function() {
    	$('#selectTermModal').modal('hide');
    };
		
}]);