'use strict';

angular.module('boinqApp').controller("GraphBuilderController",['$scope','dragging','boinqOneTimeFetch','resolvedQueryDefinition','QueryDefinition','QueryManagement','QueryDefinitionObjects','resolvedDatasources','$state',function($scope,dragging,boinqOneTimeFetch,resolvedQueryDefinition,QueryDefinition,QueryManagement,QueryDefinitionObjects,resolvedDatasources,$state) {

	console.log("Registering controller GraphBuilderController");
	
	$scope.datasources = resolvedDatasources;
	$scope.queryDefinition = resolvedQueryDefinition;
	
	$scope.selection = {
			mode: null,
			graph: null,
			bridge: null
	};
	
	var cleanup = function(queryDefinition) {
		var result =JSON.parse(JSON.stringify(queryDefinition));
		for (var i = 0; i < result.queryGraphs.length; i++) {
			// drop non selected
			// compute filter type
			var drop = [];
			for (var j = 0; j < result.queryGraphs[i].queryEdges.length; j++) {
				var queryEdge = result.queryGraphs[i].queryEdges[j];
				if (queryEdge.hasOwnProperty('selected') && !queryEdge.selected) {
					drop.push(j);
				}
			}
			for (var j = 0; j < drop.length; j++) {
				result.queryGraphs[i].queryEdges.splice(drop[j], 1);
			}
			drop = [];
			for (var j = 0; j < result.queryGraphs[i].queryNodes.length; j++) {
				var queryNode = result.queryGraphs[i].queryNodes[j];
				if (queryEdge.hasOwnProperty('selected') && !queryNode.selected) {
					drop.push(j);
				} 
			}
			for (var j = 0; j < drop.length; j++) {
				result.queryGraphs[i].queryNodes.splice(drop[j], 1);
			}
			
		}
		return result;
	};
	
	
	$scope.save = function() {
		QueryDefinition.save({},cleanup($scope.queryDefinition), function(result) {
			console.log("Saved QueryDefinition" + result);
			$scope.queryDefinition = result;
		}, function(error) {
			console.log(error);
		});
	};
	
	$scope.showquerywindow = function() {
    	$('#queryModal').modal('show');
	};
	
	$scope.generatequery = function() {
		QueryDefinition.save({},cleanup($scope.queryDefinition), function(result) {
			console.log("Saved QueryDefinition" + result);
			$scope.queryDefinition = result;
			QueryManagement.generatequery($scope.queryDefinition.id).then(function(successResponse) {
				$scope.queryDefinition.sparqlQuery = successResponse;
			});
		});
	}
	
	$scope.emptyquery = function() {
		QueryManagement.emptyquery($scope.queryDefinition.id).then(function () {
			$scope.refresh();
		});
	};
	$scope.updatequery = function() {
		QueryManagement.updatequery($scope.queryDefinition.id, $scope.queryDefinition.sparqlQuery).then(function () {
			$scope.refresh();
		});
	};
	$scope.refresh = function() {
		QueryDefinition.get({id:$scope.queryDefinition.id}).$promise.then(
				function(successResponse) {
					$scope.queryDefinition = successResponse;
				});
	};

	
}]);