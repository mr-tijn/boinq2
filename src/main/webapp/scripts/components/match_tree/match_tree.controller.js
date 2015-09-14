angular.module('boinqApp').controller("MatchTreeController",['$scope','queryFromMatch',function($scope,queryFromMatch) {
	console.log('Registering controller TreeController');


	// init

	var nodeTypes = ['MatchAll','MatchAny','MatchLocation','MatchField'];
	var contigs = ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','X','Y']; 
	$scope.mainData = {
			tree: [$scope.rootNode],
			rootNode: $scope.rootNode,
			activeNode: $scope.rootNode,
			nodeTypes: nodeTypes,
			contigs: contigs
	};

	$scope.node = $scope.rootNode;

	$scope["delete"] = function(node) {
		node.nodes = [];
	};


	var getparentnode = function(tree, childNode) {

		for (var i = 0, l = tree.nodes.length; i < l; i++) {
			var node = tree.nodes[i];
			if (node === childNode) return tree;
			else {
				var sub =  getparentnode(node, childNode);
				if (sub != null) return sub;
			}
		}
		return null;
	};

	$scope.remove = function(node) {
		var parent = getparentnode($scope.mainData.rootNode, node);
		if (parent != null) {
			var newNodes = [];
			for (var i = 0, l = parent.nodes.length; i < l; i++) {
				var child = parent.nodes[i];
				if (child === node) continue;
				newNodes.push(child);
			}
			parent.nodes = newNodes;
		}
	};

	$scope.add = function(node) {
		// fixme: name duplicates when creating after deletion
		var newName, newNode, post;
		post = node.nodes.length + 1;
		newName = node.name + '-' + post;
		node.nodes.push(newNode = {
				name: newName,
				type: "MatchAll",
				nodes: [],
		});
		return newNode;
	};

	$scope.setActive = function(node) {
		$scope.mainData.activeNode = node;
	};

	$scope.isActive = function(node) {
		return (node === $scope.mainData.activeNode);
	};

	$scope.canHaveChildren = function(node) {
		switch (node.type) {
		case 'MatchAll':
		case 'MatchAny':
			return true;
		default:
			return false;
		}
	};

	$scope.toggleActive = function(node) {
		if (isActive(node)) {
			setActive({});
		} else {
			setActive(node);
		}
	};

	$scope.submit = function(tree) {
		$scope.generatedQuery = "";
		$scope.errorMessage = null;
		queryFromMatch($scope.rootNode).then(function(result) {console.log(result); $scope.generatedQuery = result.data.query;},function (error) {$scope.errorMessage = error;});
	};


}]);
