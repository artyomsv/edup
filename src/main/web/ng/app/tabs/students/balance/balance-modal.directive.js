'use strict';

angular.module('edup.students')

    .directive('balanceModal', function (RestService, NotificationService) {
        return {
            restrict: 'E',
            templateUrl: 'balance-modal',
            link: function (scope) {
                scope.price = {
                    amount: 0
                };

                scope.saving = function (balance) {
                    if (balance && balance.amount) {
                        var balanceDto = {
                            studentId: scope.selectedStudent.id,
                            amount: balance.amount * 100,
                            comments: balance.comment
                        };

                        RestService.Private.Balance.customPOST(balanceDto).then(function (response) {
                            var recordId = response.payload;
                            if (recordId) {
                                scope.selectedStudent.balance += balance.amount;
                                scope.balance = null;
                                scope.dismissModal();
                                NotificationService.Success(balance.amount + ' EUR was added to ' + scope.selectedStudent.name + ' ' + scope.selectedStudent.lastName + ' student!');
                            }
                        });
                    }
                };

                scope.resetValue = function () {
                    scope.balance = null;
                };
            }
        };
    }
);