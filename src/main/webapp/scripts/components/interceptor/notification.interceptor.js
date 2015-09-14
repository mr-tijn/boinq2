 'use strict';

angular.module('boinqApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-boinqApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-boinqApp-params')});
                }
                return response;
            },
        };
    });