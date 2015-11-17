angular.module('boinqApp').controller("QueryBuilderController",['$scope','dragging',function($scope,dragging) {

	console.log("Registering controller QueryBuilderController");
	
	$scope.datasources = [{name: "datasource1"},{name: "datasource2"}];
	$scope.activeDS = [];
	$scope.links = [];
	$scope.selectedDS = undefined;
	$scope.overDS = null;
	$scope.overLn = null;
	
	link = function(srcDS,dstDS) {
		return {
			src: srcDS,
			dst: dstDS
		} 
	}
	
	$scope.dropped = function(event,element,data) {
		activeDS(event.offsetX, event.offsetY, data);
	}
	
	var hitTest = function(clientX, clientY) {
		return this.document.elementFromPoint(clientX, clientY);
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

	$scope.mouseMove = function (evt) {
		var element = hitTest(evt.clientX, evt.clientY);
		$scope.overDS = findModel(element,'ds');
		$scope.overLn = findModel(element,'ln');
	};
	
	$scope.overLink = function(ln) {
		if ($scope.overLn == ln) {
			return "over";
		} else {
			return "";
		}
	}
	
	$scope.selectedLink = function(ln) {
		if ($scope.selectedLine == ln) {
			return "selected";
		} else {
			return "";
		}
	}
	
	$scope.lineMouseDown = function(event,ln) {
		dragging.startDrag(event, {
			clicked: function () {
				$scope.selectedLine = ln;
				$scope.selectedDS = null;
			}
		});
	}
	
	$scope.nodeMouseDown = function(event, ds) {
		 dragging.startDrag(event, {
		        dragStarted: function (x, y) {
		        	lastMouseCoords = {x:x, y:y};
		        },
		        dragging: function (x, y) {
		            curCoords = {x:x, y:y};
		            
		            var deltaX = curCoords.x - lastMouseCoords.x;
		            var deltaY = curCoords.y - lastMouseCoords.y;

		            ds.xc = ds.xc + deltaX;
		            ds.yc = ds.yc + deltaY;
		            ds.x = ds.xc + "px";
		            ds.y = ds.yc + "px";

		            lastMouseCoords = curCoords;
		        },
		        clicked: function () {
		        	$scope.selectedDS = ds;
		        	$scope.selectedLine = null;
		        },
		        dragEnded: function() {
		        	
		        }
		    });
	}
	
	$scope.ringMouseDown = 	function(event, ds) {
		 dragging.startDrag(event, {
		        dragStarted: function (x, y) {
		        	lastMouseCoords = {x:x, y:y};
		        	$scope.selectLine = {x1:ds.xc, y1:ds.yc, x2:ds.xc, y2:ds.yc};
		        },
		        dragging: function (x, y) {
		            curCoords = {x:x, y:y};
		            var deltaX = curCoords.x - lastMouseCoords.x;
		            var deltaY = curCoords.y - lastMouseCoords.y;
		            $scope.selectLine.x2 += deltaX;
		            $scope.selectLine.y2 += deltaY;
		            lastMouseCoords = curCoords;
		        },
		        clicked: function () {
		        	
		        },
		        dragEnded: function() {
		        	$scope.selectLine = undefined;
		        	if ($scope.overDS != null && $scope.overDS != ds) {
		        		$scope.links.push(link(ds,$scope.overDS));
		        		console.log("linking " + ds + " to " + $scope.overDS);
		        	}
		        }
		    });
		
		
	}
	
	activeDS = function(x, y, dsName) {
	    var xpos = x + "px";
	    var ypos = y + "px";
	    
		$scope.activeDS.push({name: dsName, x:xpos, y:ypos, xc:x, yc:y});
	}
	
}]);

	