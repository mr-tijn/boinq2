angular.module('boinqApp').controller('BridgeDetailController',['$scope','NodeConstants','GraphTemplate',function($scope,NodeConstants,GraphTemplate) {
	console.log('Registering controller BridgeDetailController');
	
	$scope.graphTemplates={};
	
	getTemplate = function(templateId) {
		if ($scope.graphTemplates[templateId] == null) {
			$scope.graphTemplates[templateId] = GraphTemplate.get({id:templateId});
		}
		return $scope.graphTemplates[templateId];
	}
	
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
		if ($scope.selection.bridge.fromNodeIdx != null && $scope.selection.bridge.fromGraphIdx != null) {
			return getFromGraph().queryNodes.filter(function(node) {return node.idx == $scope.selection.bridge.fromNodeIdx;})[0];
		}
	};
	
	getToNode = function() {
		if ($scope.selection.bridge.fromNodeIdx != null && $scope.selection.bridge.fromGraphIdx != null) {
			return getToGraph().queryNodes.filter(function(node) {return node.idx == $scope.selection.bridge.toNodeIdx;})[0];
		}
	};

	getFromTemplate = function() {
		var fromNode = getFromNode(); 
		var fromTemplate = getTemplate(getFromGraph().template);
		if (fromNode != null) {
			return fromTemplate.nodeTemplates.filter(function(template) {return template.id == fromNode.template;})[0];
		}
	};

	getToTemplate = function() {
		var toNode = getToNode(); 
		var toTemplate = getTemplate(getToGraph().template);
		if (toNode != null) {
			return toTemplate.nodeTemplates.filter(function(template) {return template.id == toNode.template;})[0];
		}
	};
	
	$scope.stringToEntity = function() {
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && getFromNode() != null && getToNode() != null) {
			return (from.nodeType == NodeConstants.TYPE_LITERAL && to.nodeType == NodeConstants.TYPE_ENTITY) || (to.nodeType == NodeConstants.TYPE_LITERAL && from.nodeType == NodeConstants.TYPE_ENTITY)
			|| (from.nodeType == NodeConstants.TYPE_LITERAL && to.nodeType == NodeConstants.TYPE_TYPEDENTITY) || (to.nodeType == NodeConstants.TYPE_LITERAL && from.nodeType == NodeConstants.TYPE_TYPEDENTITY);
		} else return false;
	};
	
	$scope.literalToLiteral = function() {
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && getFromNode() != null && getToNode() != null) {
			return (from.nodeType == NodeConstants.TYPE_LITERAL) && (to.nodeType == from.nodeType);
		} else return false;
	};
	
	$scope.location = function() {
		var from = getFromTemplate();
		var to = getToTemplate();
		if ($scope.selection.bridge && getFromNode() != null && getToNode() != null) {
			return (from.nodeType == NodeConstants.TYPE_FALDOENTITY) && (to.nodeType == from.nodeType); 
		} else return false;
	};
	

}]);

