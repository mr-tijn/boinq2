'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('trackdetail', {
            parent: 'management',
            url: '/datasource/{dsId}/track/{id}',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'trackdetail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/management/trackdetail/trackdetail.html',
                    controller: 'TrackDetailController'
                }
            },
            resolve: {
            	
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }],	
            	resolvedTrack: ['Track', '$stateParams', function(Track,$stateParams) {
                    	if ($stateParams.id != null) {
                    		return Track.get({track_id: $stateParams.id, ds_id: $stateParams.dsId}).$promise;
                    	} else {
                    		return null;
                    	}
                    }],
                resolvedDatasource: ['Datasource', '$stateParams' , function(Datasource, $stateParams) {
                	if ($stateParams.dsId != null) {
                		return Datasource.get({id:$stateParams.dsId}).$promise;
                	}
                }]
            }
        });
});



