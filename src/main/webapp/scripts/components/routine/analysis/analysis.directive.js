angular.module('boinqApp').directive('analysisdetail', ['AnalysisConstants',function(AnalysisConstants) {
	return {
		restrict: 'E',
		scope: {
			analysis: '=',
			referencenames: '='
		},
		link: function(scope, element, attrs) {
			scope.getProgress = function(analysis) {
				var result = [];
				if (analysis.progressPercentages == null) return result;
				for (prog in analysis.progressPercentages) {
					result.push({reference: scope.referencenames[prog], progress: analysis.progressPercentages[prog]});
				}
				return result;
			}
			scope.computeUrl = function(analysis) {
				var page = 'undefined';
				if (analysis && analysis.type != null) {
					switch (analysis.type) {
					case AnalysisConstants.TYPE_FEATURESELECTION: 
						page = 'featureselection';
					}
				}
				scope.detailUrl = 'scripts/components/routine/analysis/detail/' + page + '.html';
			};
			scope.computeUrl(scope.analysis);
			scope.$watch("analysis", scope.computeUrl);
		},
		templateUrl: 'scripts/components/routine/analysis/detail/common.html'
	}
}]);