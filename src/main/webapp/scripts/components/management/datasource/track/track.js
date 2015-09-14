'use strict';

angular.module('boinqApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('track', {
            parent: 'datasource',
            url: '/datasource/track',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'track.title'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/components/management/datasource/track/tracks.html',
                    controller: 'TrackController'
                }
            },
            resolve: {
            	resolvedTrack: ['Track', function (Track) {
                    return Track.query();
                }]
            }
        });
});
