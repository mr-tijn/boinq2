'use strict';

boinqApp.factory('Datasource', ['$resource',
    function ($resource) {
    	console.info('Registering resource Datasource');
        return $resource('app/rest/datasources/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
boinqApp.factory('DatasourceDatafile', ['$resource', function($resource) {
    console.info('Registering resource DatasourceDatafile');
	return $resource('app/rest/datasources/:id/rawdatafiles/:data_id', {}, {
		'remove' : {method: 'DELETE'}
	});
}]);
boinqApp.factory('DatasourceConversion', ['$resource', function($resource) {
    console.info('Registering resource DatasourceConversion');
	return $resource('app/rest/datasources/:id/startconversion', {}, {
		'start' : {method: 'PUT'}
	});
}]);

