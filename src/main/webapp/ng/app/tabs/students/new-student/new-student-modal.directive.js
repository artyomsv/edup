'use strict';

angular.module('edup.students')

    .directive('newStudentRecord', function () {
        return {
            restrict: 'E',
            scope: {
                loadStudents: '&',
                paging: '='
            },
            templateUrl: 'new-student-modal',

            controller: function ($scope, RestService, PaginationService, NotificationService) {
                var reset = function () {
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
                    console.log(angular.toJson(student, true));
                    if (student && student.name && student.lastName) {
                        student.photoId = $scope.id;
                        RestService.Students.customPOST(student).then(function (response) {
                            NotificationService.Success('Student ' + student.name + ' ' + student.lastName + ' created!');
                            $scope.dismissModal();
                            $scope.loadStudents(response.payload, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), null);
                            reset();
                        });
                    }
                };

                $scope.executeReset = function () {
                    reset();
                };

                $scope.executeCancel = function () {
                    reset();
                    $scope.dismissModal();
                };

            },

            link: function (scope) {


            }
        };
    }
);