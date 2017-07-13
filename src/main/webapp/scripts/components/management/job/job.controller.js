angular.module('boinqApp').controller("JobController",['$scope','Job','resolvedJob',function($scope,JobService,resolvedJob) {
	console.log("Registering JobController");
	
    $scope.jobs = resolvedJob;

    $scope.cancel = function(job){
    	alert("i cannot cancel jobs yet");
    };
}]);


