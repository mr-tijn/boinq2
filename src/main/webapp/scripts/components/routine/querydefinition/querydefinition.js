'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('querydefinition', {
            parent: 'routine',
            url: '/querydefinition',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'querydefinition.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/routine/querydefinition/querydefinition.html',
                    controller: 'QueryDefinitionController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }],	
            	resolvedQueryDefinitions: ['QueryDefinition', function (QueryDefinition) {
                    return QueryDefinition.query().$promise;
                }]
            }
        });
});
