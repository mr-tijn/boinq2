angular.module('boinqApp').filter('trackstatus', ['TrackConstants', function(TrackConstants) {

	console.log('Registering filter trackstatus');

	return function(status) {
		return TrackConstants.STATUS_ITEMS[status];
	};
}]);