boinqApp.factory('queryFromMatch', ['$http',function($http) {
	console.info('Registering factory queryFromMatch');
	return function(tree) {

		console.log(['Writing tree ', tree].join('\n'));
		
		var promise = $http({
			method : 'POST',
			url: '/app/rest/querybuilder/from_match',
			data: tree,
			headers: {
				'Content-Type' : 'application/json'
			}
		});
		
		return promise;
	};
}]);
