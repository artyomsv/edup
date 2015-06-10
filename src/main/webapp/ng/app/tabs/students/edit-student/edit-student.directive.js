'use strict';

angular.module('edup.students')

    .directive('editStudent', function () {
        return {
            restrict: 'E',
            templateUrl: 'edit-student',
            controller: function($scope, RestService, NotificationService, PaginationService) {

                $scope.executeStudentUpdate = function (student) {
                    student.id = $scope.selectedStudent.id;
                    student.versionId = $scope.selectedStudent.versionId;
                    student.birthDate = new Date(student.birthDateString);
                    if (student && student.name && student.lastName && student.id && student.versionId) {
                        RestService.Students.one(student.id.toString())
                            .customPUT(student)
                            .then(function (response) {
                                $scope.newStudent = null;
                                $scope.photoUrl = null;
                                $scope.photoUploaded = false;
                                student.versionId = response.payload;
                                NotificationService.Success(student.name + ' ' + student.lastName + ' updated!');
                                $scope.loadStudents(student.id, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), null);
                            });
                    }
                };

            },
            link : function ($scope) {

            }
        };
    }
);