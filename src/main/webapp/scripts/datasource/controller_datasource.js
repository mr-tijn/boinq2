'use strict';

boinqApp.controller('DatasourceController', ['$scope', 'resolvedDatasource', 'Datasource', 'DatasourceConstants', 
    function ($scope, resolvedDatasource, Datasource, DatasourceConstants) {

	
		$scope.typeItems = DatasourceConstants.TYPE_ITEMS;
		
        $scope.datasources = resolvedDatasource;
        $scope.manageddatasource = null;

        $scope.create = function () {
            Datasource.save($scope.datasource,
                function () {
                    $scope.datasources = Datasource.query();
                    $('#saveDatasourceModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.datasource = Datasource.get({id: id});
            $('#saveDatasourceModal').modal('show');
        };

        $scope['delete'] = function (id) {
            Datasource['delete']({id: id},
                function () {
                    $scope.datasources = Datasource.query();
                });
        };

        $scope.clear = function () {
            $scope.datasource = {id: null, endpointUrl: null, type: null};
            $scope.manageddatasource = null;
        };
        
        $scope.manage = function(datasource) {
        	$scope.manageddatasource = Datasource.get({id:datasource.id});
        };

        $scope.canmanage = function(datasource) {
        	return true;
        };
        
        console.info('Loaded controller_datasources', DatasourceConstants.TYPE_ITEMS);
        
    }]);
