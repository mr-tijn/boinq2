'use strict';

angular.module('boinqApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('management', {
                abstract: true,
                parent: 'site'
            });
    });
