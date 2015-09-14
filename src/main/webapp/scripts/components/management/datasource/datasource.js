'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('datasource', {
            parent: 'management',
            url: '/datasource',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'datasource.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/management/datasource/datasources.html',
                    controller: 'DatasourceController'
                }
            },
            resolve: {
            	resolvedDatasource: ['Datasource', function (Datasource) {
                    return Datasource.query();
                }]
            }
        });
});
