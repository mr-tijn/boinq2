'use strict';

boinqApp.controller('ProjectController', ['$scope', 'resolvedProject', 'Project', 'Track', 
    function ($scope, resolvedProject, Project, Track) {

        $scope.projects = resolvedProject;
        $scope.tracks = Track.queryAll();
//        console.info($scope.tracks);
        $scope.trackToAdd = {};
        
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

        $scope['delete'] = function (id) {
            Project['delete']({id: id},
                function () {
                    $scope.projects = Project.query();
                });
        };

        $scope.clear = function () {
            $scope.project = {id: null, title: "Enter name", tracks: []};
        };
        
        $scope.removeTrackFromProject = function(track, project) {
        	var newTracks = [];
        	for (var idx in project.tracks) {
        		var tr = project.tracks[idx];
        		if (tr.id != track.id) newTracks.push(tr);
        	}
        	project.tracks = newTracks;
        }
        
        $scope.addTrackToProject = function(trackId,project) {
        	var track = null;
        	for (var idx in $scope.tracks) {
        		var tmp = $scope.tracks[idx];
        		if (trackId == tmp.id) track = tmp;
        	}
        	if (track == null) return;
        	for (var idx in project.tracks) {
        		var tmp = project.tracks[idx];
        		if (tmp.id == trackId) {
                	console.info('already in');
                	console.info(tmp.id);
        			return; 
        		}
        	}
        	console.info('adding');
        	console.info(track);
        	project.tracks.push(track); //this is a string ???
        	console.info('all');
        	console.info(project);
        }
        
        
    }]);
