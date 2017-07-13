angular.module('boinqApp').directive('nodedetail', function() {
	return {
		restrict: 'E',
		scope: {
			node: '=',
			selection: '=',
			graphTemplate : '=' 
		},
		templateUrl: 'scripts/components/graphbuilder/nodedetail/nodedetail.html'
	}
});