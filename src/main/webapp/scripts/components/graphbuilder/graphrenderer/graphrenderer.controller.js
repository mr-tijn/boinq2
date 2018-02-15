'use strict';

angular.module('boinqApp').controller("GraphRendererController",["$scope", "$document", "dragging","QueryDefinitionObjects","GraphTemplate","mouseCapture","NodeConstants",function($scope,$document,dragging,QueryDefinitionObjects,GraphTemplate,mouseCapture,NodeConstants) {
	var nodeIdx = 0;

	var maxIdx = function(array) {
		return Math.max(0,Math.max.apply(Math,array.map(function(o){return o.idx;})));
	}
	
	$scope.toolTip = {
			show: true,
			text: "",
			x: 100,
			y: 30
	}
	
	$scope.showTooltip = function(evt, object) {
		var offset = 10;
		$scope.toolTip.show = true;
		if(object.toolTipText) {
			$scope.toolTip.text = object.toolTipText();
		}
		$scope.toolTip.x = evt.clientX + offset;
		$scope.toolTip.y = evt.clientY + offset;
	}
	
	$scope.refreshGraph = function(queryGraph) {
		if (queryGraph && queryGraph.template) {
			GraphTemplate.get({id:queryGraph.template}).$promise.then(function(graphTemplate) {
				$scope.graphTemplate = graphTemplate; 
				var graphCanvas = $document[0].getElementById('graphCanvas');
				var width = $scope.horizontalSize(graphTemplate.nodeTemplates)+"px";
				var height = $scope.verticalSize(graphTemplate.nodeTemplates) + "px";
				graphCanvas.style.width =  width;
				graphCanvas.style.height = height;
			});
		}
		for (var i = 0; i < queryGraph.queryNodes.length; i++) {
			queryGraph.queryNodes[i].selected = true;
		}
		for (var i = 0; i < queryGraph.queryEdges.length; i++) {
			queryGraph.queryEdges[i].selected = true;
		}
		nodeIdx = maxIdx(queryGraph.queryNodes) + 1;
	};
	$scope.$watch('queryGraph', $scope.refreshGraph);

	$scope.findByIdx = function(id, array) {
		if (array && array.length) {
			var found = array.filter(function(element) {return id == element.idx})
			if (found && found.length) return found[0];
		}
	}
	
	$scope.findById = function(id, array) {
		if (array && array.length) {
			var found = array.filter(function(element) {return id == element.id})
			if (found && found.length) return found[0];
		}
	}
	
	$scope.findX = function(node) {
		var nodeTemplate = $scope.findById(node.template, $scope.graphTemplate.nodeTemplates);
		if (nodeTemplate) return nodeTemplate.x;
	}
	
	$scope.findY = function(node) {
		var nodeTemplate = $scope.findById(node.template, $scope.graphTemplate.nodeTemplates);
		if (nodeTemplate) return nodeTemplate.y;
	}

	$scope.isFilterable = function(node) {
		if (!node) return false;
		var nodeTemplate = $scope.findById(node.template, $scope.graphTemplate.nodeTemplates);
		if (nodeTemplate) return nodeTemplate.filterable;
	}
	$scope.targetNodeTemplate = function(bridge) {
		var queryNode = $scope.findByIdx(bridge.toNodeIdx, $scope.queryGraph.queryNodes);
		if (queryNode) {
			return $scope.findById(queryNode.template, $scope.graphTemplate.nodeTemplates);
		}
	}

	$scope.sourceNodeTemplate = function(bridge) {
		var queryNode = $scope.findByIdx(bridge.fromNodeIdx, $scope.queryGraph.queryNodes);
		if (queryNode) {
			return $scope.findById(queryNode.template, $scope.graphTemplate.nodeTemplates);
		}
	}
	
	var nodeForTemplate = function(nodeTemplateId) {
		var nodes = $scope.queryGraph.queryNodes.filter(function(node) {return node.template == nodeTemplateId});
		if (nodes && nodes.length)	{
			return nodes[0];
		}
	}
	
	var edgeForTemplate = function(edgeTemplateId) {
		var edges = $scope.queryGraph.queryEdges.filter(function(edge) {return edge.template == edgeTemplateId});
		if (edges && edges.length) {
			return edges[0];
		}
	}
	
	var fromneighbours = function(edgeTemplate) {
		return $scope.queryGraph.queryEdges.filter(function(edge) {
			if (edge.template == edgeTemplate.id || !edge.selected) {
				return false;
			} else {
				var tmpl = $scope.findById(edge.template, $scope.graphTemplate.edgeTemplates);
				return (tmpl.from == edgeTemplate.from || 
						 tmpl.to == edgeTemplate.from)
			}
		});
	}
	
	var toneighbours = function(edgeTemplate) {
		return $scope.queryGraph.queryEdges.filter(function(edge) {
			if (edge.template == edgeTemplate.id || !edge.selected) {
				return false;
			} else {
				var tmpl = $scope.findById(edge.template, $scope.graphTemplate.edgeTemplates);
				return (tmpl.from == edgeTemplate.to || 
						 tmpl.to == edgeTemplate.to)
			}
		});
	}
	
	var neighbours = function(edgeTemplate) {
		//find queryEdges neighbouring this edgeTemplate
		return $scope.queryGraph.queryEdges.filter(function(edge) {
			if (edge.template == edgeTemplate.id || !edge.selected) {
				return false;
			} else {
				var tmpl = $scope.findById(edge.template, $scope.graphTemplate.edgeTemplates);
				return (tmpl.from == edgeTemplate.from || 
						 tmpl.to == edgeTemplate.from || 
						 tmpl.from == edgeTemplate.to || 
						 tmpl.to == edgeTemplate.to)
			}
		});
	}	
	var adjoins = function(edgeTemplate) {
		return $scope.queryGraph.queryEdges.filter(function(edge) {return edge.selected;}).length == 0 || neighbours(edgeTemplate).length > 0; 
	}
	
	var addNodes = function(queryEdge) {
		var edgeTemplate = $scope.findById(queryEdge.template, $scope.graphTemplate.edgeTemplates);

		var fromTemplate = $scope.findByIdx(edgeTemplate.from, $scope.graphTemplate.nodeTemplates);
		var fromNode = nodeForTemplate(fromTemplate.id);
		if (!fromNode) {
			fromNode = new QueryDefinitionObjects.QueryNode(fromTemplate.id, nodeIdx++);
			$scope.queryGraph.queryNodes.push(fromNode);
		}
		fromNode.selected = true;
		queryEdge.from = fromNode.idx;
		
		var toTemplate = $scope.findByIdx(edgeTemplate.to, $scope.graphTemplate.nodeTemplates);
		var toNode = nodeForTemplate(toTemplate.id);
		if (!toNode) {
			toNode = new QueryDefinitionObjects.QueryNode(toTemplate.id, nodeIdx++);
			$scope.queryGraph.queryNodes.push(toNode);
		}
		toNode.selected = true;
		queryEdge.to = toNode.idx;
	}
	
	$scope.fromNode = function(edgeTemplate) {
		if (!$scope.graphTemplate || !edgeTemplate) return;
		var nodes = $scope.graphTemplate.nodeTemplates.filter(function(nodeTemplate) {return edgeTemplate.from == nodeTemplate.idx});
		if (nodes && nodes.length) {return nodes[0];}
	}
	
	$scope.toNode = function(edgeTemplate) {
		if (!$scope.graphTemplate || !edgeTemplate) return;
		var nodes = $scope.graphTemplate.nodeTemplates.filter(function(nodeTemplate) {return edgeTemplate.to == nodeTemplate.idx});
		if (nodes && nodes.length) {return nodes[0];}
	}

	var isLiteral = function(node) {
		if (node.hasOwnProperty('template')) {
			if ($scope.graphTemplate && $scope.graphTemplate.$resolved) {
				var template = $scope.findById(node.template, $scope.graphTemplate.nodeTemplates);
				return (template && template.nodeType == NodeConstants.TYPE_LITERAL);
			} else {
				return false;
			}
		} else {
			return node.nodeType == NodeConstants.TYPE_LITERAL;
		}
	};

	$scope.typeClass = function(node) {
		if (node.hasOwnProperty('template')) {
			return $scope.typeClass($scope.findById(node.template));
		} else {
			return 'bnq_' + NodeConstants.TYPE_ITEMS[node.nodeType];
		}
	};
	
	$scope.literals = function(nodes) {
		if (!nodes) return [];
		return nodes.filter(function(n) {return isLiteral(n);});
	};
	
	$scope.entities = function(nodes) {
		if (!nodes) return [];
		return nodes.filter(function(n) {return !isLiteral(n);});
	};
	
	$scope.select = function(edgeTemplate) {
		var queryEdge = edgeForTemplate(edgeTemplate.id);
		if (queryEdge) {
			queryEdge.selected = true;
			var fromNode = $scope.findByIdx(queryEdge.from,$scope.queryGraph.queryNodes);
			fromNode.selected = true;
			var toNode = $scope.findByIdx(queryEdge.to,$scope.queryGraph.queryNodes);
			toNode.selected = true;
		} else if (adjoins(edgeTemplate)) {
			queryEdge = new QueryDefinitionObjects.QueryEdge();
			queryEdge.template = edgeTemplate.id;
			queryEdge.selected = true;
			addNodes(queryEdge);
			$scope.queryGraph.queryEdges.push(queryEdge);
		} 
	}
	
	$scope.deselect = function(queryEdge) {
		var edgeTemplate = $scope.findById(queryEdge.template, $scope.graphTemplate.edgeTemplates);
		if (fromneighbours(edgeTemplate).length && toneighbours(edgeTemplate).length) {
			// only deselect end branches
			return;
		}
		queryEdge.selected = false;
		var fromNode = $scope.queryGraph.queryNodes.filter(function(node) {return node.idx == queryEdge.from})[0];
		if (fromNode) {
			var adjoining = neighbours(edgeTemplate).filter(function(edge) {return fromNode.idx == edge.from || fromNode.idx == edge.to});
			if (adjoining && adjoining.length) {
				// do nothing, node is connected to other edge
			} else {
				fromNode.selected = false;
			}
		}
		var toNode = $scope.queryGraph.queryNodes.filter(function(node) {return node.idx == queryEdge.to})[0];
		if (toNode) {
			var adjoining = neighbours(edgeTemplate).filter(function(edge) {return toNode.idx == edge.from || toNode.idx == edge.to});
			if (adjoining && adjoining.length) {
				// do nothing, node is connected to other edge
			} else {
				toNode.selected = false;
			}
		}
	}
	
	$scope.fromBridgeMouseDown = function(event, bridge) {
		var lastMouseCoords;
		dragging.startDrag(event, {
			dragStarted: function(x, y) {
				lastMouseCoords = {x:x, y:y};
				$scope.selectLine = {x1: bridge.toX, y1:bridge.toY, x2:bridge.toX, y2:bridge.toY};
			},
			dragging: function(x, y) {
	            var curCoords = {x:x, y:y};
	            var deltaX = curCoords.x - lastMouseCoords.x;
	            var deltaY = curCoords.y - lastMouseCoords.y;
	            $scope.selectLine.x2 += deltaX;
	            $scope.selectLine.y2 += deltaY;
	            lastMouseCoords = curCoords;
			},
			dragEnded : function() {
	        	$scope.selectLine = undefined;
	        	if ($scope.overNode) {
	    			bridge.toNodeIdx = $scope.overNode.idx;
	        	}
			}
		});
	}
	
	
	$scope.dragIncomingBridge = function(event, bridge) {
		var lastMouseCoords;
		dragging.startDrag(event, {
			dragStarted: function(x, y) {
				lastMouseCoords = {x:x, y:y};
			},
			dragging: function(x, y) {
	            var curCoords = {x:x, y:y};
	            var deltaX = curCoords.x - lastMouseCoords.x;
	            var deltaY = curCoords.y - lastMouseCoords.y;
	            bridge.toX += deltaX;
	            bridge.toY += deltaY;
	            lastMouseCoords = curCoords;
			},
			dragEnded : function() {
			}
		});
	};
	

	$scope.dragOutgoingBridge = function(event, bridge) {
		var lastMouseCoords;
		dragging.startDrag(event, {
			dragStarted: function(x, y) {
				lastMouseCoords = {x:x, y:y};
			},
			dragging: function(x, y) {
	            var curCoords = {x:x, y:y};
	            var deltaX = curCoords.x - lastMouseCoords.x;
	            var deltaY = curCoords.y - lastMouseCoords.y;
	            bridge.fromX += deltaX;
	            bridge.fromY += deltaY;
	            lastMouseCoords = curCoords;
			},
			dragEnded : function() {
			}
		});
	};
	
	$scope.fromNodeMouseDown = function(event, node) {
		var lastMouseCoords;
		dragging.startDrag(event, {
			dragStarted: function(x, y) {
				lastMouseCoords = {x:x, y:y};
				var nodeTemplate = $scope.findById(node.template, $scope.graphTemplate.nodeTemplates); 
				$scope.selectLine = {x1:nodeTemplate.x, y1:nodeTemplate.y, x2:nodeTemplate.x, y2:nodeTemplate.y};
			},
			dragging: function(x, y) {
	            var curCoords = {x:x, y:y};
	            var deltaX = curCoords.x - lastMouseCoords.x;
	            var deltaY = curCoords.y - lastMouseCoords.y;
	            $scope.selectLine.x2 += deltaX;
	            $scope.selectLine.y2 += deltaY;
	            lastMouseCoords = curCoords;
			},
			dragEnded : function() {
	        	$scope.selectLine = undefined;
	        	if ($scope.overBridge) {
	    			$scope.overBridge.fromNodeIdx = node.idx;
	        	}
			},
			clicked: function() {
				$scope.selectNode(node);
			}
		});
		
	}
		
	$scope.selectNode = function(node) {
		$scope.selection.node = node;
	}
	
	$scope.nodeSelected = function(node) {
		return $scope.selection.node == node;
	}
	
	$scope.inBridgeName = function(bridge) {
		var name = "UNKNOWN";
		var fromGraph = $scope.queryDefinition.queryGraphs.filter(function(queryGraph) {return bridge.fromGraphIdx == queryGraph.idx})[0];
		if (fromGraph) {
			name = fromGraph.name;
		}
		return name;
	};
	
	$scope.outBridgeName = function(bridge) {
		var name = "UNKNOWN";
		var toGraph = $scope.queryDefinition.queryGraphs.filter(function(queryGraph) {return bridge.toGraphIdx == queryGraph.idx})[0];
		if (toGraph) {
			name = toGraph.name;
		}
		return name;
	};
	
	$scope.horizontalSize = function(templates) {
		return Math.max.apply(Math,templates.map(function(o){return o.x;})) + 100;
	};
	
	$scope.verticalSize = function(templates) {
		return Math.max.apply(Math,templates.map(function(o){return o.y;})) + 100;
	};
	
	// drop line from node to edge
	
	// select node
	
	// select bridge
	
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

	$scope.mouseMove = function (evt) {
		
		var element = hitTest(evt.clientX, evt.clientY);
		$scope.overEdge = findModel(element,'queryEdge') || findModel(element,'edgeTemplate');
		$scope.overNode = findModel(element,'queryNode') || findModel(element,'nodeTemplate');
		$scope.overBridge = findModel(element, 'outgoingBridge');
	};
	
	$scope.mouseEnter = function (evt) {
		
		var element = angular.element(evt.currentTarget);
		mouseCapture.registerElement(element);
	}

	$scope.fromBridges = function(graph) {
		if (graph) {
			return $scope.queryDefinition.queryBridges.filter(function(bridge) {return bridge.fromGraphIdx == graph.idx});
		} else return [];
	}
	
	$scope.toBridges = function(graph) {
		if (graph) {
			return $scope.queryDefinition.queryBridges.filter(function(bridge) {return bridge.toGraphIdx == graph.idx});
		} else return [];
	}
	
}]);

