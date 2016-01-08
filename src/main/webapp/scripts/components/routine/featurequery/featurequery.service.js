//angular.module('boinqApp').factory('FeatureQueryService', ['$http', function ($http) {
//	console.info("Registering factory FeatureQueryService");
//	return {
//		all: function() {
//			var promise = $http.get('app/rest/featurequery').then(function(response) {
//				return response.data;
//			});
//			return promise;
//		},
//		get: function(fqId) {
//			var promise = $http.get('app/rest/featurequery',{params:{fqId:fqId}}).then(function (response) {
//				return response.data;
//			});
//			return promise;
//		},
//		create: function(featureQuery) {
//			var promise = $http.post('app/rest/featurequery',featureQuery).then(function (response) {
//				console.log(response);
//				return response.data;
//			});
//			return promise;
//		},
//		remove: function(fqId) {
//			var promise = $http.delete('app/rest/featurequery/:fqId',{params:{fqId:fqId}});
//			return promise;
//		}
//	};
//}]);



angular.module('boinqApp').factory('FeatureQuery', ['$resource',
function ($resource) {
	console.info('Registering resource FeatureQuery');
	return $resource('app/rest/featurequery/:id', {}, {
		'query': { method: 'GET', isArray: true},
		'get': { method: 'GET' },
		'delete': { method: 'DELETE'},
		'create' : { method: 'POST'}
	});
}]);

