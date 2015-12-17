angular.module('boinqApp').factory('FeatureQueryService', ['$http', function ($http) {
	console.info("Registering factory FeatureQueryService");
	return {
		get: function(fqId) {
			var promise = $http.get('app/rest/featurequery',{params:{fqId:fqId}}).then(function (response) {
				return response.data;
			});
			return promise;
		},
		create: function(featureQuery) {
			var promise = $http.post('app/rest/featurequery',featureQuery).then(function (response) {
				console.log(response);
				return response.data;
			});
			return promise;
		}
	};
}]);