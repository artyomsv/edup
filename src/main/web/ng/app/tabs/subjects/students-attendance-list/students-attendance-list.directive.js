'use strict';

angular.module('edup.subjects')

	.directive('studentsAttendanceList', function () {
		return {
			restrict: 'E',
			templateUrl: 'students-attendance-list',

			controller: function ($scope, $timeout, QueryService, RestService) {

				$scope.resetEventStudentsSearch = function () {
					$scope.eventStudentsSearch = {
						spin: false,
						formats: ['Registered', 'All', 'Not registered'],
						searchValue: '',
						values: [],
						total: 0,
						attendance: []
					};

					$scope.eventStudentsSearch.format = $scope.eventStudentsSearch.formats[0];
				};

				$scope.resetEventStudentsSearch();

				$scope.mapAttendance = function (students) {
					_.forEach(students, function (student) {
						student.active = false;
						_.forEach($scope.eventStudentsSearch.attendance, function (attendance) {
							if (student.id === attendance.studentId) {
								student.active = true;
								student.attendanceId = attendance.attendanceId;
							}
						});
					});
				};

				var studentsQuery = function (skip) {
					var search = _.isEmpty($scope.eventStudentsSearch.searchValue) ? '*' : $scope.eventStudentsSearch.searchValue;
					var filter = null;
					var attendanceStudents = _.pluck($scope.eventStudentsSearch.attendance, 'studentId');
					switch($scope.eventStudentsSearch.format) {
						case 'Registered' : filter = 'Id eq (' + (attendanceStudents || []).join(',') + ')'; break;
						case 'Not registered' : filter = 'Id ne (' + (attendanceStudents || []).join(',') + ')'; break;
					}
					return QueryService.Query(10, skip, search, 'Name asc, LastName asc', filter);
				};

				$scope.executeSearch = function () {
					if ($scope.studentsManagemnt.expanded && !$scope.eventStudentsSearch.spin) {

						$scope.eventStudentsSearch.spin = true;

						var query = studentsQuery(0);

						RestService.Private.Students.get(query).then(function (response) {
							$scope.eventStudentsSearch.values = response.values;
							$scope.eventStudentsSearch.total = response.count;
							$scope.mapAttendance($scope.eventStudentsSearch.values);
							$scope.eventStudentsSearch.spin = false;
						});

					}
				};

				$scope.$watch('eventStudentsSearch.format', function () {
					$scope.executeSearch();
				});

				$scope.loadMoreStudents = function () {
					if ($scope.eventStudentsSearch.values.length !== 0 && ($scope.eventStudentsSearch.values.length === $scope.eventStudentsSearch.total)) {
						return;
					}

					if (!$scope.eventStudentsSearch.spin && $scope.studentsManagemnt.expanded) {
						$scope.eventStudentsSearch.spin = true;
						$scope.eventStudentsSearch.spin = true;

						var query = studentsQuery($scope.eventStudentsSearch.values.length);

						RestService.Private.Students.get(query).then(function (response) {
							var students = response.values;
							_.forEach(students, function (student) {
								$scope.eventStudentsSearch.values.push(student);
							});

							$scope.eventStudentsSearch.total = response.count;
							$scope.mapAttendance(students);
							$scope.eventStudentsSearch.spin = false;
						});
					}
				};

				$scope.updateStudentAttendance = function (student) {
					if (!student.attendanceId) {
						RestService.Private.Subjects
							.one('events')
							.one($scope.selectedEvent.eventId.toString())
							.one('attendance')
							.customPOST({
								eventId: $scope.selectedEvent.eventId,
								studentId: student.id
							})
							.then(function (response) {
								student.attendanceId = response.payload;
								$scope.selectedEvent.students += 1;
							});
					} else {
						RestService.Private.Subjects
							.one('events')
							.one($scope.selectedEvent.eventId.toString())
							.one('attendance')
							.one(student.attendanceId.toString())
							.remove()
							.then(function () {
								_.remove($scope.eventStudentsSearch.attendance, function (attendance) {
									return student.id === attendance.studentId;
								});
								student.attendanceId = null;
								$scope.selectedEvent.students -= 1;
							});
					}
				};

			},

			link: function (scope) {

			}
		};
	}
)
;