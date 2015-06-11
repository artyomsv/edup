'use strict';


angular.module('edup.students')

    .controller('StudentsController', function ($scope, $timeout, $filter, RestService, PaginationService) {

        $scope.studentSelected = false;
        $scope.basicSearch = {
            spin: false
        };
        $scope.studentPaging = {
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
                    if ($scope.selectedStudent) {
                        $scope.studentEdit = _.cloneDeep($scope.selectedStudent);
                        if ($scope.studentEdit.birthDate) {
                            $scope.studentEdit.birthDateString = $filter('date')(new Date($scope.studentEdit.birthDate), 'yyyy-MM-dd')
                        }

                        $scope.selectedStudent.balance = (response.payload.balance / 100);

                        $scope.studentSelected = true;
                        $scope.basicSearch.spin = false;
                    }
                });
            }
        };

        $scope.loadStudents = function (id, top, skip, search) {
            $scope.basicSearch.spin = true;
            var query = prepareQuery(top, skip, search, 'Created desc');
            RestService.Students.get(query).then(function (result) {
                $scope.students = result.values;
                $scope.studentPaging.totalRecords = result.count;
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
            $scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging));
        }

        $scope.setSelected = function (studentId) {
            $scope.loadFullStudent(studentId);
        };

        $scope.addToBalance = function (value) {
            console.log('Add new balance ' + value);
            //$scope.selectedStudent.balance += parseInt(value);
        };

        $scope.pageChanged = function (newPage, searchValue) {
            $scope.studentPaging.page = newPage;
            $scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), searchValue);
        };

        $scope.setRecordsPerPage = function (newRecordsPerPageValue) {
            $scope.studentPaging.perPage = newRecordsPerPageValue;
        };

        function isEmpty(str) {
            return (!str || 0 === str.length);
        }

        var previousSearch = '';

        $scope.executeSearch = function (searchValue) {
            if (searchValue && searchValue.length > 2) {
                $timeout(function () {
                    $scope.studentPaging.page = 1;
                    $scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), searchValue);
                    previousSearch = searchValue;
                }, 300);
            } else if (isEmpty(searchValue) && previousSearch !== searchValue) {
                $timeout(function () {
                    $scope.studentPaging.page = 1;
                    $scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), null);
                    previousSearch = searchValue;
                }, 300);
            }
        };

    }
);

