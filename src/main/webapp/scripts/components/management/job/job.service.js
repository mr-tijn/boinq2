boinqApp.factory('Job', ['$resource',
    function ($resource) {
    	console.info('Registering resource Job');
        return $resource('app/rest/jobs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
