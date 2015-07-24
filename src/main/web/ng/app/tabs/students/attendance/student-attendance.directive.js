'use strict';

angular.module('edup.students')

	.directive('studentAttendance', function () {
		return {
			restrict: 'E',
			templateUrl: 'student-attendance',

			controller: function () {

				//$scope.studentAttendanceSearch = {
				//	spin: false,
				//	searchValue: '',
				//	values: [],
				//	total: 0
				//};
				//
				//$scope.loadStudentAttendace = function () {
				//	if ($scope.studentAttendanceSearch.values.length !== 0 && ($scope.studentAttendanceSearch.values.length === $scope.studentAttendanceSearch.total)) {
				//		return;
				//	}
				//
				//	if (!$scope.studentAttendanceSearch.spin) {
				//		$scope.studentAttendanceSearch.spin = true;
				//
				//		var query = QueryService.Query(10, $scope.studentAttendanceSearch.values.length + 10, search, 'Name asc, LastName asc', filter);
				//
				//
				//	}
				//
				//};

				//$scope.attendanceHistory = [
				//	{
				//		'subject': 'Math',
				//		'date': '2015/03/03',
				//		'amount': 15
				//	},
				//	{
				//		'subject': 'Literature',
				//		'date': '2015/03/03',
				//		'amount': 15
				//	},
				//	{
				//		'subject': 'Sport',
				//		'date': '2015/03/04',
				//		'amount': 20
				//	},
				//	{
				//		'subject': 'History',
				//		'date': '2015/03/04',
				//		'amount': 10
				//	},
				//	{
				//		'subject': 'Math',
				//		'date': '2015/03/05',
				//		'amount': 15
				//	},
				//	{
				//		'subject': 'English',
				//		'date': '2015/03/07',
				//		'amount': 15
				//	},
				//	{
				//		'subject': 'Sport',
				//		'date': '2015/03/07',
				//		'amount': 20
				//	}
				//];
			},

			link: function ($scope) {

			}
		};
	}
);