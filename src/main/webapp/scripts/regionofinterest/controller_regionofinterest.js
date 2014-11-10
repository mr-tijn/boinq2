'use strict';

boinqApp.controller('RegionOfInterestController', ['$scope', 'resolvedRegionOfInterest', 'RegionOfInterest',
    function ($scope, resolvedRegionOfInterest, RegionOfInterest) {

        $scope.regionofinterests = resolvedRegionOfInterest;

        $scope.create = function () {
            RegionOfInterest.save($scope.regionofinterest,
                function () {
                    $scope.regionofinterests = RegionOfInterest.query();
                    $('#saveRegionOfInterestModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.regionofinterest = RegionOfInterest.get({id: id});
            $('#saveRegionOfInterestModal').modal('show');
        };

        $scope.delete = function (id) {
            RegionOfInterest.delete({id: id},
                function () {
                    $scope.regionofinterests = RegionOfInterest.query();
                });
        };

        $scope.clear = function () {
            $scope.regionofinterest = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
