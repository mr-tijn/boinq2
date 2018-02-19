angular.module('boinqApp').factory('boinqOneTimeFetch', ['$q','callEndpoint','Endpoints','QueryBuilderService',function($q,callEndpoint,Endpoints,QueryBuilderService) {
	console.info('Registering factory boinqOneTimeFetch');

	var resolved = false;
	
	var speciesList = [];
	var assemblies = [];
	var references = [];
    
	var queryPromise = function(response) {
		return callEndpoint(Endpoints.localQuery, Endpoints.metaGraph, response.query);
	}
	
	var findSpeciesPromise = QueryBuilderService.mappingQuery().then(queryPromise);
	var findAssembliesPromises = [];
	var findReferencesPromises = [];

	findSpeciesPromise.then(
			function (successResponse) {					
				var species = successResponse.data.results.bindings;
				for (var idx in species){
					var speciesUri = species[idx].uri.value;
					var speciesLabel = species[idx].label.value;
					speciesList.push({'uri': speciesUri, 'label': speciesLabel});
					assemblies[speciesLabel] = [];
					findAssemblies(speciesLabel);
				}
			}, function (errorResponse) {
				console.error(errorResponse);
			});
	
	var findAssemblies = function(speciesLabel) {
		findAssembliesPromise = QueryBuilderService.assemblyQuery(speciesLabel).then(queryPromise);
		findAssembliesPromises.push(findAssembliesPromise);
		findAssembliesPromise.then(function (successResponse) {					
			var assembly = successResponse.data.results.bindings;
			for (var idx in assembly){
				var assemblyUri = assembly[idx].uri.value;
				var assemblyLabel = assembly[idx].label.value;
				assemblies[speciesLabel].push({'uri': assemblyUri, 'label': assemblyLabel});
				references[assemblyUri] = [];
				findReferences(assemblyUri);
			}
		}, function (errorResponse) {
			console.error(errorResponse);
		});
	};
	
	
	var findReferences = function(assemblyUri) {
		findReferencesPromise = QueryBuilderService.referenceQuery(assemblyUri).then(queryPromise);
		findReferencesPromises.push(findReferencesPromise);
		findReferencesPromise.then(
				function (successResponse) {
					var results = [];
					var refs = successResponse.data.results.bindings;
					for (var idx in refs) {
						results.push({'uri':refs[idx].uri.value, 'label':refs[idx].label.value});
					}
					references[assemblyUri] = results;
				},
				function (errorResponse) {
					console.error(errorResponse);
				});
	} 
	
	if (resolved) {
		return $q.resolve({
		  					species: speciesList,
		  					assemblies: assemblies,
		  					references: references
						  });
	}
	else {
		return findSpeciesPromise.then(function (){return $q.all(findAssembliesPromises);})
					  			 .then(function (){return $q.all(findReferencesPromises);})
					  			 .then(function (){
					  				 		resolved = true;
					  						return {
					  								species: speciesList,
					  								assemblies: assemblies,
					  								references: references
											};});
	}
	
}]);