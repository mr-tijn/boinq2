angular.module('boinqApp').controller("NodeFilterController",['$scope','TermTools','boinqOneTimeFetch',"NodeConstants",'FilterConstants',function($scope,TT,oneTime,NodeConstants,Filterconstants) {
	console.log("Registering NodeFilterController");
	
	$scope.globals = oneTime;
	
	$scope.labels = function(terms) {
		if (!terms) return;
		return terms.map(function(term) {return term.label;}).join();
	}
	
	$scope.termPicked = function(terms) {
		$scope.filter.termValues = [];
		for (var i=0; i<terms.length; i++) {
			$scope.filter.termValues.push(terms[i].uri.value);
		}
	};

	$scope.isText = function(template) {
		return ((template.nodeType == NodeConstants.TYPE_LITERAL || template.nodeType == NodeConstants.TYPE_ATTRIBUTE) && TT.isText(template.literalXsdType));
	};
	
	$scope.isEndpoint = function(template) {
		return (template.nodeType == NodeConstants.TYPE_ENTITY && template.valueSource == NodeConstants.SOURCE_ENDPOINT);
	};
	
	$scope.isTermList = function(template) {
		return (template.nodeType == NodeConstants.TYPE_ENTITY && template.valueSource == NodeConstants.SOURCE_LIST);
	};
	
	$scope.isNumeric = function(template) {
		return ((template.nodeType == NodeConstants.TYPE_LITERAL || template.nodeType == NodeConstants.TYPE_ATTRIBUTE) && TT.isNumeric(template.literalXsdType));
	};
	
	$scope.isFloat = function(template) {
		return ((template.nodeType == NodeConstants.TYPE_LITERAL || template.nodeType == NodeConstants.TYPE_ATTRIBUTE) && TT.isFloat(template.literalXsdType));
	};

	$scope.isInteger = function(template) {
		return ((template.nodeType == NodeConstants.TYPE_LITERAL || template.nodeType == NodeConstants.TYPE_ATTRIBUTE) && TT.isInteger(template.literalXsdType));
	};
	
	$scope.isLocation = function(template) {
		return (template.nodeType == NodeConstants.TYPE_FALDOENTITY);
	};

	
}]);