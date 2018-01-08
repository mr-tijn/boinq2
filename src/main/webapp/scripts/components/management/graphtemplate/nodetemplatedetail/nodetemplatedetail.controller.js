angular.module('boinqApp').controller('NodeTemplateDetailController',['$scope','NodeConstants','TermTools',function($scope,NodeConstants,TermTools) {
	console.log("Registering controller NodeTemplateDetailController");
	$scope.TermTools = TermTools;
	$scope.isattribute = function() {
		if ($scope.nodeTemplate == null) return false;
		return $scope.nodeTemplate.nodeType == NodeConstants.TYPE_ATTRIBUTE;
	};
	$scope.isliteral = function() {
		if ($scope.nodeTemplate == null) return false;
		return $scope.nodeTemplate.nodeType == NodeConstants.TYPE_LITERAL;
	};
	$scope.isentity = function() {
		if ($scope.nodeTemplate == null) return false;
		return $scope.nodeTemplate.nodeType == NodeConstants.TYPE_ENTITY;
	};
	$scope.istypedentity = function() {
		if ($scope.nodeTemplate == null) return false;
		return $scope.nodeTemplate.nodeType == NodeConstants.TYPE_TYPEDENTITY;
	};
	$scope.islistentity = function() {
		if ($scope.nodeTemplate == null) return false;
		return $scope.isentity() && ($scope.nodeTemplate.valueSource == NodeConstants.SOURCE_LIST);
	};
	$scope.isendpointentity = function() {
		if ($scope.nodeTemplate == null) return false;
		return $scope.isentity() && ($scope.nodeTemplate.valueSource == NodeConstants.SOURCE_ENDPOINT);
	};
	$scope.isfixedentity = function() {
		if ($scope.nodeTemplate == null) return false;
		return $scope.isentity() && ($scope.nodeTemplate.valueSource == NodeConstants.SOURCE_FIXED);
	};
	$scope.changeValueSource = function() {
		if ($scope.isfixedentity()) {
			$scope.nodeTemplate.filterable = false;
		}
	};
	$scope.changeFilterable = function() {
		if ($scope.isfixedentity()) {
			$scope.warningText = "You cannot set fixed entities to be filterable";
			$('#warningDialog').modal('show');
			$scope.nodeTemplate.filterable = false;
		}
	};
}]);
