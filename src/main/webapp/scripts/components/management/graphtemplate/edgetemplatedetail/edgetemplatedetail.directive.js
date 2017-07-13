angular.module('boinqApp').directive('edgetemplatedetail', function() {
	console.log("Registering Directive edgetemplatedetail");
	return {
		restrict: 'E',
		scope: {
			edgeTemplate: '=',
		},
		templateUrl: 'scripts/components/management/graphtemplate/edgetemplatedetail/edgetemplatedetail.html'
	}
});