angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('graphbuilder', {
            parent: 'management',
            url: '/graphbuilder?{qdId}',
            data: {
                roles: ['ROLE_USER'],
                pageTitle: 'graphbuilder.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/graphbuilder/graphbuilder.html',
                    controller: 'GraphBuilderController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }],	
               	resolvedDatasources: ['Datasource', function (Datasource) {
                    return Datasource.query().$promise;
                }],
                resolvedQueryDefinition: ['QueryDefinition', '$stateParams', 'QueryDefinitionObjects', function(QueryDefinition,$stateParams,QDC) {
                	if ($stateParams.qdId != null) {
                		return QueryDefinition.get({id: $stateParams.qdId}).$promise;
                	} else {
                		return new QDC.QueryDefinition();
                	}
                }],
                resolvedAccount: ['Principal', function(Principal) {
                	return Principal.identity();
                }]
            }
        });
});
