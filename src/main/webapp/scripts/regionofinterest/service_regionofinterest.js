'use strict';

boinqApp.factory('RegionOfInterest', ['$resource',
    function ($resource) {
        return $resource('app/rest/regionofinterests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
