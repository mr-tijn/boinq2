'use strict';

boinqApp.factory('Datasource', ['$resource',
    function ($resource) {
    	console.info('Registering resource Datasource');
        return $resource('app/rest/datasources/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);

