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
	
	STATUS_ITEMS : {
  		'0': "Empty",
  		'1': "Contains raw data"
	}
	
});

angular.module('boinqApp').constant('DataFileConstants', {
	
	STATUS_LOADING : 0,
	STATUS_COMPLETE : 1,
	STATUS_ERROR : 2,
	
	STATUS_ITEMS : {
  		'0': "Loading into endpoint",
  		'1': "Complete",
  		'2': "Error"
	}

});

angular.module('boinqApp').constant('SPARQLConstants',{
	
	DEFAULT_PREFIXES : ["PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
	                    "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"].join('\n'),
	                    
	QUERY_GET_ROOT_TERMS : ["SELECT ?uri ?label WHERE {",
	                        "  ?uri rdfs:label ?label.",
	                        "  OPTIONAL{?uri rdfs:subClassOf ?super}",
	                        "  FILTER(!bound(?super))","}"].join('\n'),
	                        
	STATIC_ENDPOINT : "http://localhost:8080/bigdata/#namespaces/boinq_static/sparql"
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