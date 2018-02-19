angular.module('boinqApp').controller('BridgeDetailController',['$scope','$q','NodeConstants','BridgeConstants','GraphTemplate','TermTools',function($scope,$q,NodeConstants,BridgeConstants,GraphTemplate,TermTools) {
	console.log('Registering controller BridgeDetailController');
	
	$scope.graphTemplates={};
	$scope.matchTypes = BridgeConstants.MATCH_ITEMS;
	
	
	$scope.$watch('selection.bridge',function() {	
		$q.all([GraphTemplate.get({id: getFromGraph().template}).$promise,
				GraphTemplate.get({id: getToGraph().template}).$promise]).then(function(data) {
					$scope.fromTemplate = data[0];
					$scope.toTemplate = data[1];
					computeMatchTypes();
				});
	});

	computeMatchTypes = function() {
		from = getFromTemplate();
		to = getToTemplate();
		if (from && to) {
			if (TermTools.isNumeric(from) && TermTools.isNumeric(to)) {
				$scope.matchTypes = BridgeConstants.MATCH_ITEMS_NUMERIC;
			} else {
				$scope.matchTypes = BridgeConstants.MATCH_ITEMS_STRING;
			}
		}
	};

	getFromGraph = function() {
		if ($scope.selection.bridge.fromGraphIdx != null) {
			return $scope.queryDefinition.queryGraphs.filter(function(graph) {return graph.idx == $scope.selection.bridge.fromGraphIdx;})[0];
		}
	};
	
	getToGraph = function() {
		if ($scope.selection.bridge.toGraphIdx != null) {
			return $scope.queryDefinition.queryGraphs.filter(function(graph) {return graph.idx == $scope.selection.bridge.toGraphIdx;})[0];
		}
	};
	
	getFromNode = function() {
		if (!resolved()) return null;
		if ($scope.selection.bridge.fromNodeIdx != null && $scope.selection.bridge.fromGraphIdx != null) {
			return getFromGraph().queryNodes.filter(function(node) {return node.idx == $scope.selection.bridge.fromNodeIdx;})[0];
		}
	};
	
	getToNode = function() {
		if (!resolved()) return null;
		if ($scope.selection.bridge.fromNodeIdx != null && $scope.selection.bridge.fromGraphIdx != null) {
			return getToGraph().queryNodes.filter(function(node) {return node.idx == $scope.selection.bridge.toNodeIdx;})[0];
		}
	};

	getFromTemplate = function() {
		if (!resolved()) return null;
		var fromNode = getFromNode();
		if (fromNode) {
			return $scope.fromTemplate.nodeTemplates.filter(function(tmpl) {return tmpl.id == fromNode.template;})[0]; 
		}
	}
	
	getToTemplate = function() {
		if (!resolved()) return null;
		var toNode = getToNode();
		if (toNode) {
			return $scope.toTemplate.nodeTemplates.filter(function(tmpl) {return tmpl.id == toNode.template;})[0];;
		}
	}
	
	var resolved = function() {
		return $scope.fromTemplate && $scope.fromTemplate.$resolved && $scope.toTemplate && $scope.toTemplate.$resolved;
	}
	
	$scope.stringToEntity = function() {
		if (!resolved()) return false;
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && from && to) {
			return (from.nodeType == NodeConstants.TYPE_LITERAL && to.nodeType == NodeConstants.TYPE_ENTITY) || (to.nodeType == NodeConstants.TYPE_LITERAL && from.nodeType == NodeConstants.TYPE_ENTITY)
			|| (from.nodeType == NodeConstants.TYPE_LITERAL && to.nodeType == NodeConstants.TYPE_TYPEDENTITY) || (to.nodeType == NodeConstants.TYPE_LITERAL && from.nodeType == NodeConstants.TYPE_TYPEDENTITY);
		} else return false;
	};
	
	$scope.literalToLiteral = function() {
		if (!resolved()) return false;
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && from && to) {
			return (from.nodeType == NodeConstants.TYPE_LITERAL) && (to.nodeType == from.nodeType);
		} else return false;
	};
	
	$scope.location = function() {
		if (!resolved()) return false;
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && from && to) {
			return (from.nodeType == NodeConstants.TYPE_FALDOENTITY) && (to.nodeType == from.nodeType); 
		} else return false;
	};
	
}]);

