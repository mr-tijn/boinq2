// need to solve recursion problem with angular;
// http://stackoverflow.com/questions/14430655/recursion-in-angular-directives

boinqApp.factory('RecursionHelper', ['$compile', function($compile){
    return {
        /**
         * Manually compiles the element, fixing the recursion loop.
         * @param element
         * @param [link] A post-link function, or an object with function(s) registered via pre and post properties.
         * @returns An object containing the linking functions.
         */
        compile: function(element, link){
            // Normalize the link parameter
            if(angular.isFunction(link)){
                link = { post: link };
            }

            // Break the recursion loop by removing the contents
            var contents = element.contents().remove();
            var compiledContents;
            return {
                pre: (link && link.pre) ? link.pre : null,
                /**
                 * Compiles and re-adds the contents
                 */
                post: function(scope, element){
                    // Compile the contents
                    if(!compiledContents){
                        compiledContents = $compile(contents);
                    }
                    // Re-add the compiled contents to the element
                    compiledContents(scope, function(clone){
                        element.append(clone);
                    });

                    // Call the post-linking function, if any
                    if(link && link.post){
                        link.post.apply(null, arguments);
                    }
                }
            };
        }
    };
}]);



boinqApp.directive('termTreeNode', function(RecursionHelper) {
	console.info('Registering directive termTreeNode');
	return {
		restrict: 'E',
		templateUrl: 'views/term_tree/term_tree_node.html',
		compile: function(element) {
			return RecursionHelper.compile(element);
	    },
	};
});

boinqApp.directive('termTreePicker', function() {
	console.info('Registering directive termTreePicker');
	return {
		restrict: 'E',
		templateUrl: 'views/term_tree/term_tree_root.html',
		scope : {
			sourceGraph: "@",
			sourceEndpoint: "@",
			selectHandler : "=",
			rootNodesQuery : "@",
			searchFilter : "="
		}
	};
});
