'use strict';

angular.module('edup.students')

	.directive('editStudent', function () {
		return {
			restrict: 'E',
			templateUrl: 'edit-student',
			controller: function ($scope, RestService, NotificationService, PaginationService) {

				$scope.executeStudentUpdate = function (student) {
					if ($scope.studentProcessingInProgress) {
						return;
					}

					$scope.studentProcessingInProgress = true;

					student.id = $scope.selectedStudent.id;
					student.versionId = $scope.selectedStudent.versionId;
					student.birthDate = new Date(student.birthDateString);
					console.log(angular.toJson(student));
					if (student && student.name && student.lastName && student.id && student.versionId) {
						RestService.Private.Students.one(student.id.toString())
							.customPUT(student)
							.then(function (response) {
								$scope.newStudent = null;
								$scope.photoUrl = null;
								$scope.photoUploaded = false;
								student.versionId = response.payload;
								NotificationService.Success(student.name + ' ' + student.lastName + ' updated!');
								$scope.loadStudents(student.id, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), null);
								$scope.studentProcessingInProgress = false;
							}, function () {
								$scope.studentProcessingInProgress = false;
							});
					}
				};

			},
			link: function ($scope) {

			}
		};
	}
);