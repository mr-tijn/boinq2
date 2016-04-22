angular.module('boinqApp').controller("QueryBuilderController",['$scope','dragging','FeatureQuery','resolvedDatasource','resolvedFeatureQuery','resolvedAccount','QueryBuilderService','callEndpoint','$state',function($scope,dragging,FeatureQuery,resolvedDatasource,resolvedFeatureQuery,resolvedAccount,QueryBuilderService,callEndpoint,$state) {

	console.log("Registering controller QueryBuilderController");
	
	$scope.datasources = resolvedDatasource;
	$scope.tracks = {};
	$scope.datasourceref = {};
	$scope.featureQuery = {selects: [], joins: []};
	$scope.cansave = true;
	
	console.log(resolvedDatasource);
	
	var processResolvedFeatureQuery = function(resolvedFeatureQuery) {
		if (resolvedFeatureQuery != null) {
			var selectIdx = {};
			$scope.cansave = false;
			for (var i=0; i< resolvedFeatureQuery.selects.length; i++) {
				var select =  resolvedFeatureQuery.selects[i];
				for (var j=0; j<select.criteria; j++) {
					var crit = select.criteria[j];
					if (crit.featureTypeUri) {
						crit.featureType = {uri:crit.featureTypeUri, label:crit.featureTypeLabel};
					}
				}
				select.xpos = select.viewX+"px";
				select.ypos = select.viewY+"px";
				selectIdx[select.idx] = select;
			}
			var joins = [];
			var indexedJoins = resolvedFeatureQuery.joins;
			for (var i=0; i<indexedJoins.length; i++) {
				var indexedJoin = indexedJoins[i];
				joins.push({
					sourceSelect:selectIdx[indexedJoin.sourceSelectIdx],
					targetSelect:selectIdx[indexedJoin.targetSelectIdx]});
			}
			$scope.featureQuery = {
			 	selects: resolvedFeatureQuery.selects,
				joins: joins,
				name: resolvedFeatureQuery.name,
				ownerId: resolvedFeatureQuery.ownerId
			};
		}
	};
	
	processResolvedFeatureQuery(resolvedFeatureQuery);
	
	for (var i = 0; i < resolvedDatasource.length; i++) {
		var tracks = resolvedDatasource[i].tracks;
		for (var j = 0; j < tracks.length; j++) {
			var track = tracks[j];
			$scope.tracks[track.id] = track;
			$scope.datasourceref[track.id] = resolvedDatasource[i];
		}
	}
	
	$scope.selectedFS = undefined;
	$scope.overFS = null;

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
		        		$scope.featureQuery.joins.push(fj);
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
				
				// now: assume all tracks support type filter and location filter
				
				// fetch feature types for type filter
				var queryTypes = [];

//				QueryBuilderService.featureTypeQuery(trackId).then(function(queryString) {
//					callEndpoint(datasource.metaEndpointUrl,datasource.metaGraphName,queryString).then(function(successResponse){
//						var records = successResponse.data.results.bindings;
//						for (var i=0; i<records.length; i++) {
//							var record = records[i];
//							queryTypes.push({uri: record.featureType.value, label: record.label.value});
//						}
//					}, function(error) {
//						console.error(error);
//					});
//				});

				typeNames = Object.keys(track.supportedFeatureTypes);
				for (var i=0; i<typeNames.length; i++) {
					typeName = typeNames[i];
					queryTypes.push({uri:track.supportedFeatureTypes[typeName], label:typeName});
				}
				
				var operators = track.supportedOperators;
				operators.forEach(function (current, index, array) {
					if (!current.operatorName) {
						current.operatorName = current.operatorTypeName;
					}
				});
				
				var FS = {idx:idx, xpos:xpos, ypos:ypos, viewX:x, viewY:y, criteria: [], trackId: trackId, trackName : trackName, type: "undefined", queryTypes : queryTypes, operators : operators, retrieve : false};
				idx++;
				$scope.featureQuery.selects.push(FS);
				$scope.selectedFS = FS;
				$scope.selectedFJ = undefined;
			}
	}();
	
	$scope.save = function() {
		
		console.log($scope.featureQuery);
		// rebuild the object to get rid of unnecessary stuff in the JSON
		var featureQuery = {
				joins : [],
				selects : [],
				name: $scope.featureQuery.name,
				ownerId : resolvedAccount.login
		};
		for (var i=0; i<$scope.featureQuery.joins.length; i++) {
			var join = $scope.featureQuery.joins[i];
			featureQuery.joins.push({
				type:join.type,
				sameStrand:join.sameStrand,
				sourceSelectIdx:join.sourceSelect.idx, 
				targetSelectIdx:join.targetSelect.idx});
		}
		for (var i=0; i<$scope.featureQuery.selects.length; i++) {
			var select = $scope.featureQuery.selects[i];
			var criteria = [];
			for (var j=0; j< select.criteria.length; j++) {
				var criterion = select.criteria[j];
				switch (criterion.operator.operatorName) {
				case "FeatureType" :
					criteria.push({
						type: criterion.operator.operatorName,
						featureTypeUri: (criterion.featureType?criterion.featureType.uri:null),
						featureTypeLabel: (criterion.featureType?criterion.featureType.label:null)
					});
					break;
				case "Location" :
					criteria.push({
						type: criterion.operator.operatorName,
						contig: criterion.contig,
						start: criterion.start,
						end: criterion.end,
						strand: criterion.strand,
					});
					break;
				// TODO: other types
				default:
					criteria.push({
						type: "undefined"
					});
				}
			}
			featureQuery.selects.push ({
				idx: select.idx,
				viewX: select.viewX,
				viewY: select.viewY,
				criteria: criteria,
				trackId: select.trackId,
				retrieve: select.retrieve
			});
		}
		
		console.log("Saving featurequery: ");
		console.log(featureQuery);
		
	
		FeatureQuery.create(featureQuery).$promise.then(function() {
			$state.go("featurequery");
		});
		
	}
	
}]);

	