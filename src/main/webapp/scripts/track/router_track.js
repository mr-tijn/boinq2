'use strict';

boinqApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/track', {
                    templateUrl: 'views/tracks.html',
                    controller: 'TrackController',
                    resolve:{
                        resolvedTrack: ['Track', function (Track) {
                            return Track.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                });
        }]);
