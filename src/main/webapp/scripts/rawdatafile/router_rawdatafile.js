'use strict';

boinqApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/rawdatafile', {
                    templateUrl: 'views/rawdatafiles.html',
                    controller: 'RawDataFileController',
                    resolve:{
                        resolvedRawDataFile: ['RawDataFile', function (RawDataFile) {
                            return RawDataFile.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
