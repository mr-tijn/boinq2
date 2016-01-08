'use strict';

angular.module('boinqApp').controller('FeatureQueryController', ['$scope', 'resolvedFeaturequeries', 'FeatureQuery', 
function ($scope, resolvedFeaturequeries,FeatureQuery) {
	$scope.featurequeries = resolvedFeaturequeries;
	$scope.remove = function(fqId) {
		FeatureQuery['delete']({id:fqId}).$promise.then(function () {
			$scope.featurequeries = FeatureQuery.query();
		});
	}
	$scope.start = function(fqId) {
		console.error("not yet implemented");
	}
}]);

