'use strict';

angular.module('boinqApp').controller('TrackController', ['$scope', 'Track', 'Datasource', 'TrackDatafile', 'TrackConversion', 'FileUploader', 'TrackConstants', 'DatasourceConstants', 'SPARQLConstants', 'QueryBuilderService','callEndpoint','$cookies', 
    function ($scope, Track, Datasource, TrackDatafile, TrackConversion, FileUploader, TrackConstants, DatasourceConstants, SPARQLConstants, QueryBuilderService, callEndpoint, $cookies) {
	
		console.log("Loading TrackController");
		$scope.sourceEndpoint = "/local/sparql"
		$scope.sourceGraph = "http://www.boinq.org/iri/graph/meta/"
		
        $scope.create = function () {
        	console.log($scope.track);
            Track.save({ds_id: $scope.datasource.id}, $scope.track,
                function () {
                    $scope.datasource.tracks = Track.query({ds_id: $scope.datasource.id});
                    $('#saveTrackModal').modal('hide');
                    $scope.clear();
                });
        };
        
        $scope.retrieveMapping = function (){
        	$scope.species=$scope.managedtrack.species;
        	$scope.assembly=$scope.managedtrack.assembly;
        }

        $scope.update = function (id) {
            $scope.track = Track.get({ds_id: $scope.datasource.id, track_id: id});
            $('#saveTrackModal').modal('show');
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
        
        $scope.managedtrack = null;
        $scope.staticEndpoint = SPARQLConstants.STATIC_ENDPOINT;
        
        $scope.canupload = function(track) {
        	return $scope.managedtrack != null && $scope.datasource.type == DatasourceConstants.TYPE_LOCAL_FALDO;
        };
        
        $scope.manage= function(id) {
        	$scope.managedtrack = Track.get({ds_id: $scope.datasource.id, track_id: id}, function(track){
        		$('#manageTrackModal').modal('show');
        	});
        	$('#manageTrackModal').modal('show');
        };

        $scope.process= function(id, rawdatafile, filePath) {
        	$scope.track = Track.get({ds_id: $scope.datasource.id, track_id: id});
        	$scope.fileType =filePath.substring(filePath.length-3,filePath.length);
        	$scope.rawDataFile = rawdatafile;
        	$('#processTrackModal').modal('show');
        };
        
        $scope.canmanage = function(track) {
        	if ($scope.datasource != null) {
        		return $scope.datasource.type == DatasourceConstants.TYPE_LOCAL_FALDO;
        	}
        	return false;
        };
        $scope.isLocal = function(track) {
        	if ($scope.datasource != null) {
        		return $scope.datasource.type == DatasourceConstants.TYPE_LOCAL_FALDO;
        	}
        	return false;
        };
        $scope.removefile = function(rawdatafile_id) {
        	var ds_id = $scope.datasource.id;
        	TrackDatafile.remove({ds_id:$scope.datasource.id, track_id:$scope.managedtrack.id, data_id:rawdatafile_id},
        			function () {
        				$scope.managedtrack = Track.get({ds_id:$scope.datasource.id, track_id:ds_id});
        	});
        };
        
        $scope.startconversion = function(rawdatafile_id) {
        	$scope.track.type=null;
        	if ($scope.mainType!=null){
        		$scope.track.type=$scope.mainType;
        	}
        	if ($scope.subType!=null){
        		$scope.track.type+="|"+$scope.subType;
        	}
        	var ds_id = $scope.track.id;
        	Track.save({ds_id: $scope.datasource.id}, $scope.track,
                    function () {
                        $scope.datasource.tracks = Track.query({ds_id: $scope.datasource.id},
                        		function(successResponse){
                        		   	TrackConversion.start({ds_id:$scope.datasource.id, track_id:ds_id}, rawdatafile_id,
                        		   			function(successResponse){
                        		   			   $('#processTrackModal').modal('hide');
                                               $('#manageTrackModal').modal('hide');
                                               $scope.clear();
                        		   			},function(errorResponse){
                        		   				console.error("Couldn't start conversion"+ errorResponse);
                        		   			});
                        		}, function (errorResponse){                     		  
                        		   	console.error("Couldn't save track"+errorResponse);
                        		});
                    });
                        		   	
                        		   	
                        		    
       
         };

        $scope.mainTypePicker = function(terms) {
        	console.log("main picker")
        	$scope.mainType = terms.uri.value;
        };
        
        $scope.subTypePicker = function(terms) {
        	console.log("sub picker")
        	$scope.subType = terms.uri.value;

        }
         
         
        
        $scope.termsPicked = function(terms) {
        	var uris = "";
        	if (terms[0]==null){
        		uris = terms.uri.value;
        	} else {
        	for (var idx in terms) {
        		var term = terms[idx];
        		uris += "|" + term.uri.value;
        	}}
        	uris = uris.substring(1);
        	$scope.track.type = uris;
        	console.info(uris);
        	$('#selectTermModal').modal('hide');
        };
        
        $scope.getMappings = function() {
    	QueryBuilderService.mappingQuery().then(function(response) {
			var query = response.query;
			console.log("Retrieving supported species");
			callEndpoint($scope.sourceEndpoint, $scope.sourceGraph, query).then(
					function (successResponse) {					
						var results = [];
						var species = successResponse.data.results.bindings;
						for (var idx in species){
							results.push({'uri':species[idx].uri.value, 'label':species[idx].label.value});
						}
						$scope.speciesList = results;
					}, function (errorResponse) {
						console.error(errorResponse);
					});
			});
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
        
            
        // FILE UPLOADER
        
        var uploader = $scope.uploader = new FileUploader({
            url: 'upload',
            removeAfterUpload: true, 
        });

        // FILTERS

        uploader.filters.push({
            name: 'customFilter',
            fn: function(item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            }
        });

        // CALLBACKS
        var refresh = function() {
        	console.log($scope.managedtrack);
        	$scope.managedtrack = Track.get({ds_id: $scope.datasource.id, track_id: $scope.managedtrack.id});
        	console.log($scope.managedtrack);
        }
        uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {};
        uploader.onAfterAddingFile = function(fileItem) {};
        uploader.onAfterAddingAll = function(addedFileItems) {};
        uploader.onBeforeUploadItem = function(item) {
        	var filename = item.file.name;
        	var dsurl = 'rest/datasources/' + $scope.datasource.id + '/tracks/' + $scope.managedtrack.id + '/upload';
        	item.formData.push({name: filename});
        	item.url = dsurl;
        	item.headers['X-CSRF-TOKEN'] = $cookies.get("CSRF-TOKEN");
        };
        uploader.onProgressItem = function(fileItem, progress) {};
        uploader.onProgressAll = function(progress) {};
        uploader.onSuccessItem = function(fileItem, response, status, headers) {
        	refresh();
        };
        uploader.onErrorItem = function(fileItem, response, status, headers) {
        	refresh();
        };
        uploader.onCancelItem = function(fileItem, response, status, headers) {
        	refresh();
        };
        uploader.onCompleteItem = function(fileItem, response, status, headers) {};
        uploader.onCompleteAll = function() {};

    }]);
