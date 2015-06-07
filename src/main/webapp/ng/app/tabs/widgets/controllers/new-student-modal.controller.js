'use strict';

angular.module('edup.students')

    .controller('NewStudentModalController', function ($scope, RestService, PaginationService) {
        $scope.executeStudentSave = function (student) {
            if (student && student.name && student.lastName) {
                RestService.Students.customPOST(student).then(function (response) {
                    $scope.dismissModal();
                    $scope.newStudent = null;
                    $scope.photoUrl = null;
                    $scope.photoUploaded = false;
                    $scope.loadStudents(response.payload, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), null);
                });
            }
        };

        $scope.executeCancel = function () {
            $scope.newStudent = null;
        };

    }
);

