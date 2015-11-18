angular.module('boinqApp').controller("JobController",['$scope','Job','jobstatus'],function($scope,JobService,jobstatus) {
	console.log("Registering JobController");
	
    $scope.jobs = resolvedJob;

    $scope.cancel = function(job){
    	alert("i cannot cancel jobs yet");
    };
});


