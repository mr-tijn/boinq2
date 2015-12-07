angular.module('boinqApp').controller("QueryBuilderController",['$scope','dragging','FeatureQueryService','resolvedDatasource',function($scope,dragging,FeatureQueryService,resolvedDatasource) {

	console.log("Registering controller QueryBuilderController");
	
	$scope.datasources = resolvedDatasource;
	$scope.tracks = {};
	
	for (var i = 0; i < resolvedDatasource.length; i++) {
		var tracks = resolvedDatasource[i].tracks;
		for (var i = 0; i < tracks.length; i++) {
			var track = tracks[i];
			$scope.tracks[track.id] = track;
			console.log("trackid "+track.id);
		}
	}
	
	$scope.activeFS = [];
	$scope.selectedFS = undefined;
	$scope.overFS = null;

	$scope.activeFJ = [];
	$scope.selectedFJ = undefined;
	$scope.overFJ = null;
	
	link = function(srcFS,dstFS) {
		return {
			sourceSelect: srcFS,
			targetSelect: dstFS,
			type: 'LocationOverlap'
		} 
	}
	
	$scope.dropped = function(event,element,trackId) {
		console.log("looking up track "+trackId);
		var track = $scope.tracks[trackId];
		console.log("trackid "+track.id);

		console.log(track);
		activeFS(event.offsetX, event.offsetY, track.id, track.name);
	}
	
	var hitTest = function(clientX, clientY) {
		return this.document.elementFromPoint(clientX, clientY);
	};

	var findModel = function(element,property) {
		if (element == undefined || element == null || element.length == 0) {
			return null;
		} else {
			var elementScope = angular.element(element).data(undefined).$scope;
			if (elementScope != null && elementScope.hasOwnProperty(property)) {
				return elementScope[property];
			} else {
				return findModel(element.parentNode,property);
			}
		}
	};

	$scope.mouseMove = function (evt) {
		var element = hitTest(evt.clientX, evt.clientY);
		$scope.overFS = findModel(element,'fs');
		$scope.overFJ = findModel(element,'fj');
	};
	
	$scope.overLink = function(ln) {
		if ($scope.overFJ == ln) {
			return "over";
		} else {
			return "";
		}
	}
	
	$scope.selectedFJ = function(ln) {
		if ($scope.selectedFJ == ln) {
			return "selected";
		} else {
			return "";
		}
	}
	
	$scope.lineMouseDown = function(event,ln) {
		dragging.startDrag(event, {
			clicked: function () {
				$scope.selectedFJ = ln;
				$scope.selectedFS = null;
			}
		});
	}
	
	$scope.nodeMouseDown = function(event, fs) {
		 dragging.startDrag(event, {
		        dragStarted: function (x, y) {
		        	lastMouseCoords = {x:x, y:y};
		        },
		        dragging: function (x, y) {
		            curCoords = {x:x, y:y};
		            
		            var deltaX = curCoords.x - lastMouseCoords.x;
		            var deltaY = curCoords.y - lastMouseCoords.y;

		            fs.xc = fs.xc + deltaX;
		            fs.yc = fs.yc + deltaY;
		            fs.viewX = fs.xc + "px";
		            fs.viewY = fs.yc + "px";

		            lastMouseCoords = curCoords;
		        },
		        clicked: function () {
		        	$scope.selectedFS = fs;
		        	$scope.selectedFJ = null;
		        },
		        dragEnded: function() {
		        	
		        }
		    });
	}
	
	$scope.ringMouseDown = 	function(event, fs) {
		 dragging.startDrag(event, {
		        dragStarted: function (x, y) {
		        	lastMouseCoords = {x:x, y:y};
		        	$scope.selectLine = {x1:fs.xc, y1:fs.yc, x2:fs.xc, y2:fs.yc};
		        },
		        dragging: function (x, y) {
		            curCoords = {x:x, y:y};
		            var deltaX = curCoords.x - lastMouseCoords.x;
		            var deltaY = curCoords.y - lastMouseCoords.y;
		            $scope.selectLine.x2 += deltaX;
		            $scope.selectLine.y2 += deltaY;
		            lastMouseCoords = curCoords;
		        },
		        clicked: function () {
		        	
		        },
		        dragEnded: function() {
		        	$scope.selectLine = undefined;
		        	if ($scope.overFS != null && $scope.overFS != fs) {
		        		var fj = link(fs,$scope.overFS);
		        		$scope.activeFJ.push(fj);
		        		$scope.selectedFJ = fj;
		        		$scope.selectedFS = undefined;
		        		console.log("linking " + fs + " to " + $scope.overFS);
		        	}
		        }
		    });
		
		
	}
	
	activeFS = function(x, y, trackId, trackName) {
	    var xpos = x + "px";
	    var ypos = y + "px";
	    
	    var FS = {viewX:xpos, viewY:ypos, xc:x, yc:y, criteria: [], trackId: trackId, trackName : trackName, type: "undefined"};
	    
		$scope.activeFS.push(FS);
	}
	
	$scope.save = function() {
		
		var featureQuery = {
				joins : $scope.featureJoins,
				selects : $scope.featureSelects,
				ownerId : $user
		};
		
		FeatureQueryService.post(featureQuery)
		
	}
	
}]);

	