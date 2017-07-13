angular.module('boinqApp').factory('TermTools', ['NodeConstants', function (NodeConstants) {
	console.info("Registering TermTools");
	var XSD = "http://www.w3.org/2001/XMLSchema#"
	var FALDO = "http://biohackathon.org/resource/faldo#";
	var getType = function(term) {
		if (term && (typeof term) == "string") {
			return term.substring(term.indexOf('#')+1);
		}
	};
	var isInteger = function(term) {
		var type = getType(term);
		return "int" == type  || "integer" == type  || "long" == type || "short" == type || "byte" == type ;
	};
	var isFloat = function(term) {
		var type = getType(term);
		return "float" == type  || "double" == type  || "decimal" == type ;
	};
	var isText = function(term) {
		var type = getType(term);
		return "string" == type ;
	}
	var isTerm = function(term) {
		var type = getType(term);
		return "anyURI" == type  || "QName" == type ;
	}
	var isLocation = function(term) {
		var type = getType(term);
		return "Region" == type  ; 
	}
	
	var XSDDouble = XSD + "double";
	var XSDInteger = XSD + "integer";
	var XSDanyURI = XSD + "anyURI";
	var XSDString = XSD + "string";
	var FALDORegion = FALDO + "Region";
	var typeTerms = [XSDDouble, XSDInteger, XSDanyURI, XSDString, FALDORegion];
	var typeLabels = ["Decimal number", "Integer number", "Term", "String", "FALDO Region"];
	var XSDTypes = [];
	for (var i = 0; i < typeTerms.length; i++) {
		XSDTypes.push({term:typeTerms[i], label:typeLabels[i]});
	}
	var nodeTypes = [];
	var addType = function(type) {
		nodeTypes.push({value:type, label:NodeConstants.TYPE_ITEMS[type]});
	};
	addType(NodeConstants.TYPE_ENTITY);
	addType(NodeConstants.TYPE_LITERAL);
	addType(NodeConstants.TYPE_TYPEDENTITY);
	addType(NodeConstants.TYPE_FALDOENTITY);
	//addType(NodeConstants.TYPE_ATTRIBUTE);
	var valueSources = [];
	var addSource = function(source) {
		valueSources.push({value:source, label:NodeConstants.SOURCE_ITEMS[source]});
	};
	addSource(NodeConstants.SOURCE_LIST);
	addSource(NodeConstants.SOURCE_ENDPOINT);
	addSource(NodeConstants.SOURCE_FIXED);
 	return {
		isInteger: isInteger,
		isFloat: isFloat,
		isNumeric: function(term) {
			return isInteger(term) || isFloat(term);
		},
		isText: isText,
		isTerm: isTerm,
		isLocation: isLocation,
		XSDTypes: XSDTypes,
		nodeTypes: nodeTypes,
		valueSources: valueSources
	};
}]);


angular.module('boinqApp').factory('QueryValidator', ['TermTools','NodeConstants',function(TT,Node) {
	return function(queryDefinition) {
		ValidationResult = function(comment, validity) {
			this.comment = comment;
			this.validity = validity;
		};
		
		validateBridge = function(queryBridge) {
			// check prerequisites
			var valid = true;
			var comments = [];
			var fromGraph, toGraph, fromNode, toNode;
				if (queryBridge.fromGraphIdx == null) {
				// should never happen
				valid = false;
				comments.push("no origin graph chosen");
			} else {
				var graphs = queryDefinition.queryGraphs.filter(function(graph) {return graph.idx == queryBridge.fromGraphIdx;});
				if (graphs.length != 1) {
					valid = false;
					comments.push("unknown origin graph");
				} else {
					fromGraph = graphs[0];
					if (queryBridge.fromNodeIdx == null) {
						valid = false;
						comments.push("no origin node chosen");
					} else {
						var nodes = fromGraph.queryNodes.filter(function(node) {return node.idx == queryBridge.fromNodeIdx});;
						if (nodes.length != 1) {
							valid = false;
							comments.push("unknown origin node");
						} else {
							fromNode = nodes[0];
						}
					}
				}
			}
			if (queryBridge.toGraphIdx == null) {
				// should never happen
				valid = false;
				comments.push("no target graph chosen");
			} else {
				var graphs = queryDefinition.queryGraphs.filter(function(graph) {return graph.idx == queryBridge.toGraphIdx;});
				if (graphs.length != 1) {
					valid = false;
					comments.push("unknown target graph");
				} else {
					toGraph = graphs[0];
					if (queryBridge.toNodeIdx == null) {
						valid = false;
						comments.push("no target node chosen");
					} else {
						var nodes = toGraph.queryNodes.filter(function(node) {return node.idx == queryBridge.toNodeIdx});;
						if (nodes.length != 1) {
							valid = false;
							comments.push("unknown target node");
						} else {
							toNode = nodes[0];
						}
					}
				}
			}
			if (valid) {
				
				var isEntity = function(node) {
					return node.type == Node.TYPE_ENTITY || node.type == Node.TYPE_TYPEDENTITY;
				}
				// check compatibility
				if (fromNode.type == Node.TYPE_FALDOENTITY && toNode.type != Node.TYPE_FALDOENTITY ||
					toNode.type == Node.TYPE_FALDOENTITY && fromNode.type != Node.TYPE_FALDOENTITY) {
					valid = false;
					comments.push("Location nodes can only be coupled with other Location nodes");
				}
				if (fromNode.type == Node.TYPE_LITERAL && isEntity(toNode) ||
						isEntity(fromNode) && toNode.type == Node.TYPE_LITERAL) {
					if (!queryBridge.stringToEntityTemplate) {
						valid = false;
						comments.push("Template is needed to convert literal to entity");
					}
				}
				if (fromNode.type == Node.TYPE_LITERAL && toNode.type == Node.TYPE_LITERAL) {
					if (!queryBridge.literalToLiteralMatchType) {
						valid = false;
						comments.push("Match Type required for comparing literals");
					}
				}
				if (fromNode.type == Node.TYPE_FALDOENTITY && toNode.type == Node.TYPE_FALDOENTITY) {
					if (!queryBridge.matchStrand) {
						valid = false;
						comments.push("Match Strand setting required for location bridge");
					}
				}
			}
			return new ValidationResult(comments.join(";\n"), valid);
		};
		
		return {
			validateBridge: validateBridge
		};
	}
}]);

