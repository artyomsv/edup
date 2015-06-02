'use strict';

angular.module('edup.students')

    .controller('NewStudentModalController', function ($scope, RestService) {
        $scope.executeStudentSave = function (student) {
            if (student && student.name && student.lastName) {
                RestService.Students.customPOST(student).then(function (response) {
                    $scope.dismissModal();
                    $scope.newStudent = null;
                    //$scope.students.push(student);
                    $scope.loadFullStudent(response.payload);
                });
            }
        };

        $scope.executeCancel = function () {
            $scope.newStudent = null;
        };

    }
);

