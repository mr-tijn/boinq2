angular.module('boinqApp').constant('GlobalConstants', {
	supportedSpecies : [{label:'Homo sapiens'}],
});


angular.module('boinqApp').constant('DatasourceConstants', {
	TYPE_LOCAL_FALDO : 0,
	TYPE_REMOTE_FALDO : 1,
	TYPE_LOCAL_SPARQL : 2,
	TYPE_REMOTE_SPARQL : 3,


	TYPE_ITEMS : {
		'0' : 'Local Faldo endpoint',
		'1' : 'Remote Faldo endpoint',
		'2' : 'Local generic SPARQL',
		'3' : 'Remote generic SPARQL'
	},
	
});

angular.module('boinqApp').constant('TrackConstants', {

	STATUS_EMPTY : 0,
	STATUS_RAW_DATA : 1,
	STATUS_PROCESSING : 2,
	STATUS_DONE : 3,
	STATUS_ERROR : 4,

	
	
	STATUS_ITEMS : {
  		'0': "Empty",
  		'1': "Contains raw data",
  		'2': "Processing",
  		'3': "Done",
  		'4': "Error"
	}
	
});

angular.module('boinqApp').constant('DataFileConstants', {

	STATUS_WAITING : 0,
	STATUS_LOADING : 1,
	STATUS_COMPLETE : 2,
	STATUS_ERROR : 3,

	STATUS_ITEMS : {
		'0': "Waiting for processing",
  		'1': "Loading into endpoint",
  		'2': "Complete",
  		'3': "Error"
	}

});

angular.module('boinqApp').constant('SPARQLConstants',{
	
	DEFAULT_PREFIXES : ["PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
	                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"].join('\n'),
	                    
	QUERY_GET_ROOT_TERMS : ["SELECT ?uri ?label WHERE {",
	                        "  ?uri rdfs:label ?label.",
	                        "  OPTIONAL{?uri rdfs:subClassOf ?super}",
	                        "  FILTER(!bound(?super))","}"].join('\n'),
	                        
	STATIC_ENDPOINT : "/static/sparql"
});



angular.module('boinqApp').constant('Endpoints',{
	localQuery: "/local/sparql",
	localUpdate: "/local/update",
	metaGraph: "http://www.boinq.org/iri/graph/meta/"
});

angular.module('boinqApp').constant('AnalysisConstants',{
	TYPE_FEATURESELECTION : 0,
});

angular.module('boinqApp').constant('CriteriaConstants', {
	LOCATION_CRITERIA : "LocationFilter",
	FEATURETYPE_CRITERIA : "FeatureTypeFilter",
	MATCHTERM_CRITERIA : "MatchTermFilter",
	MATCHINTEGER_CRITERIA : "MatchIntegerFilter",
	MATCHDECIMAL_CRITERIA : "MatchDecimalFilter",
	MATCHSTRING_CRITERIA : "MatchStringFilter"
});

angular.module('boinqApp').constant('JobConstants',{
	
	STATUS_PENDING : 0,
	STATUS_COMPUTING : 1,
	STATUS_SUCCESS : 2,
	STATUS_ERROR : 3,
	STATUS_UNKNOWN : 4,
	
	STATUS_ITEMS : {
		'0' : "Pending",
		'1' : "Computing",
		'2' : "Succesful",
		'3' : "Error",
		'4' : "Unknown"
	}

	
});

angular.module('boinqApp').constant('FilterConstants',{
	TYPE_GENERIC_EQUALS : 0,
	TYPE_GENERIC_BETWEEN : 1,
	TYPE_GENERIC_STARTSWITH : 2,
	TYPE_GENERIC_CONTAINS :  3,
	
	TYPE_GENERIC_VALUES : 20,
	TYPE_FALDOLOCATION : 21

});


angular.module('boinqApp').constant('NodeConstants',{
	TYPE_ENTITY : 0,
	TYPE_LITERAL : 1,
	TYPE_FALDOENTITY : 2,
	TYPE_ATTRIBUTE : 3,
	TYPE_TYPEDENTITY : 4,
	
	TYPE_ITEMS : {
		'0' : 'Entity',
		'1' : 'Literal',
		'2' : 'Location',
		'3' : 'Attribute',
		'4' : 'Typed entity'
	},
	
	SOURCE_LIST : 0,
	SOURCE_ENDPOINT : 1,
	SOURCE_FIXED : 2,

	SOURCE_ITEMS : {
		'0'	: 'List',
		'1' : 'Endpoint',
		'2'	: 'Fixed'
	}
	
});

