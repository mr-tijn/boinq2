'use strict';

angular.module('boinqApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('routine', {
                abstract: true,
                parent: 'site'
            });
    });
