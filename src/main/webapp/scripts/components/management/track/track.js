'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('track', {
            parent: 'management',
            url: '/datasource/{dsId}/tracks',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'track.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/management/track/tracks.html',
                    controller: 'TrackController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('main');
                    return $translate.refresh();
                }],	
            	resolvedTracks: ['Track', '$stateParams', function(Track,$stateParams) {
                    	if ($stateParams.dsId != null) {
                    		return Track.queryAll({ds_id: $stateParams.dsId}).$promise;
                    	} else {
                    		return null;
                    	}
                    }],
                resolvedDatasource: ['Datasource', '$stateParams', function(Datasource, $stateParams) {
                	if ($stateParams.dsId != null) {
                		return Datasource.get({id: $stateParams.dsId}).$promise
                	}
                }]
//            	resolvedTrack: ['Track', function (Track) {
//                    return Track.query().$promise;
//                }]
            }
        });
});
