'use strict';

angular.module('edup.students')

	.directive('newStudent', function () {
		return {
			restrict: 'E',
			templateUrl: 'new-student-modal',

			controller: function ($scope, RestService, PaginationService, NotificationService) {
				var dismiss = function () {
					var view = $('#addNewStudentModalView');
					if (view) {
						view.modal('hide');
					}
				};

				$scope.resetNewStudent = function () {
					$scope.$broadcast('clearFileUploadQueue', {message: 'reset'});

					if ($scope.newStudent) {
						$scope.newStudent.birthDate = null;
						$scope.newStudent.photoUrl = null;
						$scope.newStudent.photoId = null;
					}
					$scope.newStudent = null;
					$scope.photoUrl = null;
					$scope.photoUploaded = false;
				};

				$scope.executeStudentSave = function (student) {
					if ($scope.studentProcessingInProgress) {
						return;
					}

					if (student && student.name && student.lastName) {
						$scope.studentProcessingInProgress = true;
						student.birthDate = new Date(student.birthDateString);
						RestService.Private.Students.customPOST(student).then(function (response) {
							NotificationService.Success('Student ' + student.name + ' ' + student.lastName + ' created!');
							$scope.loadStudents(response.payload, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), null);
							$scope.resetNewStudent();
							dismiss();
							$scope.studentProcessingInProgress = false;
						}, function () {
							$scope.studentProcessingInProgress = false;
						});
					}
				};

				$scope.executeReset = function () {
					$scope.resetNewStudent();
				};

				$scope.executeCancel = function () {
					$scope.resetNewStudent();
					dismiss();
				};

			},

			link: function (scope) {
				scope.dismissNewStudentModalDialog = function () {
					scope.dismissModal();
				};
			}
		};
	}
);