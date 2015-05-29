'use strict';

angular.module('edup.students')

    .controller('StudentsController', function ($scope, RestService) {

        $scope.studentSelected = false;

        $scope.loadStudents = function (id) {
            RestService.Students.get().then(function (result) {
                $scope.students = result.values;
                if ($scope.students.length > 0) {
                    if (id) {
                        $scope.loadFullStudent(id);
                    } else {
                        $scope.loadFullStudent($scope.students[0].id);
                    }
                } else {
                    $scope.studentSelected = false;
                }
            });
        };

        $scope.loadFullStudent = function (id) {
            if (id) {
                RestService.Students.one(id.toString()).get().then(function (response) {
                    $scope.selectedStudent = response.payload;
                    $scope.studentSelected = true;
                });
            }
        };

        $scope.loadStudents();

        $scope.setSelected = function (studentId) {
            $scope.loadFullStudent(studentId);
        };

        $scope.addToBalance = function (value) {
            $scope.selectedStudent.balance = $scope.selectedStudent.balance + parseInt(value);
        };

    }
);

