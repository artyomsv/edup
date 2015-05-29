'use strict';

angular.module('edup.widgets')

    .controller('NewStudentController', function ($scope, RestService) {
        $scope.executeStudentSave = function (student) {
            if (student && student.name && student.lastName) {
                console.log(angular.toJson(student, true));

                RestService.Students.customPOST(student).then(function (response) {
                    $scope.dismiss();
                    $scope.newStudent = null;
                    //$scope.students.push(student);
                    $scope.loadStudents();
                });
            }
        };

        $scope.executeCancel = function () {
            $scope.newStudent = null;
        };

    }
);

