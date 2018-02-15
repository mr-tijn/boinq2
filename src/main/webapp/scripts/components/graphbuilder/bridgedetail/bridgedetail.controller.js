angular.module('boinqApp').controller('BridgeDetailController',['$scope','NodeConstants','BridgeConstants','GraphTemplate',function($scope,NodeConstants,BridgeConstants,GraphTemplate) {
	console.log('Registering controller BridgeDetailController');
	
	$scope.graphTemplates={};
	$scope.matchTypes = BridgeConstants.MATCH_ITEMS;
	
//	getTemplate = function(templateId) {
//		if ($scope.graphTemplates[templateId] == null) {
//			$scope.graphTemplates[templateId] = GraphTemplate.get({id:templateId});
//		}
//		return $scope.graphTemplates[templateId];
//	}
	
	
	$scope.$watch('selection.bridge',function() {		
		$scope.fromTemplate = GraphTemplate.get({id: getFromGraph().template});
		$scope.toTemplate = GraphTemplate.get({id: getToGraph().template});
	});
	
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
		if (unresolved()) return null;
		if ($scope.selection.bridge.fromNodeIdx != null && $scope.selection.bridge.fromGraphIdx != null) {
			return getFromGraph().queryNodes.filter(function(node) {return node.idx == $scope.selection.bridge.fromNodeIdx;})[0];
		}
	};
	
	getToNode = function() {
		if (unresolved()) return null;
		if ($scope.selection.bridge.fromNodeIdx != null && $scope.selection.bridge.fromGraphIdx != null) {
			return getToGraph().queryNodes.filter(function(node) {return node.idx == $scope.selection.bridge.toNodeIdx;})[0];
		}
	};

	getFromTemplate = function() {
		if (unresolved()) return null;
		var fromNode = getFromNode();
		if (fromNode) {
			return $scope.fromTemplate.nodeTemplates.filter(function(tmpl) {return tmpl.id == fromNode.template;})[0]; 
		}
	}
	
	getToTemplate = function() {
		if (unresolved()) return null;
		var toNode = getToNode();
		if (toNode) {
			return $scope.toTemplate.nodeTemplates.filter(function(tmpl) {return tmpl.id == toNode.template;})[0];;
		}
	}
	
	var unresolved = function() {
		return !$scope.fromTemplate.$resolved || !$scope.toTemplate.$resolved;
	}
	
	$scope.stringToEntity = function() {
		if (unresolved()) return false;
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && from && to) {
			return (from.nodeType == NodeConstants.TYPE_LITERAL && to.nodeType == NodeConstants.TYPE_ENTITY) || (to.nodeType == NodeConstants.TYPE_LITERAL && from.nodeType == NodeConstants.TYPE_ENTITY)
			|| (from.nodeType == NodeConstants.TYPE_LITERAL && to.nodeType == NodeConstants.TYPE_TYPEDENTITY) || (to.nodeType == NodeConstants.TYPE_LITERAL && from.nodeType == NodeConstants.TYPE_TYPEDENTITY);
		} else return false;
	};
	
	$scope.literalToLiteral = function() {
		if (unresolved()) return false;
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && from && to) {
			return (from.nodeType == NodeConstants.TYPE_LITERAL) && (to.nodeType == from.nodeType);
		} else return false;
	};
	
	$scope.location = function() {
		if (unresolved()) return false;
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && from && to) {
			return (fromTmp.nodeType == NodeConstants.TYPE_FALDOENTITY) && (toTmp.nodeType == from.nodeType); 
		} else return false;
	};
	

}]);

