'use strict';

boinqApp.controller('RawDataFileController', ['$scope', 'resolvedRawDataFile', 'RawDataFile',
    function ($scope, resolvedRawDataFile, RawDataFile) {

        $scope.rawdatafiles = resolvedRawDataFile;

        $scope.create = function () {
            RawDataFile.save($scope.rawdatafile,
                function () {
                    $scope.rawdatafiles = RawDataFile.query();
                    $('#saveRawDataFileModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.rawdatafile = RawDataFile.get({id: id});
            $('#saveRawDataFileModal').modal('show');
        };

        $scope.delete = function (id) {
            RawDataFile.delete({id: id},
                function () {
                    $scope.rawdatafiles = RawDataFile.query();
                });
        };

        $scope.clear = function () {
            $scope.rawdatafile = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
