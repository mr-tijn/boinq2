angular.module('boinqApp').factory('FeatureQueryService', ['$http', function ($http) {
	console.info("Registering factory FeatureQueryService");
	return {
		getFeatureQueries: function() {
			var promise = $http.get('app/rest/featureQuery').then(function (response) {
				return response.data;
			});
			return promise;
		},
		createFeatureQuery: function(featureQuery) {
			var promise = $http.put('app/rest/featureQuery',featureQuery).then(function (response) {
				return response.data;
			});
			return promise;
		}
	};
}]);