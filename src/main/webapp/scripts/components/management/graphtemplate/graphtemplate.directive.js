angular.module('boinqApp').directive('graphtemplateview', function() {
	console.log("Registering Directive graphtemplateview");
	return {
		restrict: 'E',
		scope: {
			graphTemplateId: '=',
		},
		link: function(scope, element, attrs) {
			
		},
		templateUrl: 'scripts/components/management/graphtemplate/graphtemplateview.html'
	};
});