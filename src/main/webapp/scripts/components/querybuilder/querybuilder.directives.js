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
			scope.detailUrl = 'scripts/components/querybuilder/featurejoin_detail/' + scope.join.type + '.html';
			scope.$watch("join", function(join) {
				if (join == null) scope.detailUrl = 'scripts/components/querybuilder/featurejoin_detail/undefined.html';
				else scope.detailUrl = 'scripts/components/querybuilder/featurejoin_detail/' + join.type + '.html';
				console.log("detailurl changed");
			});
		},
		template: '<div ng-include="detailUrl"></div>'
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
				scope.featureselect.criteria.push({type:''});
			}
			scope.selectCriterion = function(crit) {
				scope.activeCriterion = crit;
			}
			scope.detailUrl = 'scripts/components/querybuilder/featureselect_detail/' + scope.featureselect.type + '.html';
			scope.$watch("featureselect", function(featureselect) {
				if (featureselect == null) scope.detailUrl = 'scripts/components/querybuilder/featureselect_detail/undefined.html';
				else scope.detailUrl = 'scripts/components/querybuilder/featureselect_detail/' + featureselect.type + '.html';
				console.log("detailurl changed");
			});
		},
		templateUrl: 'scripts/components/querybuilder/featureselect_detail/common.html'
	}
});

angular.module('boinqApp').directive('criteriondetail', function() {
	return {
		restrict: 'E',
		scope: {
			criterion: '='
		},
		link: function(scope, element, attrs) {
			scope.detailUrl = 'scripts/components/querybuilder/criterion_detail/' + scope.criterion.type + '.html';
			scope.$watch("criterion", function(criterion) {
				if (featureselect == null) scope.detailUrl = 'scripts/components/querybuilder/criterion_detail/undefined.html';
				else scope.detailUrl = 'scripts/components/querybuilder/criterion_detail/' + criterion.type + '.html';
				console.log("detailurl changed");
			});
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