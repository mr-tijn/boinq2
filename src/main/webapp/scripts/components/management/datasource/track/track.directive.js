angular.module('boinqApp').directive('trackView', function() {
	console.log('Registering directive trackView');
	return {
		restrict: "E",
		templateUrl: 'scripts/components/management/datasource/track/tracks.html',
		scope : {
			datasource: '=bindDatasource',
		}

	};
});