angular.module('boinqApp').factory('QueryDefinitionObjects', [function () {
	console.info("Registering QueryDefinitionObjects");
	return {
		QueryDefinition: function() {
			this.id = null;
			this.status = 0;
			this.name = "";
			this.description= "";
			this.species = "";
			this.assembly = "";
			this.owner = null;
			this.resultAsTable = false;
			this.queryBridges = [];
			this.queryGraphs = [];
			this.targetGraph = "";
			this.targetFile = "";
			this.sparqlQuery = "";
		},
		QueryBridge: function(fromGraph, toGraph) {
			this.id = null;
			this.fromGraphIdx = fromGraph.idx;
			this.toGraphIdx = toGraph.idx;
			this.fromNodeIdx = 0;
			this.toNodeIdx = 0;
			this.stringToEntityTemplate = "";
			this.literalToLiteralMatchType = 0;
			this.matchStrand = false;
		},
		QueryGraph: function(graphtemplateId, x, y, name, idx) {
			this.id = null;
			this.idx = idx;
			this.x = x;
			this.y = y;
			this.name = name;
			this.template = graphtemplateId;
			this.queryEdges = [];
			this.queryNodes = [];
		},
		QueryEdge: function() {
			this.id = null;
			this.template = null;
			this.from = 0;
			this.to = 0;
			this.retrieve = false;
		},
		QueryNode: function(nodeTemplateId, idx) {
			this.id = null;
			this.template = nodeTemplateId;
			this.nodeFilters = [];
			this.entityValues = [];
			this.idx = idx;
		},
		NodeFilter: function() {
			this.id = null;
			this.type = 0;
			this.exactMatch = false;
			this.caseInsensitive = false;
			this.not = false;
			this.minInteger = null;
			this.maxInteger = null;
			this.integerValue = null;
			this.minDouble = null;
			this.maxDouble = null;
			this.doubleValue = null;
			this.includeMin = false;
			this.includeMax = false;
			this.stringValue = "";
			this.termValues = [];
			this.contig = "";
			this.strand = false;
		},
		GraphTemplate: function() {
			this.id = null;
			this.type = null;
			this.name = "";
			this.endpointUrl = "";
			this.graphIri = "";
			this.edgeTemplates = [];
			this.nodeTemplates = [];
		},
		EdgeTemplate: function() {
			this.id = null;
			this.from = null;
			this.to = null;
			this.term = "";
			this.label = "";
		},
		NodeTemplate: function(idx) {
			this.id = null;
			this.idx = idx;
			this.name = "";
			this.nodeType = 0;
			this.description = "";
			this.variablePrefix = "";
			this.valueSource = null;
			this.valuesTermList = [];
			this.valuesEndpoint = "";
			this.valuesGraph = "";
			this.valuesRootTerm = "";
			this.fixedValue = "";
			this.literalXsdType = "";
			this.assembly = "";
			this.filterable = false;
			this.color = "";
			this.x = 0;
			this.y = 0;
		},
	}
}]);