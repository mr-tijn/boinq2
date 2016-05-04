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
                resolvedFeatureQuery: ['FeatureQuery', '$stateParams', function(FeatureQuery,$stateParams) {
                	if ($stateParams.fqId != null) {
                		return FeatureQuery.get({id: $stateParams.fqId}).$promise;
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
