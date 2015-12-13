angular.module('boinqApp').factory('callEndpoint', ['$http',function($http) {
	console.info('Registering factory callEndPoint');
	var callEndpoint = function(endpointUrl, graphUri, sparqlQuery) {

		console.log(['Calling endpoint : ', endpointUrl, 'Graph : ', graphUri, 'Query : ', sparqlQuery].join('\n'));
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


angular.module('boinqApp').factory('updateEndpoint', ['$http',function($http) {
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

angular.module('boinqApp').factory('SPARQLTools',function() {
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

angular.module('boinqApp').factory('QueryBuilderService', ['$http', function ($http) {
	console.info("Registering factory QueryBuilderService");
	return {
		rootNodesQuery: function() {
			var promise = $http.get('app/rest/querybuilder/rootNodesQuery').then(function (response) {
				return response.data;
			});
			return promise;
		},
		childNodesQuery: function(parentUri) {
			var promise = $http.get('app/rest/querybuilder/childNodesQuery', {params: {parentUri: parentUri}}).then(function (response) {
				return response.data;
			});
			return promise;
		},
		filteredTreeQuery: function(filter) {
			var promise = $http.get('app/rest/querybuilder/filteredTreeQuery', {params: {filter: filter}}).then(function (response) {
				return response.data;
			});
			return promise;
		},
		insertQuery: function(graphUri, subject, predicate, object)  {
			var promise = $http.get('app/rest/querybuilder/insertQuery', {params: {graphUri: graphUri, subject:subject, predicate: predicate, object: object}}).then(function (response) {
				return response.data;
			});
			return promise;
		},
		featureTypeQuery: function(trackId)  {
			var promise = $http.get('app/rest/querybuilder/featureTypesQuery', {params: {trackId: trackId}}).then(function (response) {
				return response.data;
			});
			return promise;
		}
	};
}]);

angular.module('boinqApp').directive('rdfnode', function() {
	console.info("Registering directive rdfnode");
	var RDFNODE_REGEXP = /^<.+>|".+"$/;
	return {
		require: 'ngModel',
		link: function(scope, elm, attr, ngModel) {
			
			ngModel.$parsers.unshift(function(value) {
				var valid = false;
				if (value == null || RDFNODE_REGEXP.test(value)) {
					valid = true;
				}
				ngModel.$setValidity('rdfnode', valid);
	            return valid ? value : undefined;
			});
			
			ngModel.$formatters.unshift(function(value) {
				ngModel.$setValidity('rdfnode', RDFNODE_REGEXP.test(value));
				return value;
			});
			
		}
	};
});

angular.module('boinqApp').directive('regex', function() {
	console.info("Registering directive regex");
	return {
		require: 'ngModel',
		restrict: 'A',
		link: function(scope, elm, attr, ngModel) {
			var regex = new RegExp(attr.regex);
			ngModel.$parsers.unshift(function(value) {
				var valid = false;
				if (value == undefined || value == "" || regex.test(value)) {
					valid = true;
				}
				ngModel.$setValidity('regex', valid);
	            return valid ? value : undefined;
			});
			
			ngModel.$formatters.unshift(function(value) {
				ngModel.$setValidity('regex', regex.test(value));
				return value;
			});
			
		}
	};
});