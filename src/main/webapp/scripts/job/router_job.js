boinqApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/job', {
                    templateUrl: 'views/jobs.html',
                    controller: 'JobController',
                    resolve:{
                        resolvedDatasource: ['Job', function (Datasource) {
                            return Datasource.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.admin]
                    }
                })
        }]);
