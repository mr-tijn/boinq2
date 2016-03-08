'use strict';

angular.module('boinqApp').controller('TrackController', ['$scope', 'Track', 'Datasource', 'TrackDatafile', 'TrackConversion', 'FileUploader', 'TrackConstants', 'DatasourceConstants', 'SPARQLConstants', '$cookies', 
    function ($scope, Track, Datasource, TrackDatafile, TrackConversion, FileUploader, TrackConstants, DatasourceConstants, SPARQLConstants, $cookies) {
	
		console.log("Loading TrackController");
				
        $scope.create = function () {
        	console.log($scope.track);
            Track.save({ds_id: $scope.datasource.id}, $scope.track,
                function () {
                    $scope.datasource.tracks = Track.query({ds_id: $scope.datasource.id});
                    $('#saveTrackModal').modal('hide');
                    $scope.clear();
                });
        };

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
        $scope.manage= function(track) {
        	$scope.managedtrack = Track.get({ds_id: $scope.datasource.id, track_id: track.id});
        	$('#manageTrackModal').modal('show');
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
        	var ds_id = $scope.managedtrack.id;
        	TrackConversion.start({ds_id:$scope.datasource.id, track_id:ds_id}, rawdatafile_id);
        };
        
        
        
        $scope.termsPicked = function(terms) {
        	var uris = "";
        	for (var idx in terms) {
        		var term = terms[idx];
        		uris += "|" + term.uri.value;
        	}
        	uris = uris.substring(1);
        	$scope.track.type = uris;
        	console.info(uris);
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
