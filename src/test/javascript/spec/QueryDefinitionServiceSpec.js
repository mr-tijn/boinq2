'use strict';

describe('QueryDefinition Service Tests ', function () {

	var testedService, httpBackend;

    beforeEach(module('boinqApp'));

	beforeEach(function() {
		angular.mock.inject(function ($injector) {
            httpBackend = $injector.get('$httpBackend');
            testedService = $injector.get('QueryDefinition');
        });
	});

	beforeEach(function () {
		httpBackend.whenGET("app/rest/querydefinition/99").respond({targetGraph: "someGraph"});
		httpBackend.whenGET(/.*/).respond({});
	});
	
	it ("Should call backend", function() {
        testedService.get({id:99}).$promise.then(function(result) {
        	expect(result.targetGraph).toEqual("someGraph");
		}, function(error) {
			fail(error);
		});
       httpBackend.flush();
	});

    afterEach(function() {
        httpBackend.verifyNoOutstandingExpectation();
        httpBackend.verifyNoOutstandingRequest();
    });
	
});
