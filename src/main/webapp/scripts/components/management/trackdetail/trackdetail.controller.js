	'use strict';

angular.module('boinqApp').controller('TrackDetailController', ['$scope', 'FileUploader', 'Track', 'resolvedTrack', 'resolvedDatasource', 'TrackDatafile', 'ServerDataFile', 'TrackConversion', 'SPARQLConstants', 'DatasourceConstants', 'DataFileConstants', 'GlobalConstants', 'TrackConstants', 'QueryBuilderService','callEndpoint','$cookies', 
	function ($scope, FileUploader, Track, resolvedTrack, resolvedDatasource, TrackDatafile, ServerDataFile, TrackConversion, SPARQLConstants, DatasourceConstants, DataFileConstants, GlobalConstants, TrackConstants, QueryBuilderService, callEndpoint, $cookies) {

	console.log("Loading TrackDetailController");
	$scope.sourceEndpoint = "/local/sparql"
	$scope.sourceGraph = "http://www.boinq.org/iri/graph/meta/"

	$scope.mainType = null;
	$scope.subType = null;
	$scope.attributeType = null;

	$scope.track = resolvedTrack;
	$scope.datasource = resolvedDatasource;
	$scope.supportedSpecies = GlobalConstants.supportedSpecies;
	
	$scope.$watch('track.species', function() {
		if ($scope.track && $scope.track.species != null) {
			$scope.getAssemblies($scope.track.species);
		}
	});

	$scope.save = function(){
		Track.save({ds_id: $scope.datasource.id}, $scope.track, null, 
			function (errorResponse) {
				console.error("Couldn't save track"+errorResponse);
			});
	};
	
	// management

	$scope.staticEndpoint = SPARQLConstants.STATIC_ENDPOINT;

	$scope.canupload = function(track) {
		return $scope.track != null && $scope.datasource.type == DatasourceConstants.TYPE_LOCAL_FALDO;
	};

	$scope.iscomplete = function(track) {
		return $scope.track.status == TrackConstants.STATUS_DONE;
	};

	$scope.hasError = function(rawDataFile) {
		return (rawDataFile.status==DataFileConstants.STATUS_ERROR);
	};

	
	// directly on server
	$scope.addServerFile = function() {
		$scope.newServerFilePath = "full path to file on server";
		$('#addServerFile').modal('show');
	};
	
	$scope.saveServerFile = function() {
		$('#addServerFile').modal('hide');
		ServerDataFile.add({ds_id: $scope.datasource.id, track_id:$scope.track.id},$scope.newServerFilePath,function(successResponse) {
			refresh();
		});
	};
	
	$scope.showFileError = function(message) {
		$scope.fileError = message;
		$('#fileError').modal('show');
	};

	$scope.process= function(id, rawdatafile, filePath) {
		$scope.track = Track.get({ds_id: $scope.datasource.id, track_id: id});
		$scope.fileType =filePath.substring(filePath.length-3,filePath.length);
		$scope.rawDataFile = rawdatafile;
		$('#conversion').modal('show');
	};

	$scope.confirmempty = function() {
		$('#confirmEmpty').modal('show');
	}

	$scope.startempty = function() {
		$scope.track = Track.empty({ds_id: $scope.datasource.id, track_id:$scope.track.id});
		$('#confirmEmpty').modal('hide');
	}

	$scope.confirmconversion = function() {
		$('#confirmConversion').modal('show');
	}

	$scope.isfaldo = function() {
		return $scope.track.type == 'faldo';
	};
	
	$scope.startconversion = function() {
//		if (!$scope.track.supportedFeatureTypes) {
//			$scope.track.supportedFeatureTypes = [];
//		}
//		if ($scope.mainType!=null){
//			$scope.track.supportedFeatureTypes.push($scope.mainType);
//		}
//		if ($scope.subType!=null){
//			$scope.track.supportedFeatureTypes.push($scope.subType);
//		}
		Track.save({ds_id: $scope.datasource.id}, $scope.track,
			function () {
				$scope.datasource.tracks = Track.query({ds_id: $scope.datasource.id},
						function(successResponse){
							console.log({ds_id:$scope.datasource.id, track_id:$scope.track.id, mainType:$scope.mainType, subType:$scope.subType, attributeType:$scope.attributeType});
							TrackConversion.start({ds_id:$scope.datasource.id, track_id:$scope.track.id, mainType:$scope.mainType, subType:$scope.subType, attributeType:$scope.attributeType}, [],
									function(successResponse){
										$('#confirmConversion').modal('hide');
									},function(errorResponse){
										console.error("Couldn't start conversion"+ errorResponse);
									});
						}, function (errorResponse){
							console.error("Couldn't save track"+errorResponse);
						});
			});                        		   	
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
		TrackDatafile.remove({ds_id:$scope.datasource.id, track_id:$scope.track.id, data_id:rawdatafile_id},
				function () {
			Track.get({ds_id:$scope.datasource.id, track_id:$scope.track.id}, function(result) {
				$scope.track = result;
			});
		});
	};


	$scope.mainTypePicker = function(terms) {
		$scope.mainType = terms.uri.value;
	};

	$scope.subTypePicker = function(terms) {
		$scope.subType = terms.uri.value;
	};
	
	$scope.attributeTypePicker = function(terms) {
		$scope.attributeType = terms.uri.value;
	};
	

	
	$scope.getMapping = function() {
		var flatten = function(string) {
			return string.toLowerCase().replace(/ /g,"_");
		}
		if ($scope.track && $scope.track.species && $scope.track.assembly) {
			return "http://www.boinq.org/resource/" + flatten($scope.track.species) + "/" + flatten($scope.track.assembly) +"/1";
		} else {
			return "http://www.boinq.org/resource/UNKNOWN";
		}
	}

	$scope.termsPicked = function(terms) {
		var uris = "";
		if (terms[0]==null) {
			uris = terms.uri.value;
		} else {
			for (var idx in terms) {
				var term = terms[idx];
				uris += "|" + term.uri.value;
			}
		}
		uris = uris.substring(1);
		$scope.track.type = uris;
		console.info(uris);
		$('#selectTermModal').modal('hide');
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
		Track.get({ds_id: $scope.datasource.id, track_id: $scope.track.id},function(result) {
			$scope.track = result;
		});
	}
	uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {};
	uploader.onAfterAddingFile = function(fileItem) {};
	uploader.onAfterAddingAll = function(addedFileItems) {};
	uploader.onBeforeUploadItem = function(item) {
		var filename = item.file.name;
		var dsurl = 'rest/datasources/' + $scope.datasource.id + '/tracks/' + $scope.track.id + '/upload';
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
