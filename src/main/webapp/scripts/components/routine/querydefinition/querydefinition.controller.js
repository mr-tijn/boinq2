'use strict';

angular.module('boinqApp').controller('QueryDefinitionController', ['$scope', 'resolvedQueryDefinitions', 'QueryDefinition','QueryManagement',
function ($scope, resolvedQueryDefinitions,QueryDefinition, QueryManagement) {
	$scope.queryDefinitions = resolvedQueryDefinitions;
	$scope.remove = function(qdId) {
		QueryDefinition['delete']({id:qdId}).$promise.then(function () {
			$scope.refresh();
		});
	};
	$scope.start = function(qdId) {
		QueryManagement.start(qdId);
	};
	$scope.refresh = function() {
		$scope.queryDefinitions = QueryDefinition.query();
	}
}]);

