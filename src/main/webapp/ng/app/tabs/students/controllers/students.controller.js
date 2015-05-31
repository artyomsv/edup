'use strict';


angular.module('edup.students')

    .controller('StudentsController', function ($scope, $timeout, RestService, PaginationService) {

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

        var prepareQuery = function (top, skip, search) {
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
            return queries;
        };

        var loadStudents = function (id, top, skip, search) {
            $scope.basicSearch.spin = true;
            var query = prepareQuery(top, skip, search);
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

        $scope.loadFullStudent = function (id) {
            if (id) {
                RestService.Students.one(id.toString()).get().then(function (response) {
                    $scope.selectedStudent = response.payload;
                    $scope.studentSelected = true;
                });
            }
        };

        loadStudents(null, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging));

        $scope.setSelected = function (studentId) {
            $scope.loadFullStudent(studentId);
        };

        $scope.addToBalance = function (value) {
            $scope.selectedStudent.balance += parseInt(value);
        };

        $scope.pageChanged = function (newPage, searchValue) {
            $scope.paging.page = newPage;
            loadStudents(null, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), searchValue);
            console.log($scope.paging);
        };

        $scope.setRecordsPerPage = function (newRecordsPerPageValue) {
            $scope.paging.perPage = newRecordsPerPageValue;
        };

        $scope.executeSearch = function (searchValue) {
            console.log(searchValue);
            if (searchValue && searchValue.length > 2) {
                $timeout(function () {
                    $scope.paging.page = 1;
                    loadStudents(null, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), searchValue);
                }, 300);
            } else if (searchValue.length === 0) {
                $timeout(function () {
                    $scope.paging.page = 1;
                    loadStudents(null, PaginationService.Top($scope.paging), PaginationService.Skip($scope.paging), null);
                }, 300);
            }
        };

    }
)
;

