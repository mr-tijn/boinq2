describe('Termtree Tests ', function () {

    beforeEach(module('boinqApp'));
    
    describe('TermTreeController', function () {
        var $scope;


        beforeEach(inject(function ($rootScope, $controller) {
            $scope = $rootScope.$new();
            $controller('TermTreeController', {$scope: $scope});
        }));

        it('should correctly make tree', function () {
        	
        	var root1 = {uri:{value:"http://test/root1"}, label:{value:"ROOT1"}};
        	var root2 = {uri:{value:"http://test/root1"}, label:{value:"ROOT2"}};
        	var root1child1 = {uri:{value:"http://test/root1child1"}, label:{value:"ROOT1CHILD1"}, parent:root1.uri};
        	var root1child2 = {uri:{value:"http://test/root1child2"}, label:{value:"ROOT1CHILD2"}, parent:root1.uri};
           	var root1child3 = {uri:{value:"http://test/root1child3"}, label:{value:"ROOT1CHILD3"}, parent:root1.uri};
           	var child2baby1 = {uri:{value:"http://test/child2baby1"}, label:{value:"CHILD2BABY1"}, parent:root1child2.uri};
           	var child2baby2 = {uri:{value:"http://test/child2baby2"}, label:{value:"CHILD2BABY2"}, parent:root1child2.uri};
        	var root2child1 = {uri:{value:"http://test/root2child1"}, label:{value:"ROOT2CHILD1"}, parent:root2.uri};
        	
        	var terms=[root2child1, child2baby2, root1child1, root2, root1child3, child2baby1, root1, root1child2];
        	
        	$scope.makeTree(terms);
        	
            expect($scope.rootNodes).toContain(root1);
            expect($scope.rootNodes).toContain(root2);
            expect($scope.rootNodes).not.toContain(root1child1);
            expect(root1.subTerms).toContain(root1child1);
        });
    });

    
});
