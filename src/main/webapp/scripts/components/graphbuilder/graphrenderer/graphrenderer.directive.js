angular.module('boinqApp').directive('graphrenderer', function() {
	console.log("Registering Directive graphrenderer");
	return {
		restrict: 'E',
		scope: {
			queryGraph: '=',
			queryDefinition: '=',
			selection: '=',
			incomingBridges: '=',
			outgoingBridges: '='
		},
		link: function(scope, element, attrs) {
			
		},
		templateUrl: 'scripts/components/graphbuilder/graphrenderer/graphrenderer.html'
	};
});