'use strict';

boinqApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/datasource', {
                    templateUrl: 'views/datasources.html',
                    controller: 'DatasourceController',
                    resolve:{
                        resolvedDatasource: ['Datasource', function (Datasource) {
                            return Datasource.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.admin]
                    }
                })
        }]);
