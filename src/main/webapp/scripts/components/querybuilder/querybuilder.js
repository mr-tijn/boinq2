angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('querybuilder', {
            parent: 'management',
            url: '/querybuilder',
            data: {
                roles: [],
                pageTitle: 'querybuilder.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/querybuilder/querybuilder.html',
                    controller: 'QueryBuilderController'
                }
            },
            resolve: {
            	resolvedDatasource: ['Datasource', function (Datasource) {
                    return Datasource.query();
                }]
            }
        });
});
