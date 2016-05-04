angular.module('boinqApp').controller("JobController",['$scope','Job','jobstatus','resolvedJob',function($scope,JobService,jobstatus,resolvedJob) {
	console.log("Registering JobController");
	
    $scope.jobs = resolvedJob;

    $scope.cancel = function(job){
    	alert("i cannot cancel jobs yet");
    };
}]);


