angular.module('boinqApp').directive('nodetemplatedetail', ["TermTools",function(TermTools) {
	console.log("Registering Directive nodetemplatedetail");
	return {
		restrict: 'E',
		scope: {
			nodeTemplate: '=',
		},
		link: function(scope, element, attrs) {
			
		},
		templateUrl: 'scripts/components/management/graphtemplate/nodetemplatedetail/nodetemplatedetail.html'
	}
}]);