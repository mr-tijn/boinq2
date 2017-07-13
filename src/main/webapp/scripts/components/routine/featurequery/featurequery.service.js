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

angular.module('boinqApp').factory('FeatureQueryManagement', ['$http', function ($http) {
	console.info("Registering factory FeatureQueryManagement");
	return {
		start: function(fqId) {
			var promise = $http.post('app/rest/featurequery/'+fqId+'/start','1').then(function (response) {
				return response.data;
			});
			return promise;
		},
	}
}]);

