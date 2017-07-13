'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('sparql_browser', {
            parent: 'management',
            url: '/sparql_browser',
            data: {
                roles: [],
                pageTitle: 'sparql_browser.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/sparql_browser/sparql_browser.html',
                    controller: 'SparqlBrowserController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }]
            }
        });
});
