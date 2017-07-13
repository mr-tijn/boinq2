'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('graphtemplate', {
            parent: 'management',
            url: '/datasource/:datasourceId/track/:trackId/graphtemplate/:id',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'graphtemplate.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/management/graphtemplate/graphtemplate.html',
                    controller: 'GraphTemplateController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }],	
            	resolvedGraphTemplate: ['GraphTemplate', '$stateParams', function (GraphTemplate, $stateParams) {
            		if ($stateParams.id) {
            			return GraphTemplate.get({id:$stateParams.id});
            		}
                }]
            }
        });
});
