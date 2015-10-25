'use strict';

angular.module('edup.students')

	.directive('balanceModal', function (RestService, NotificationService) {
		return {
			restrict: 'E',
			templateUrl: 'balance-modal',
			link: function (scope) {

				scope.balanceUpdateInProgress = false;

				scope.price = {
					amount: 0,
					cash: true
				};

				scope.saving = function (balance) {
					if (scope.balanceUpdateInProgress) {
						return;
					}

					if (balance && balance.amount) {
						scope.balanceUpdateInProgress = true;
						var balanceDto = {
							studentId: scope.selectedStudent.id,
							amount: balance.amount * 100,
							comments: balance.comment,
							cash: balance.cash ? balance.cash : true
						};

						RestService.Private.Balance.customPOST(balanceDto)
							.then(function (response) {
								var recordId = response.payload;
								if (recordId) {
									scope.selectedStudent.balance += balance.amount;
									scope.balance = null;
									scope.dismissModal();
									NotificationService.Success(balance.amount + ' EUR was added to ' + scope.selectedStudent.name + ' ' + scope.selectedStudent.lastName + ' student!');
									scope.balanceUpdateInProgress = false;
									scope.reloadTransactions(scope.selectedStudent.id);
								}
							}, function (error) {
								scope.balanceUpdateInProgress = false;
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