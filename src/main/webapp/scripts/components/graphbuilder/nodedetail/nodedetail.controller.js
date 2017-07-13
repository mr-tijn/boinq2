angular.module('boinqApp').controller("QueryNodeController",['$scope','QueryDefinitionObjects','NodeConstants',function($scope,QueryDefinitionObjects,NodeConstants) {
	console.log("Registering QueryNodeController");
	$scope.selection.filter = null;
	$scope.name = function(template) {
		return NodeConstants.TYPE_ITEMS[template.nodeType];
		// TODO : add some shorthand notation describing the filter itself
	}
	$scope.selectFilter = function(filter) {
		$scope.selection.filter = filter;
	};
	$scope.addFilter = function() {
		var filter = new QueryDefinitionObjects.NodeFilter();
		$scope.selection.node.nodeFilters.push(filter);
		$scope.selection.filter = filter;
	};
	$scope.removeFilter = function(filter) {
		if ($scope.selection.filter == filter) {
			$scope.selection.filter = null;
		}
		var idx = $scope.selection.node.nodeFilters.indexOf(filter);
		if (idx > -1) {
			$scope.selection.node.nodeFilters.splice(idx,1);
		}
	};
	$scope.$watch("node",function(node) {
		if ($scope.graphTemplate) {
			$scope.nodeTemplate = $scope.graphTemplate.nodeTemplates.filter(function(tmpl) {return tmpl.id == node.template;})[0];
		}
	});

}]);
