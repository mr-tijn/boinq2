angular.module('boinqApp').controller("QueryBuilderController",['$scope','dragging','FeatureQueryService','resolvedDatasource','QueryBuilderService','callEndpoint',function($scope,dragging,FeatureQueryService,resolvedDatasource,QueryBuilderService,callEndpoint) {

	console.log("Registering controller QueryBuilderController");
	
	$scope.datasources = resolvedDatasource;
	$scope.tracks = {};
	$scope.datasourceref = {};
	
	for (var i = 0; i < resolvedDatasource.length; i++) {
		var tracks = resolvedDatasource[i].tracks;
		for (var i = 0; i < tracks.length; i++) {
			var track = tracks[i];
			$scope.tracks[track.id] = track;
			$scope.datasourceref[track.id] = resolvedDatasource[i];
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
		var track = $scope.tracks[trackId];

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

		            fs.viewX = fs.viewX + deltaX;
		            fs.viewY = fs.viewY + deltaY;
		            fs.xpos = fs.viewX + "px";
		            fs.ypos = fs.viewY + "px";

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
		        	$scope.selectLine = {x1:fs.viewX, y1:fs.viewY, x2:fs.viewX, y2:fs.viewY};
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
		        	}
		        }
		    });
		
		
	}
	
	activeFS = function() {
			
			var idx = 0;
			
			return function(x, y, trackId, trackName) {

				var xpos = x + "px";
				var ypos = y + "px";
				
				var track = $scope.tracks[trackId];
				var datasource = $scope.datasourceref[trackId];
				
				// TODO: fetch supported filters for track
				// now: assume all tracks support type filter and location filter
				
				// fetch feature types for type filter
				var queryTypes = [];

				QueryBuilderService.featureTypeQuery(trackId).then(function(queryString) {
					callEndpoint(datasource.metaEndpointUrl,datasource.metaGraphName,queryString).then(function(successResponse){
						var records = successResponse.data.results.bindings;
						for (var i=0; i<records.length; i++) {
							var record = records[i];
							queryTypes.push({uri: record.featureType.value, label: record.label.value});
						}
					}, function(error) {
						console.error(error);
					});
				});
				
				var FS = {idx:idx++, xpos:xpos, ypos:ypos, viewX:x, viewY:y, criteria: [], trackId: trackId, trackName : trackName, type: "undefined", queryTypes : queryTypes};
				$scope.activeFS.push(FS);
			}
	}();
	
	$scope.save = function() {
		
		var featureQuery = {
				joins : $scope.activeFJ,
				selects : $scope.activeFS,
				ownerId : $scope.$user
		};
		
		console.log("Saving featurequery: ");
		console.log(featureQuery);
		
		FeatureQueryService.create(featureQuery)
		
	}
	
}]);

	