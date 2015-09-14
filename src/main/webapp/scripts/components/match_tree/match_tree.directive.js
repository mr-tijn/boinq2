angular.module('boinqApp').directive('matchTree', function() {
	console.log('Registering directive matchTree');
	return {
		restrict: "E",
		templateUrl: 'scripts/components/match_tree/match_tree_root.html',
		scope : {
			rootNode: "=",
		}

	};
});
angular.module('boinqApp').directive('matchTreeNode', function() {
	console.log('Registering directive matchTreeNode');
	return {
		restrict: "E",
		templateUrl: 'scripts/components/match_tree/match_tree_node.html'
	};
});
