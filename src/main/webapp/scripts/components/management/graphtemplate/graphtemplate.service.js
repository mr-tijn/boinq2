angular.module('boinqApp').factory('GraphTemplate', ['$resource',
function ($resource) {
	console.info('Registering resource GraphTemplate');
	return $resource('app/rest/graphtemplate/:id', {}, {
		'query': { method: 'GET', isArray: true},
		'get': { method: 'GET' },
//		'delete': { method: 'DELETE'},
		'save' : { method: 'POST'}
	});
}]);

