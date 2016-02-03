'use strict';

angular.module('boinqApp').controller('FeatureQueryController', ['$scope', 'resolvedFeaturequeries', 'FeatureQuery','FeatureQueryManagement',
function ($scope, resolvedFeaturequeries,FeatureQuery,FeatureQueryManagement) {
	$scope.featurequeries = resolvedFeaturequeries;
	$scope.remove = function(fqId) {
		FeatureQuery['delete']({id:fqId}).$promise.then(function () {
			$scope.featurequeries = FeatureQuery.query();
		});
	}
	$scope.start = function(fqId) {
		FeatureQueryManagement.start(fqId);
	}
}]);

