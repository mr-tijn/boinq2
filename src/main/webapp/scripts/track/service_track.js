'use strict';

boinqApp.factory('Track', ['$resource', function ($resource) {
	console.info("Registering resource Track");
	return $resource('app/rest/datasources/:ds_id/tracks/:id', {}, {
		'query': { method: 'GET', isArray: true},
		'queryAll' : { method: 'GET', url: 'app/rest/tracks/:id', isArray:true},
		'get': { method: 'GET'}
	});
}]);

boinqApp.factory('TrackDatafile', ['$resource', function($resource) {
    console.info('Registering resource TrackDatafile');
	return $resource('app/rest/datasources/:ds_id/tracks/:track_id/rawdatafiles/:data_id', {}, {
		'remove' : {method: 'DELETE'}
	});
}]);

boinqApp.factory('TrackConversion', ['$resource', function($resource) {
    console.info('Registering resource TrackConversion');
	return $resource('app/rest/datasources/:ds_id/tracks/:id/startconversion', {}, {
		'start' : {method: 'PUT'}
	});
}]);