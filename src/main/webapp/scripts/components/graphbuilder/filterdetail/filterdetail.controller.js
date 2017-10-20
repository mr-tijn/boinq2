angular.module('boinqApp').controller("NodeFilterController",['$scope','TermTools','boinqOneTimeFetch',"NodeConstants",'FilterConstants',function($scope,TT,oneTime,NodeConstants,Filterconstants) {
	console.log("Registering NodeFilterController");
	
	$scope.globals = oneTime;
	
	$scope.labels = function(terms) {
		if (!terms) return;
		return terms.map(function(term) {return term.label;}).join();
	}
	
	$scope.termPicked = function(terms) {
		
	};

	$scope.isText = function(template) {
		return (template.nodeType == NodeConstants.TYPE_LITERAL && TT.isText(template.literalXsdType));
	};
	
	$scope.isTerm = function(template) {
		return (template.nodeType == NodeConstants.TYPE_ENTITY);
	};
	
	$scope.isNumeric = function(template) {
		return (template.nodeType == NodeConstants.TYPE_LITERAL && TT.isNumeric(template.literalXsdType));
	};
	
	$scope.isFloat = function(template) {
		return (template.nodeType == NodeConstants.TYPE_LITERAL && TT.isFloat(template.literalXsdType));
	};

	$scope.isInteger = function(template) {
		return (template.nodeType == NodeConstants.TYPE_LITERAL && TT.isInteger(template.literalXsdType));
	};
	
	$scope.isLocation = function(template) {
		return (template.nodeType == NodeConstants.TYPE_FALDOENTITY);
	};

	
}]);