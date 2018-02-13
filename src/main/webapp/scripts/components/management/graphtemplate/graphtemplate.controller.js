'use strict';

angular.module('boinqApp').controller("GraphTemplateController",["$scope", "$document", "$stateParams", "dragging","mouseCapture","GraphTemplate","QueryDefinitionObjects","overElement","boinqOneTimeFetch","NodeConstants",function($scope,$document,$stateParams,dragging,mouseCapture,GraphTemplate,QueryDefinitionObjects,overElement,oneTime,NodeConstants) {

	var drag = null;
	$scope.globals = oneTime;
	
	var getGraphTemplate = function(graphTemplateId) {
		GraphTemplate.get({id:graphTemplateId}).$promise.then(function(graphTemplate) {
			$scope.graphTemplate = graphTemplate;
			$scope.resizeCanvas(graphTemplate);
		});
	}
	
	if ($scope.graphTemplateId != null) {
		// coming in from graphtemplateview directive
		getGraphTemplate($scope.graphTemplateId);
	} else {
		// coming in from state
		getGraphTemplate($stateParams.id);
		$scope.graphTemplateId = $stateParams.id;
		$scope.trackId = $stateParams.trackId;
		$scope.datasourceId = $stateParams.datasourceId;
	}
	
	
	$scope.localselection = {
		mode : 'none',
		edgeTemplate : null,
		nodeTemplate : null
	};
	

	$scope.horizontalSize = function(templates) {
		return Math.max.apply(Math,templates.map(function(o){return o.x;})) + 100;
	};
	
	$scope.verticalSize = function(templates) {
		return Math.max.apply(Math,templates.map(function(o){return o.y;})) + 100;
	};

	$scope.resizeCanvas = function(graphTemplate) {
		var graphCanvas = $document[0].getElementById('graphTemplateCanvas');
		var width = $scope.horizontalSize(graphTemplate.nodeTemplates)+"px";
		var height = $scope.verticalSize(graphTemplate.nodeTemplates) + "px";
		graphCanvas.style.width =  width;
		graphCanvas.style.height = height;
	};
	
	$scope.mouseEnter = function (evt) {
		var element = angular.element(evt.currentTarget);
		mouseCapture.registerElement(element);
	}
	
	$scope.selectEdge = function(edgeTemplate) {
		$scope.localselection.nodeTemplate = null;
		$scope.localselection.edgeTemplate = edgeTemplate;
		$scope.localselection.mode = 'edge';
	}
	
	$scope.selectNode = function(nodeTemplate) {
		$scope.localselection.nodeTemplate = nodeTemplate;
		$scope.localselection.edgeTemplate = null;
		$scope.localselection.mode = 'node';
	}
	
	var overId = function(x,y,id) {
		var elements = window.document.elementsFromPoint(x, y);
		if (elements == null || elements == undefined || elements.length == 0) {
			return false;
		} else {
			return elements.filter(function(element) {return element.id == id;}).length > 0;
		}
	};

	var removeNode = function(node) {
		$scope.graphTemplate.nodeTemplates = $scope.graphTemplate.nodeTemplates.filter(function(test) {return test != node;});
		$scope.graphTemplate.edgeTemplates = $scope.graphTemplate.edgeTemplates.filter(function(edge) {return edge.from != node.idx && edge.to != node.idx;});
	};
	
	$scope.nodeMouseDown = function(evt, nodeTemplate) {
		var lastMouseCoords;
		var curCoords;
		dragging.startDrag(evt, {
			dragStarted: function (x, y) {
				drag = nodeTemplate;
				lastMouseCoords = {x:x, y:y};
			},
			dragging: function (x, y) {
				curCoords = {x:x, y:y};

				var deltaX = curCoords.x - lastMouseCoords.x;
				var deltaY = curCoords.y - lastMouseCoords.y;

				nodeTemplate.x += deltaX;
				nodeTemplate.y += deltaY;

				lastMouseCoords = curCoords;
			},
			clicked: function() {
				$scope.selectNode(nodeTemplate);
			},
			dragEnded: function() {
				drag = null;
				if($scope.overTrash) {
					removeNode(nodeTemplate);
				}
			}
		});

	};
	
	$scope.ringMouseDown = 	function(event, nodeTemplate) {
		var lastMouseCoords;
		var curCoords;
		 dragging.startDrag(event, {
		        dragStarted: function (x, y) {
		        	lastMouseCoords = {x:x, y:y};
		        	$scope.selectLine = {x1:nodeTemplate.x, y1:nodeTemplate.y, x2:nodeTemplate.x, y2:nodeTemplate.y};
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
		        	if ($scope.overNode != null && $scope.overNode != nodeTemplate) {
		        		var edge = new QueryDefinitionObjects.EdgeTemplate();
		        		edge.from = nodeTemplate.idx;
		        		edge.to = $scope.overNode.idx;
		        		$scope.graphTemplate.edgeTemplates.push(edge);
		        		
		        		$scope.localselection.mode = 'edge';
		        		$scope.localselection.edgeTemplate = edge;
		        		$scope.localselection.nodeTemplate = undefined;
		        	}
		        }
		    });
		
	};	

	$scope.typeClass = function(nodeTemplate) {
		return 'bnq_' + NodeConstants.TYPE_ITEMS[nodeTemplate.nodeType];
	};
	
	var isLiteral = function(nodeTemplate) {
		return nodeTemplate.nodeType == NodeConstants.TYPE_LITERAL;
	};

	$scope.literals = function(nodeTemplates) {
		return nodeTemplates.filter(function(nt) {return isLiteral(nt);});
	};
	
	$scope.entities = function(nodeTemplates) {
		return nodeTemplates.filter(function(nt) {return !isLiteral(nt);});
	};
	
	$scope.mouseMove = function (evt) {
		$scope.overNode = overElement(evt,'nodeTemplate');
		$scope.overTrash = overId(evt.clientX,evt.clientY,'trash');
	};
	
	$scope.fromNode = function(edgeTemplate) {
		var nodes = $scope.graphTemplate.nodeTemplates.filter(function(nodeTemplate) {return edgeTemplate.from == nodeTemplate.idx});
		if (nodes && nodes.length) {return nodes[0];}
	}
	
	$scope.toNode = function(edgeTemplate) {
		var nodes = $scope.graphTemplate.nodeTemplates.filter(function(nodeTemplate) {return edgeTemplate.to == nodeTemplate.idx});
		if (nodes && nodes.length) {return nodes[0];}
	}
	
	var maxIdx = function(array) {
		if (array && array.length) {
			var max = Math.max.apply(Math,array.map(function(o){return o.idx;}));
			return Math.max(max, array.length);
		} else {
			return 0;
		}
	}
	
	$scope.addNode = function() {
		var idx = maxIdx($scope.graphTemplate.nodeTemplates) + 1;
		var newNode = new QueryDefinitionObjects.NodeTemplate(idx);
		newNode.x = event.offsetX;
		newNode.y = event.offsetY;
		$scope.graphTemplate.nodeTemplates.push(newNode); 
		$scope.selectNode(newNode);
	}
	
	$scope.nodeClass = function(node) {
		if (drag == node && $scope.overTrash) {
			return 'delete';
		} else {
			if ($scope.localselection.nodeTemplate == node) {
				return 'active';
			} else {
				return 'inactive';
			}
		}
	};
	
	$scope.save = function() {
		
		GraphTemplate.save({id:$scope.graphTemplate.id}, $scope.graphTemplate, 
				function(successResponse) {
					getGraphTemplate($scope.graphTemplateId);
				}, function(errorReponse) {
					console.error("Could not save graphTemplate "+$scope.graphTemplate.id);
				});
	}                 		   	

	
	
}]);
