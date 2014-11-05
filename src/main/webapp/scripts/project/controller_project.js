'use strict';

boinqApp.controller('ProjectController', ['$scope', 'resolvedProject', 'Project', 'Datasource', 
    function ($scope, resolvedProject, Project, Datasource) {

        $scope.projects = resolvedProject;
        $scope.datasources = Datasource.query();
        console.info($scope.datasources);
        $scope.datasourceToAdd = {};
        
        $scope.create = function () {
            Project.save($scope.project,
                function () {
                    $scope.projects = Project.query();
                    $('#saveProjectModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.project = Project.get({id: id});
            $('#saveProjectModal').modal('show');
        };

        $scope.delete = function (id) {
            Project.delete({id: id},
                function () {
                    $scope.projects = Project.query();
                });
        };

        $scope.clear = function () {
            $scope.project = {id: null, title: "Enter name", datasources: []};
        };
        
        $scope.removeDatasourceFromProject = function(datasource, project) {
        	var newDatasources = [];
        	for (var idx in project.datasources) {
        		var ds = project.datasources[idx];
        		if (ds.id != datasource.id) newDatasources.push(ds);
        	}
        	project.datasources = newDatasources;
        }
        
        $scope.addDatasourceToProject = function(dsId,project) {
        	var datasource = null;
        	for (var idx in $scope.datasources) {
        		var ds = $scope.datasources[idx];
        		if (dsId == ds.id) datasource = ds;
        	}
        	if (datasource == null) return;
        	for (var idx in project.datasources) {
        		var ds = project.datasources[idx];
            	console.info('already in');
            	console.info(ds.id);
        		if (ds.id == dsId) return; 
        	}
        	console.info('adding');
        	console.info(datasource);
        	project.datasources.push(datasource); //this is a string ???
        	console.info('all');
        	console.info(project);
        }
        
        
    }]);
