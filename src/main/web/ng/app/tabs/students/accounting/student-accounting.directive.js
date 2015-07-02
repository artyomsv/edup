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

                        RestService.Private.Balance.customPOST(balanceDto).then(function (response) {
                            var recordId = response.payload;
                            if (recordId) {
                                student.balance += balance.amount;
                                $scope.balance = null;
                                NotificationService.Success(balance.amount + ' EUR was added to ' + student.name + ' ' + student.lastName + ' student!');
                            }
                        });
                    }
                };
            },

            link: function (scope) {
            }
        };
    }
);