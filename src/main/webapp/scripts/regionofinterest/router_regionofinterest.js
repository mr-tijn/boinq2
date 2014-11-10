'use strict';

boinqApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/regionofinterest', {
                    templateUrl: 'views/regionofinterests.html',
                    controller: 'RegionOfInterestController',
                    resolve:{
                        resolvedRegionOfInterest: ['RegionOfInterest', function (RegionOfInterest) {
                            return RegionOfInterest.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
