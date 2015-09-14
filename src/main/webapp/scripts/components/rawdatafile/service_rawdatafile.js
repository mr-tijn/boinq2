'use strict';

boinqApp.factory('RawDataFile', ['$resource',
    function ($resource) {
        return $resource('app/rest/rawdatafiles/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
