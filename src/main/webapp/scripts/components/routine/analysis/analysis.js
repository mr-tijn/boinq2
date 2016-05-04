
angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('analysis', {
            parent: 'routine',
            url: '/analysis',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'analysis.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/routine/analysis/analysis.html',
                    controller: 'AnalysisController'
                }
            },
            resolve: {
            	resolvedAnalysis: ['Analysis', function (Analysis) {
                    return Analysis.query();
                }],
                resolvedGlobals: ['boinqOneTimeFetch', function (globals) {
                	return globals;
                }]
            }
        });
});
