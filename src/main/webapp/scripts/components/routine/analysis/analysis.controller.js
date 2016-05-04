angular.module('boinqApp').controller("AnalysisController",['resolvedGlobals','$scope','Analysis','resolvedAnalysis','JobConstants',
	function(resolvedGlobals,$scope,Analysis,resolvedAnalysis,JobConstants) {
		console.log("Registering AnalysisController");
	
		$scope.globals = resolvedGlobals;
		$scope.referencenames = [];
		for (assembly in resolvedGlobals.references) {
			for (ref in resolvedGlobals.references[assembly]) {
				reference = resolvedGlobals.references[assembly][ref]
				$scope.referencenames[reference.uri] = reference.label;
			}
		}
		$scope.analyses = resolvedAnalysis;

		$scope.selectedAnalysis = null;

		$scope.cancel = function(analysis){
			console.log($scope);
			alert("I cannot cancel jobs yet");
		};
		
		$scope.detail = function(analysis) {
			$scope.selectedAnalysis = analysis;
		}
		
		$scope.haserror = function(analysis) {
			return (JobConstants.STATUS_ERROR == analysis.status);
		}
		
		$scope.showerror = function(analysis) {
			alert(analysis.errorDescription);
		}
		
        $scope.clear = function () {
            $scope.selectedAnalysis = null;
        };

		
}]);


