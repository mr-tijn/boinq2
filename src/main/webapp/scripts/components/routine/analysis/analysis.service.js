angular.module('boinqApp').factory('Analysis', ['$resource',
    function ($resource) {
    	console.info('Registering resource Analysis');
        return $resource('app/rest/analysis/:id', {}, {
            'query': { method: 'GET', isArray: true},
//            'get': { method: 'GET'}
        });
    }]);
