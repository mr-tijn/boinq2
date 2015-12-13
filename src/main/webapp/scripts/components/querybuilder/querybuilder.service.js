angular.module('boinqApp').factory('FeatureQueryService', ['$http', function ($http) {
	console.info("Registering factory FeatureQueryService");
	return {
		get: function() {
			var promise = $http.get('app/rest/featurequery').then(function (response) {
				return response.data;
			});
			return promise;
		},
		create: function(featureQuery) {
			var promise = $http.post('app/rest/featurequery',featureQuery).then(function (response) {
				return response.data;
			});
			return promise;
		}
	};
}]);