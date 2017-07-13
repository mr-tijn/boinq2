angular.module('boinqApp').directive('trackView', function() {
	console.log('Registering directive trackView');
	return {
		restrict: "E",
		templateUrl: 'scripts/components/management/track/tracks.html',
		scope : {
			datasource: '=bindDatasource',
		}

	};
});