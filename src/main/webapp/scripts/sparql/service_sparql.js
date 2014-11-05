boinqApp.factory('callEndpoint', ['$http',function($http) {
	console.info('Registering factory callEndPoint');
	var callEndpoint = function(endpointUrl, graphUri, sparqlQuery) {

		console.log(['Calling endpoint', endpointUrl, 'Graph', graphUri, 'Query', sparqlQuery].join('\n'));
		var params = {
				query: 	sparqlQuery,
		};
		if (graphUri != null) {
			params['default-graph-uri'] = graphUri;
		}

		var promise = $http({
			method : 'GET',
			url: endpointUrl,
			params: params,
			headers: {
				'Accept': 'application/sparql-results+json'
			}
		});

		return promise;
	};
	return callEndpoint;
}]);


boinqApp.factory('updateEndpoint', ['$http',function($http) {
	console.info('Registering factory updateEndPoint');
	var updateEndpoint = function(endpointUrl, graphUri, sparqlQuery) {

		console.log(['Putting to endpoint', endpointUrl, 'Graph', graphUri, 'Query', sparqlQuery].join('\n'));
		
		console.log($http.defaults);
		
		var promise = $http({
			method : 'POST',
			url: endpointUrl,
			params: {graph: graphUri},
			data: 'update=' + sparqlQuery,
			headers: {
				'Content-Type' : 'application/x-www-form-urlencoded',
				'X-Requested-With': undefined
			}
		});
		
		return promise;
	};
	return updateEndpoint;
}]);

boinqApp.factory('SPARQLTools',function() {
	console.info("Registering factory SPARQLTools");
	
	var angle_bracket = function(input) {
		if (typeof input == 'string' || input instanceof String) {
			var matches = input.match(/^<.*>$/);
			if (matches != null && matches.length) return input;
			else return "<"+input+">";
		}
	};
	
	return {
		angle_bracket: angle_bracket
	};

});