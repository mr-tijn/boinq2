boinqApp.directive('trackView', function() {
	console.log('Registering directive trackView');
	return {
		restrict: "E",
		templateUrl: 'views/tracks.html',
		scope : {
			datasource: '=bindDatasource',
		}

	};
});