
angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('job', {
            parent: 'management',
            url: '/job',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'job.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/management/job/jobs.html',
                    controller: 'JobController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }],	
            	resolvedJob: ['Job', function (Job) {
                    return Job.query();
                }]
            }
        });
});
