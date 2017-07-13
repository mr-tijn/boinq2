angular.module('boinqApp').directive('filterdetail', function() {
	return {
		restrict: 'E',
		scope: {
			filter: '=',
			template: '='
		},
		templateUrl: 'scripts/components/graphbuilder/filterdetail/filterdetail.html'
	}
});