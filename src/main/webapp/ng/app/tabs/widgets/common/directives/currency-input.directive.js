'use strict';

angular.module('edup.widgets')

    .directive('currencyInput', function (RestService) {
        return {
            restrict: 'E',
            templateUrl: 'currency-input',
            scope: {
                selectedStudent: '='
            },
            link: function (scope) {
                scope.price = {
                    amount: 0
                };

                scope.saving = function (balance) {
                    var balanceDto = {
                        studentId: scope.selectedStudent.id,
                        amount: balance.amount * 100,
                        comments: balance.comment
                    };

                    RestService.Balance.customPOST(balanceDto).then(function (response) {
                        var recordId = response.payload;
                        if (recordId) {
                            scope.selectedStudent.balance += balance.amount;
                            scope.balance = null;
                            scope.dismissModal();
                        }
                    });
                };

                scope.resetValue = function () {
                    scope.balance = null;
                }
            }
        };
    }
);