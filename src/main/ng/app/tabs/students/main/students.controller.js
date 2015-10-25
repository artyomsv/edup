'use strict';

angular.module('edup.students')

	.controller('StudentsController', function ($scope, $timeout, $filter, RestService, PaginationService, QueryService) {

		$scope.studentSelected = false;
		$scope.basicSearch = {
			spin: false
		};
		$scope.studentPaging = {
			enabled: false,
			page: 1,
			perPage: 10,
			totalRecords: 0
		};

		$scope.loadFullStudent = function (id) {
			if (id) {
				$scope.basicSearch.spin = true;
				RestService.Private.Students.one(id.toString()).get().then(function (response) {
					$scope.selectedStudent = response.payload;
					if ($scope.selectedStudent) {
						$scope.studentEdit = _.cloneDeep($scope.selectedStudent);
						if ($scope.studentEdit.birthDate) {
							$scope.studentEdit.birthDateString = $filter('date')(new Date($scope.studentEdit.birthDate), 'yyyy-MM-dd');
						}

						$scope.selectedStudent.balance = (response.payload.balance / 100);

						$scope.studentSelected = true;
						$scope.basicSearch.spin = false;
					}
				});
			}
		};

		$scope.loadStudents = function (id, top, skip, search) {
			$scope.basicSearch.spin = true;
			var query = QueryService.Query(top, skip, search, 'Created desc');
			RestService.Private.Students.get(query).then(function (result) {
				$scope.students = result.values;
				_.forEach($scope.students, function (student) {
					student.fullName = student.name + ' ' + student.lastName;
				});

				$scope.studentPaging.totalRecords = result.count;

				if (!$scope.studentsSearch) {
					$scope.studentsSearch = {
					};
				}

				$scope.studentsSearch.studentRecordsFound = result.count !== 0;
				if ($scope.students.length > 0) {
					if (id) {
						$scope.setSelected(id);
					} else {
						$scope.setSelected($scope.students[0].id);
					}
				} else {
					$scope.studentSelected = false;
				}

				$scope.basicSearch.spin = false;
			});
		};

		if (!$scope.basicSearch.spin) {
			$scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging));
		}

		$scope.setSelected = function (studentId) {
			$scope.loadFullStudent(studentId);
			$scope.reloadTransactions(studentId);
		};

		$scope.addToBalance = function (value) {
			console.log('Add new balance ' + value);
			//$scope.selectedStudent.balance += parseInt(value);
		};

		$scope.studentsPageChanged = function (newPage, searchValue) {
			if (!$scope.basicSearch.spin) {
				$scope.studentPaging.page = newPage;
				$scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), searchValue);
			}
		};

		$scope.setRecordsPerPage = function (newRecordsPerPageValue) {
			$scope.studentPaging.perPage = newRecordsPerPageValue;
		};

		var previousSearch = '';

		$scope.executeSearch = function (searchValue) {
			if (searchValue && searchValue.length > 2) {
				$timeout(function () {
					$scope.studentPaging.page = 1;
					$scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), searchValue);
					previousSearch = searchValue;
				}, 300);
			} else if (_.isEmpty(searchValue) && previousSearch !== searchValue) {
				$timeout(function () {
					$scope.studentPaging.page = 1;
					$scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), null);
					previousSearch = searchValue;
				}, 300);
			}
		};

	}
);

