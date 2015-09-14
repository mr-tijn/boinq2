'use strict';

angular.module('boinqApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
