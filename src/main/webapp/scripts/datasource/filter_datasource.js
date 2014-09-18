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