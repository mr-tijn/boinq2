'use strict';

angular.module('boinqApp').controller("QueryRendererController",["$scope", "dragging","QueryDefinitionObjects","mouseCapture","QueryValidator",function($scope,dragging,QueryDefinitionObjects,mouseCapture,QueryValidator) {
	console.log("Registering QueryRendererController");
	
	var globalIdx = 0;
	var graphIdx = 0;
	var drag = null;

	$scope.toolTip = {
			show: true,
			text: "",
			class: "",
			x: 100,
			y: 30
	}
	
	$scope.bridgeTooltip = function(evt, bridge) {
		var bridgeValidation = QueryValidator($scope.queryDefinition).validateBridge(bridge);
		var offset = 10;
		if (bridgeValidation.validity) {
			// describe node
		} else {
			$scope.toolTip.x = evt.offsetX + offset;
			$scope.toolTip.y = evt.offsetY + offset;
			$scope.toolTip.text = bridgeValidation.comment;
			$scope.toolTip.show = true;
			$scope.toolTip.class = "error";
		}
	}
	
	$scope.hideTooltip = function() {
		$scope.toolTip.show = false;
		$scope.toolTip.text = "";
	}
	
	$scope.getfromgraph = function(bridge) {
		return $scope.queryDefinition.queryGraphs.filter(function(graph) {return bridge.fromGraphIdx == graph.idx;})[0];
	};
	$scope.gettograph = function(bridge) {
		return $scope.queryDefinition.queryGraphs.filter(function(graph) {return bridge.toGraphIdx == graph.idx;})[0];
	};
	$scope.getfromnode = function(bridge) {
		return $scope.getfromgraph(bridge).queryNodes.filter(function(node) {return node.idx == bridge.fromNodeIdx;})[0];
	};
	$scope.gettonode = function(bridge) {
		return $scope.gettograph(bridge).queryNodes.filter(function(node) {return node.idx == bridge.toNodeIdx;})[0];
	};

	
	
	var findTrack = function(trackId) {
		var tracks = [];
		for (var i = 0; i < $scope.datasources.length; i++) {
			var datasource = $scope.datasources[i];
			for (var j = 0; j < datasource.tracks.length; j++) {
				var track = datasource.tracks[j];
				if (trackId == track.id) return track;
			}
		}
	}
	
	// what to do when a graphtemplate is dropped on the canvas
	$scope.dropped = function(event,element,trackId) {
		var track = findTrack(trackId);
		var graph = new QueryDefinitionObjects.QueryGraph(track.graphTemplateId, event.offsetX, event.offsetY, track.name, graphIdx++);
		$scope.queryDefinition.queryGraphs.push(graph);
		$scope.selection.mode = 'graph';
		$scope.selection.graph = graph;
		$scope.selection.bridge = null;
	}
	
	var removeGraph = function(graph) {
		$scope.queryDefinition.queryGraphs = $scope.queryDefinition.queryGraphs.filter(function(test) {return test != graph;});
		$scope.queryDefinition.queryBridges = $scope.queryDefinition.queryBridges.filter(function(bridge) {return bridge.fromGraphIdx != graph.idx && bridge.toGraphIdx != graph.idx;});
	}
	
	// functions to check what element the mouse is over
	var hitTest = function(clientX, clientY) {
		return window.document.elementFromPoint(clientX, clientY);
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
	
	var overId = function(x,y,id) {
		var elements = window.document.elementsFromPoint(x, y);
		if (elements == null || elements == undefined || elements.length == 0) {
			return false;
		} else {
			return elements.filter(function(element) {return element.id == id;}).length > 0;
		}
	};

	$scope.mouseMove = function (evt) {
		var element = hitTest(evt.clientX, evt.clientY);
		$scope.overGraph = findModel(element,'graph');
		$scope.overBridge = findModel(element,'bridge');
		$scope.overTrash = overId(evt.clientX,evt.clientY,'trash');
	};
	
	$scope.valid = function(bridge) {
		var bridgeValidation = QueryValidator($scope.queryDefinition).validateBridge(bridge);
		if (bridgeValidation.validity) {
			return 'valid';
		} else {
			return 'invalid';
		}
	}
	
	$scope.selectBridge = function(bridge) {
		$scope.selection.mode = 'bridge';
		$scope.selection.bridge = bridge;
		$scope.selection.graph = null;
	};
	
	// line clicked
	$scope.lineMouseDown = function(event,bridge) {
		dragging.startDrag(event, {
			clicked: function () {
				$scope.selectBridge(bridge);
			}
		});
	};
	
	$scope.selectGraph = function(graph) {
    	$scope.selection.mode = 'graph';
    	$scope.selection.graph = graph;
    	$scope.selection.bridge = null;
	};
	
	
	// node clicked, dragged
	$scope.nodeMouseDown = function(event, graph) {
		 var lastMouseCoords;
		 dragging.startDrag(event, {
		        dragStarted: function (x, y) {
		        	lastMouseCoords = {x:x, y:y};
		        	drag = graph;
		        },
		        dragging: function (x, y) {
		            var curCoords = {x:x, y:y};
		            
		            var deltaX = curCoords.x - lastMouseCoords.x;
		            var deltaY = curCoords.y - lastMouseCoords.y;

		            graph.x = graph.x + deltaX;
		            graph.y = graph.y + deltaY;

		            lastMouseCoords = curCoords;
		        },
		        clicked: function () {
		        	$scope.selectGraph(graph);
		        },
		        dragEnded: function() {
		        	drag = null;
		        	if ($scope.overTrash) {
		        		removeGraph(graph);		        	}
		        }
		    });
	};
	
	// ring dragged
	$scope.ringMouseDown = 	function(event, graph) {
		var lastMouseCoords;
		 dragging.startDrag(event, {
		        dragStarted: function (x, y) {
		        	lastMouseCoords = {x:x, y:y};
		        	$scope.selectLine = {x1:graph.x, y1:graph.y, x2:graph.x, y2:graph.y};
		        },
		        dragging: function (x, y) {
		            var curCoords = {x:x, y:y};
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
		        	if ($scope.overGraph != null && $scope.overGraph != graph) {
		        		var bridge = new QueryDefinitionObjects.QueryBridge(graph, $scope.overGraph);
		        		$scope.queryDefinition.queryBridges.push(bridge);
		        		
		        		$scope.selection.mode = 'bridge';
		        		$scope.selection.bridge = bridge;
		        		$scope.selection.graph = undefined;
		        	}
		        }
		    });
		
		
	};
	
	$scope.getClass = function(graph) {
		if (drag == graph && $scope.overTrash) {
			return 'bnq_ds_delete';
		} else {
			return $scope.selection.graph == graph ? 'bnq_ds_selected' : 'bnq_ds_circle'
		}
	}
	
	$scope.mouseEnter = function (evt) {
		var element = angular.element(evt.currentTarget);
		mouseCapture.registerElement(element);
	}
	
}]);