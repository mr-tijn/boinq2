boinqApp.directive('matchTreeNode', function() {
	console.log('Registering directive matchTreeNode');
	return {
		restrict: "E",
		templateUrl: 'views/match_tree/match_tree_node.html',
		scope : {
			rootNode: "=",
		}

	};
});