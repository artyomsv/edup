'use strict';


angular.module('edup.students')

    .controller('StudentsController', function ($scope, $timeout, RestService, PaginationService, NotificationService) {

        $scope.studentSelected = false;
        $scope.basicSearch = {
            spin: false
        };
        $scope.paging = {
            enabled: false,
            page: 1,
            perPage: 10,
            totalRecords: 0
        };

        var prepareQuery = function (top, skip, search, orderBy) {
            var queries = {};

            queries.$count = true;

            if (top) {
                queries.$top = top;
            }
            if (top) {
                queries.$skip = skip;
            }
            if (top) {
                queries.$search = search;
            }
            if (orderBy) {
                queries.$orderby = orderBy;
            }
            return queries;
        };

        $scope.loadFullStudent = function (id) {
            if (id) {
                $scope.basicSearch.spin = true;
                RestService.Students.one(id.toString()).get().then(function (response) {
                    $scope.selectedStudent = response.payload;
                    $scope.selectedStudent.balance = (response.payload.balance / 100);
                    $scope.studentSelected = true;
                    $scope.basicSearch.spin = false;
                });
            }
        };

        $scope.loadStudents = function (id, top, skip, search) {
            $scope.basicSearch.spin = true;
            var query = prepareQuery(top, skip, search, 'Created desc');
            RestService.Students.get(query).then(function (result) {
                $scope.students = result.values;
                $scope.paging.totalRecords = result.count;
                if ($scope.students.length > 0) {
                    if (id) {
                        $scope.loadFullStudent(id);
                    } else {
                        $scope.loadFullStudent($scope.students[0].id);
                    }
                } else {
                    $scope.studentSelected = false;
                }

                $scope.basicSearch.spin = false;
            });
        };

        if (!$scope.basicSearch.spin) {
            $scope.loadStudents(null, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging));
        }

        $scope.setSelected = function (studentId) {
            $scope.loadFullStudent(studentId);
        };

        $scope.addToBalance = function (value) {
            console.log('Add new balance ' + value);
            //$scope.selectedStudent.balance += parseInt(value);
        };

        $scope.pageChanged = function (newPage, searchValue) {
            $scope.paging.page = newPage;
            $scope.loadStudents(null, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), searchValue);
        };

        $scope.setRecordsPerPage = function (newRecordsPerPageValue) {
            $scope.paging.perPage = newRecordsPerPageValue;
        };

        function isEmpty(str) {
            return (!str || 0 === str.length);
        }

        var previousSearch = '';

        $scope.executeSearch = function (searchValue) {
            if (searchValue && searchValue.length > 2) {
                $timeout(function () {
                    $scope.paging.page = 1;
                    $scope.loadStudents(null, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), searchValue);
                    previousSearch = searchValue;
                }, 300);
            } else if (isEmpty(searchValue) && previousSearch !== searchValue) {
                $timeout(function () {
                    $scope.paging.page = 1;
                    $scope.loadStudents(null, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), null);
                    previousSearch = searchValue;
                }, 300);
            }
        };

        $scope.executeStudentUpdate = function (student) {
            student.id = $scope.selectedStudent.id;
            student.versionId = $scope.selectedStudent.versionId;
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

    }
)
;

