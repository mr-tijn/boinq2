angular.module('boinqApp').filter('jobstatus', ['JobConstants', function(JobConstants) {

	console.log('Registering filter jobstatus');

	return function(status) {
		return JobConstants.STATUS_ITEMS[status];
	};
}]);