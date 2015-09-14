angular.module('boinqApp').filter('nodesummary', function() {
	console.info('Registering filter nodesummary');

	return function(inputNode) {
		switch (inputNode.type) {
		case 'MatchLocation' :
			var res = "Location " + inputNode.contig;
			res += "[";
			res += inputNode.start?inputNode.start:'*';
			res += "-";
			res += inputNode.end?inputNode.end:'*';
			res += "]";
			switch (inputNode.strand) {
			case 'true':
				res += '(+)';
				break;
			case 'false':
				res += '(-)';
				break;
			}
			return res; 
		default:
			return inputNode.type + " " + inputNode.name;

		}
	};
});

angular.module('boinqApp').filter('detailurl', function() {
	console.info('Registering filter detailurl');

	return function(inputNode) {
		return "scripts/components/match_tree/node_detail_" + inputNode.type + ".html";
	};
});