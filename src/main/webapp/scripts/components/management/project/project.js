'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('project', {
            parent: 'management',
            url: '/project',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'project.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/management/project/projects.html',
                    controller: 'ProjectController'
                }
            },
            resolve: {
            	resolvedProject: ['Project', function (Project) {
                    return Project.query();
                }]
            }
        });
});
