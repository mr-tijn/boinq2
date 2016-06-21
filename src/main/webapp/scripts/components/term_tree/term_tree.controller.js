angular.module('boinqApp').controller("TermTreeController",["$scope",'callEndpoint','Datasource','DatasourceConstants','SPARQLConstants','SPARQLTools','QueryBuilderService',function($scope,callEndpoint,Datasource,DatasourceConstants,SPARQLConstants,SPARQLTools,QueryBuilderService) {
	console.info('Registering controller TermTreeController');

	// from directive: 	<term-tree-picker sourceGraph="selectEndpoint" sourceEndpoint="selectGraph" rootNodesQuery="" multiValued="" >

	
	
	$scope.selectedTerms = [];
	$scope.selectedTerm = {};
	
//	$scope.$watch('ngModel', function(){
//		$scope.getngModel();
//	})
	
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
		if ($scope.rootNodesQuery != null) {
			console.log('Getting query from query builder');
			callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, $scope.rootNodesQuery).then(
					function(successResponse) {
						$scope.rootTerms = successResponse.data.results.bindings;
					},
					processError);			
		} else {
			QueryBuilderService.rootNodesQuery().then(function(response) {
				console.log('Fetching root terms');
				var query = response.query;
				$scope.rootNodesQuery = query;
				callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
						function(successResponse) {
							if (successResponse && successResponse.data && successResponse.data.results) {
								$scope.rootTerms = successResponse.data.results.bindings;
							} else {
								$scope.rootTerms = null;
							}
						},
						processError);
			});
		}
	};
	
	$scope.getChildTerms = function(parentTerm) {
		console.log('Getting query from query builder');
		QueryBuilderService.childNodesQuery(parentTerm.uri.value).then(function(response) {
			console.log('Fetching child terms for ' + parentTerm.label.value);
			var query = response.query;
			callEndpoint($scope.sourceEndpoint,$scope.sourceGraph,query).then(
					function(successResponse) {
						if (successResponse && successResponse.data && successResponse.data.results) {
							parentTerm.subTerms = successResponse.data.results.bindings;
						} else {
							parentTerm.subTerms = null;
						}
					},
					function(errorResponse) {
						$scope.sparqlError = true;
						$scope.sparqlErrorText = errorResponse.data;
						console.log(errorResponse);
					});
		});

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
		if ($scope.selectHandler) {
			$scope.selectHandler($scope.selectedTerms);
		}
		$scope.close();
	};
	
	$scope.getFilteredTree = function() {
		var filter = $scope.searchFilter;
		if (filter != null && filter.length > 0) {
			QueryBuilderService.filteredTreeQuery(filter).then(function(response) {
				var query = response.query;
				console.log("Fetching matching tree");
				callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
						function (successResponse) {
							$scope.terms = successResponse.data.results.bindings;
							$scope.rootTerms = $scope.makeTree($scope.terms);
						});
			});
		} else {
			$scope.getRootTerms();
		}
	};
	
	$scope.makeTree = function(terms) {
		// find rootTerms
		$scope.rootTerms = [];
		for (var idx in terms) {
			if (terms[idx].parenturi == null || "http://www.w3.org/2002/07/owl#Thing" == terms[idx].parenturi.value ) {
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
	
	$scope.handleSelect = function(term) {
		$scope.selectedTerm = term;
		if ($scope.selectHandler) {
			$scope.selectHandler(term);
		}
		$scope.close()
	}
	
    $scope.pickTerm = function() {
    	console.info("getting root terms now");
    	$scope.getRootTerms();
    	$("#" + $scope.modalId).modal('show');
    };

    $scope.close = function() {
    	$("#" + $scope.modalId).modal('hide');
    };
		
}]);