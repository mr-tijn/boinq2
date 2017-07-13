angular.module('boinqApp').factory('overElement', [function () {


	var hitTest = function(clientX, clientY) {
		return window.document.elementFromPoint(clientX, clientY);
	};
	
	var findModel = function(element,property) {
		if (element == undefined || element == null || element.length == 0) {
			return null;
		} else {
			var elementScope = angular.element(element).data(undefined).$scope;
			if (elementScope != null && elementScope.hasOwnProperty(property)) {
				return elementScope[property];
			} else {
				return findModel(element.parentNode,property);
			}
		}
	};
	
	return function(evt, property) {
		var element = hitTest(evt.clientX, evt.clientY);
		return findModel(element, property);
	};
	
}]);