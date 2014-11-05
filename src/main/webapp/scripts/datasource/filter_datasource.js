boinqApp.filter('dstype', ['DatasourceConstants', function(DatasourceConstants) {

	console.log('Registering filter dstype');

	return function(type) {
		return DatasourceConstants.TYPE_ITEMS[type];
	};
}]);

boinqApp.filter('dsstatus', ['DatasourceConstants', function(DatasourceConstants) {

	console.log('Registering filter dsstatus');

	return function(status) {
		return DatasourceConstants.STATUS_ITEMS[status];
	};
}]);

boinqApp.filter('dfstatus', ['DataFileConstants', function(DataFileConstants) {

	console.log('Registering filter dfstatus');

	return function(status) {
		return DataFileConstants.STATUS_ITEMS[status];
	};
}]);