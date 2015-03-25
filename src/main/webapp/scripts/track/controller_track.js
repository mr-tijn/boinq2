'use strict';

boinqApp.controller('TrackController', ['$scope', 'Track', 'Datasource', 'TrackDatafile', 'TrackConversion', 'FileUploader', 'TrackConstants', 'DatasourceConstants', 
    function ($scope, Track, Datasource, TrackDatafile, TrackConversion, FileUploader, TrackConstants, DatasourceConstants) {
	
		console.log("Loading TrackController");
				
        $scope.create = function () {
            Track.save({ds_id: $scope.datasource.id}, $scope.track,
                function () {
            		// hides datasource from parent scope ?
//            		$scope.datasource = Datasource.get({id: $scope.datasource.id});
                    $scope.datasource.tracks = Track.query({ds_id: $scope.datasource.id});
                    $('#saveTrackModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.track = Track.get({ds_id: $scope.datasource.id, id: id});
            $('#saveTrackModal').modal('show');
        };

        $scope['delete'] = function (id) {
            Track['delete']({ds_id: $scope.datasource.id, id: id},
                function () {
//            		$scope.datasource = Datasource.get({id:$scope.datasource.id});

                    $scope.datasource.tracks = Track.query({ds_id: $scope.datasource.id});
                });
        };

        $scope.clear = function () {
            $scope.track = {id: null};
        };
        
        // management
        
        $scope.managedtrack = null;
        
        $scope.canupload = function(track) {
        	return $scope.managedtrack != null && $scope.datasource.type == DatasourceConstants.TYPE_LOCAL_FALDO;
        };
        $scope.manage= function(track) {
        	$scope.managedtrack = Track.get({ds_id: $scope.datasource.id, id: track.id});
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
        				$scope.managedtrack = Track.get({ds_id:$scope.datasource.id, id:ds_id});
//        				$scope.datasource = Datasource.get({id:$scope.datasource.id});
//        				$scope.tracks = Track.query({ds_id:$scope.datasource.id});
        	});
        };
        $scope.startconversion = function(rawdatafile_id) {
        	var ds_id = $scope.managedtrack.id;
        	TrackConversion.start({ds_id:$scope.datasource.id, id:ds_id}, rawdatafile_id);
        };
        
        $scope.pickTerm = function() {
        	$('#selectTermModal').modal('show');
        };
        $scope.termPicked = function(term) {
        	console.info(term);
        	$scope.track.type = term.uri.value;
        	$('#selectTermModal').modal('hide');
        };
        
        $scope.termsPicked = function(terms) {
        	console.info(terms);
        };
        
        // file uploader stuff
        
        var uploader = $scope.uploader = new FileUploader({
            url: 'upload',
            removeAfterUpload: true
        });

        // FILTERS

        uploader.filters.push({
            name: 'customFilter',
            fn: function(item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            }
        });

        // CALLBACKS

        uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function(fileItem) {
            console.info('onAfterAddingFile', fileItem);
        };
        uploader.onAfterAddingAll = function(addedFileItems) {
            console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function(item) {
        	var filename = item.file.name;
        	var dsurl = 'rest/datasources/' + $scope.datasource.id + '/tracks/' + $scope.managedtrack.id + '/upload';
        	item.formData.push({name: filename});
        	item.url = dsurl;
            console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function(fileItem, progress) {
            console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function(progress) {
            console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function(fileItem, response, status, headers) {
	        //remove fileItem from queue
        	$scope.managedtrack = Track.get({ds_id: $scope.datasource.id, id: $scope.managedtrack.id});
//			$scope.datasource = Datasource.get({id:$scope.datasource.id});

        	console.info('onSuccessItem', $scope.managedtrack );
            console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader.onErrorItem = function(fileItem, response, status, headers) {
            console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader.onCancelItem = function(fileItem, response, status, headers) {
            console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function(fileItem, response, status, headers) {
            console.info('onCompleteItem', fileItem, response, status, headers);
        };
        uploader.onCompleteAll = function() {
            console.info('onCompleteAll');
        };

    }]);
