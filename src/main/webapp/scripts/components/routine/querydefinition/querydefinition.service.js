angular.module('boinqApp').factory('QueryDefinition', ['$resource',
function ($resource) {
	console.info('Registering resource QueryDefinition');
	return $resource('app/rest/querydefinition/:id', {}, {
		'query': { method: 'GET', isArray: true},
		'get': { method: 'GET' },
		'delete': { method: 'DELETE'},
		'create' : { method: 'POST'}
	});
}]);

angular.module('boinqApp').factory('QueryManagement', ['$http', function ($http) {
	console.info("Registering factory QueryManagement");
	return {
		generatequery: function(qdId) {
			var promise = $http.post('app/rest/querydefinition/'+qdId+'/generatequery','1').then(function(response) {
				return response.data;
			});
			return promise;
		},
		updatequery: function(qdId, query) {
			var promise = $http.post('app/rest/querydefinition/'+qdId+'/updatequery',query).then(function(response) {
				return response.data;
			});
			return promise;
		},
		emptyquery: function(qdId) {
			var promise = $http['delete']('app/rest/querydefinition/'+qdId+'/removequery').then(function(response) {
				return response.data;
			});
			return promise;

		},
		start: function(qdId) {
			var promise = $http.post('app/rest/querydefinition/'+qdId+'/start','1').then(function (response) {
				return response.data;
			});
			return promise;
		},
	}
}]);