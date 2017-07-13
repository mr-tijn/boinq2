'use strict';

describe('Queryrenderer', function () {

	beforeEach(module('templates.cache'));
	beforeEach(module('boinqApp'));

	var elm, scope, httpBackend, templateCache;

	beforeEach(inject(['$compile','$rootScope','$httpBackend','$templateCache',function($compile, $rootScope,$httpBackend,$templateCache) {

		httpBackend = $httpBackend;
		templateCache = $templateCache;

		httpBackend.whenGET(/.*/).respond({});

		var html = '<queryrenderer query-definition="qd">{{qd | json}}</queryrenderer>';

		scope = $rootScope.$new();
		scope.qd = {
				id: 99,
				resultAsTable: false,
				queryBridges: [{
					id:99,
					fromGraphIdx: 1,
					toGraphIdx: 2,
					fromNodeIdx: 0,
					toNodeIdx: 0,
					stringToEntityTemplate: "",
					literalToLiteralMatchType:0,
					matchStrand:false
				}],
				queryGraphs: [{
					id: 99,
					idx: 1,
					x: 10,
					y: 10,
					template: 99,
					queryEdges: [],
					queryNodes: []
				},
				{
					id: 100,
					idx: 2,
					x: 20,
					y: 20,
					template: 99,
					queryEdges: [],
					queryNodes: []
				}],
				targetGraph: 'http://target.graph',
				name: 'test',
				description: 'a test querydefinition',
				species: 'Homo sapiens',
				assembly: 'GRCh37'
		};
		elm = angular.element(html);

		$compile(elm)(scope);
		// need to resolve the $promise that gets the template from the cache
		$rootScope.$digest();

	}]));


	describe('Renderer', function () {
		it("Cache should contain template for queryrenderer", function() {
			// templates should be cached by ng-html2js - see karma config
			expect(templateCache.get("scripts/components/graphbuilder/queryrenderer/queryrenderer.html")).toBeTruthy();
			httpBackend.flush();
		});
		it("Has two nodes and one edge", function() {
			var graphs = 2;
			var bridges = 1;
			var circles = 2*graphs; // ring and core
			var lines = bridges + 1; // selector line and bridges
			expect(elm[0].querySelectorAll('circle').length).toEqual(circles);
			expect(elm[0].querySelectorAll('line').length).toEqual(lines);
			httpBackend.flush();
		});
	});
});
