'use strict';

angular.module('boinqApp').factory('Track', ['$resource', function ($resource) {
	console.info("Registering resource Track");
	return $resource('app/rest/datasources/:ds_id/tracks/:track_id', {}, {
		'query': { method: 'GET', isArray: true},
		'queryAll' : { 
			method: 'GET', 
			url: 'app/rest/datasources/:ds_id/tracks', 
			isArray:true, 
		},
		'empty' : {
			method: 'GET',
			url: 'app/rest/datasources/:ds_id/tracks/:track_id/empty'
		},
		'get': { method: 'GET'}
	});
}]);

angular.module('boinqApp').factory('TrackDatafile', ['$resource', function($resource) {
    console.info('Registering resource TrackDatafile');
	return $resource('app/rest/datasources/:ds_id/tracks/:track_id/rawdatafiles/:data_id', {}, {
		'remove' : {method: 'DELETE'}
	});
}]);

angular.module('boinqApp').factory('TrackConversion', ['$resource', function($resource) {
    console.info('Registering resource TrackConversion');
	return $resource('app/rest/datasources/:ds_id/tracks/:track_id/startconversion', {}, {
		'start' : {method: 'PUT'}
	});
}]);