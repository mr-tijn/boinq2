boinqApp.directive('matchTree', function() {
	console.log('Registering directive matchTree');
	return {
		restrict: "E",
		templateUrl: 'views/match_tree/match_tree_root.html',
		scope : {
			rootNode: "=",
		}

	};
});
boinqApp.directive('matchTreeNode', function() {
	console.log('Registering directive matchTreeNode');
	return {
		restrict: "E",
		templateUrl: 'views/match_tree/match_tree_node.html'
	};
});
