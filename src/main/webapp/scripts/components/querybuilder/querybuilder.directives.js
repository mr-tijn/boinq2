//http://blog.parkji.co.uk/2013/08/11/native-drag-and-drop-in-angularjs.html

angular.module('boinqApp').directive('draggable', function() {
	return {
		scope: {
			dragdata: '='
		},       
		link: function(scope, element) {
			// this gives us the native JS object
			var el = element[0];
			
			el.draggable = true;

			el.addEventListener(
					'dragstart',
					function(e) {
						e.dataTransfer.effectAllowed = 'move';
						e.dataTransfer.setData("text/plain", scope.dragdata);
						this.classList.add('drag');
						return false;
					},
					false
			);

			el.addEventListener(
					'dragend',
					function(e) {
						this.classList.remove('drag');
						return false;
					},
					false
			);
		}
	}
});

angular.module('boinqApp').directive('joindetail', function() {
	return {
		restrict: 'E',
		scope: {
			join: '='
		},
		link: function(scope, element, attrs) {
			scope.computeUrl = function(type) {
				if (type == null) scope.detailUrl = 'scripts/components/querybuilder/featurejoin_detail/undefined.html';
				else scope.detailUrl = 'scripts/components/querybuilder/featurejoin_detail/' + type + '.html';
			};
			scope.computeUrl(scope.join != null ? scope.join.type : null);
			scope.$watch("join.type", scope.computeUrl);
		},
		templateUrl: 'scripts/components/querybuilder/featurejoin_detail/common.html'
	}
});

angular.module('boinqApp').directive('featureselectdetail', function() {
	return {
		restrict: 'E',
		scope: {
			featureselect: '='
		},
		link: function(scope, element, attrs) {
			scope.addCriterion = function() {
				var newcrit = {type:'undefined'};
				scope.selectCriterion(newcrit);
				scope.featureselect.criteria.push(newcrit);
			}
			scope.removeCriterion = function(crit) {
				var idx = scope.featureselect.criteria.indexOf(crit);
				if (idx > -1) {
					scope.featureselect.criteria.splice(idx,1);
				}
			}
			scope.selectCriterion = function(crit) {
				scope.activeCriterion = crit;
			}
			scope.computeUrl = function(featureselect) {
				scope.activeCriterion = null;
				if (featureselect == null) scope.detailUrl = 'scripts/components/querybuilder/featureselect_detail/undefined.html';
				else scope.detailUrl = 'scripts/components/querybuilder/featureselect_detail/' + featureselect.type + '.html';
			};
			scope.computeUrl(scope.featureselect);
			scope.$watch("featureselect", scope.computeUrl);
		},
		templateUrl: 'scripts/components/querybuilder/featureselect_detail/common.html'
	}
});

angular.module('boinqApp').directive('criteriondetail', function() {
	return {
		restrict: 'E',
		scope: {
			criterion: '=',
			supportedFeatureTypes: '=',
			supportedFilters: '='
		},
		link: function(scope, element, attrs) {
			scope.termPicked = function(term) {
				newTerm = { uri: term.uri.value, label: term.label.value };
				scope.criterion.term = newTerm;
			}
			scope.computeUrl = function(criterion) {
				if (criterion && criterion.filter) scope.detailUrl = 'scripts/components/querybuilder/criterion_detail/' + criterion.filter.filterTypeName + '.html';
				else scope.detailUrl = 'scripts/components/querybuilder/criterion_detail/undefined.html';
			};
			scope.computeUrl(scope.criterion);
			scope.$watch("criterion", scope.computeUrl);
		},
		templateUrl: 'scripts/components/querybuilder/criterion_detail/common.html'
	}
});

angular.module('boinqApp').directive('droppable', function() {
	return {
		scope: {
			drop: '&', // parent

		},       
		link: function(scope, element) {
			// again we need the native object
			var el = element[0];

			el.addEventListener(
					'dragover',
					function(e) {
						e.dataTransfer.dropEffect = 'move';
						// allows us to drop
						if (e.preventDefault) e.preventDefault();
						this.classList.add('over');
						return false;
					},
					false
			);

			el.addEventListener(
					'dragenter',
					function(e) {
						this.classList.add('over');
						return false;
					},
					false
			);

			el.addEventListener(
					'dragleave',
					function(e) {
						this.classList.remove('over');
						return false;
					},
					false
			);

			el.addEventListener(
					'drop',
					function(e) {
						// Stops some browsers from redirecting.
						if (e.stopPropagation) e.stopPropagation();
						if (e.preventDefault) e.preventDefault();

						this.classList.remove('over');
						var data = e.dataTransfer.getData("text/plain");
						// call the drop passed drop function
						scope.$apply(function(scope) {
							var fn = scope.drop();
							if ('undefined' !== typeof fn) {
								fn(e,el,data);
							}
						});

						return false;
					},
					false
			);


		}
	}
});