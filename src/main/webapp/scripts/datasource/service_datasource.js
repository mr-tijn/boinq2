'use strict';

boinqApp.factory('Datasource', ['$resource',
    function ($resource) {
        return $resource('app/rest/datasources/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
