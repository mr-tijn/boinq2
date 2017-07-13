angular.module('boinqApp').directive('queryrenderer', function() {
	console.log("Registering Directive queryrenderer");
	return {
		restrict: 'E',
		scope: {
			queryDefinition: '=',
			selection: '=',
			datasources: '='
		},
		link: function(scope, element, attrs) {
			
		},
		templateUrl: 'scripts/components/graphbuilder/queryrenderer/queryrenderer.html'
	};
});