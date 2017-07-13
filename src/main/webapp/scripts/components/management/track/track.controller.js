'use strict';

angular.module('boinqApp').controller('TrackController', ['$scope', 'resolvedTracks', 'resolvedDatasource', 'Track', 'Datasource', 'GlobalConstants', 'TrackConstants', 'DatasourceConstants', 'SPARQLConstants', 'QueryBuilderService','callEndpoint','$cookies', 
    function ($scope, resolvedTracks, resolvedDatasource, Track, Datasource, GlobalConstants,  TrackConstants, DatasourceConstants, SPARQLConstants, QueryBuilderService, callEndpoint, $cookies) {
	
		console.log("Loading TrackController");
		$scope.sourceEndpoint = "/local/sparql"
		$scope.sourceGraph = "http://www.boinq.org/iri/graph/meta/"
		$scope.supportedSpecies = GlobalConstants.supportedSpecies;
		
		$scope.$watch('track.species', function() {
			if ($scope.track && $scope.track.species != null) {
				$scope.getAssemblies($scope.track.species);
			}
		});
		
			
		$scope.datasource = resolvedDatasource;
		$scope.tracks = resolvedTracks;
			
        $scope.create = function () {
        	console.log($scope.track);
            Track.save({ds_id: $scope.datasource.id}, $scope.track,
                function () {
                    $scope.datasource.tracks = Track.query({ds_id: $scope.datasource.id});
                    $('#saveTrackModal').modal('hide');
                    $scope.clear();
                });
        };
        
        $scope['delete'] = function (id) {
            Track['delete']({ds_id: $scope.datasource.id, track_id: id},
                function () {
                    $scope.datasource.tracks = Track.query({ds_id: $scope.datasource.id});
                });
        };

        $scope.clear = function () {
            $scope.track = {id: null};
        };
        
        // management
        
        $scope.staticEndpoint = SPARQLConstants.STATIC_ENDPOINT;
        
   
        $scope.isLocal = function(track) {
        	if ($scope.datasource != null) {
        		return $scope.datasource.type == DatasourceConstants.TYPE_LOCAL_FALDO;
        	}
        	return false;
        };
 
        $scope.getAssemblies = function(speciesLabel) {
        	QueryBuilderService.assemblyQuery(speciesLabel).then(function(response) {
    			var query = response.query;
    			console.log("Retrieving Assemblies");
    			callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
    					function (successResponse) {					
    						var results = [];
    						var assembly = successResponse.data.results.bindings;
    						for (var idx in assembly){
    							results.push({'uri':assembly[idx].uri.value, 'label':assembly[idx].label.value});
    						}
    						$scope.assemblyList = results;
    					}, function (errorResponse) {
    						console.error(errorResponse);
    					});
    			});
            };
        
 
    }]);
