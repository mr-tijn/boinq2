'use strict';

angular.module('boinqApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


