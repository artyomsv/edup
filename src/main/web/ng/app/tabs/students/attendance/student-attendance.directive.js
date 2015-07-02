'use strict';

angular.module('edup.students')

    .directive('studentAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-attendance',

            controller: function ($scope) {
                $scope.attendanceHistory = [
                    {
                        'subject' : 'Math',
                        'date' : '2015/03/03',
                        'amount' : 15
                    },
                    {
                        'subject' : 'Literature',
                        'date' : '2015/03/03',
                        'amount' : 15
                    },
                    {
                        'subject' : 'Sport',
                        'date' : '2015/03/04',
                        'amount' : 20
                    },
                    {
                        'subject' : 'History',
                        'date' : '2015/03/04',
                        'amount' : 10
                    },
                    {
                        'subject' : 'Math',
                        'date' : '2015/03/05',
                        'amount' : 15
                    },
                    {
                        'subject' : 'English',
                        'date' : '2015/03/07',
                        'amount' : 15
                    },
                    {
                        'subject' : 'Sport',
                        'date' : '2015/03/07',
                        'amount' : 20
                    }
                ];
            },

            link : function ($scope) {
               
            }
        };
    }
);