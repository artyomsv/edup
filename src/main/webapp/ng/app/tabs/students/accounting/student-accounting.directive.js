'use strict';

angular.module('edup.students')

    .directive('studentAccounting', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-accounting',

            controller: function ($scope, RestService, NotificationService) {

                $scope.updateStudentBalance = function(balance, student) {
                    if (balance && balance.amount) {
                        var balanceDto = {
                            studentId: student.id,
                            amount: balance.amount * 100,
                            comments: balance.comment
                        };

                        RestService.Balance.customPOST(balanceDto).then(function (response) {
                            var recordId = response.payload;
                            if (recordId) {
                                student.balance += balance.amount;
                                $scope.balance = null;
                                NotificationService.Success(balance.amount + ' EUR was added to ' + student.name + ' ' + student.lastName + ' student!');
                            }
                        });
                    }
                };

                $scope.deactivateBalance = function(balance) {
                    NotificationService.Success(balance.id + ' for deactivation');
                };

                $scope.balanceList = [
                    {
                        id: 1,
                        amount: "10.00",
                        comment: "heya",
                        created: new Date()

                    },
                    {
                        id: 2,
                        amount: "15.00",
                        comment: "heya2",
                        created: new Date()

                    },
                    {
                        id: 3,
                        amount: "12.00",
                        comment: "heya3",
                        created: new Date()

                    }
                ]

            },

            link: function (scope) {


            }
        };
    }
);