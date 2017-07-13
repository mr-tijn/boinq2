angular.module('boinqApp').directive('bridgedetail', function() {
	console.log("Registering Directive bridgedetail");
	return {
		restrict: 'E',
		scope: {			
			queryDefinition: '=',
			selection: '='
		},
		templateUrl: 'scripts/components/graphbuilder/bridgedetail/bridgedetail.html'
	}
});