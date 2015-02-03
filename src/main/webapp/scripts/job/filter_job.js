boinqApp.filter('jobstatus', ['JobConstants', function(DatasourceConstants) {

	console.log('Registering filter jobstatus');

	return function(status) {
		return JobConstants.STATUS_ITEMS[status];
	};
}]);