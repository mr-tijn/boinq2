'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('featurequery', {
            parent: 'routine',
            url: '/featurequery',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'featurequery.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/routine/featurequery/featurequery.html',
                    controller: 'FeatureQueryController'
                }
            },
            resolve: {
            	resolvedFeaturequeries: ['FeatureQuery', function (FeatureQuery) {
                    return FeatureQuery.query().$promise;
                }]
            }
        });
});
