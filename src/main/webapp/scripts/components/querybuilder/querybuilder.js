angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('querybuilder', {
            parent: 'management',
            url: '/querybuilder?{fqId:[0-9]}',
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
                    return Datasource.query().$promise;
                }],
                resolvedFeatureQuery: ['FeatureQueryService', '$stateParams', function(FeatureQueryService,$stateParams) {
                	if ($stateParams.fqId != null) {
                		return FeatureQueryService.get($stateParams.fqId);
                	} else {
                		return null;
                	}
                }],
                resolvedAccount: ['Principal', function(Principal) {
                	return Principal.identity();
                }]	
            }
        });
});
