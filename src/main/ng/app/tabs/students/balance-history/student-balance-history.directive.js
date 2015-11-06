'use strict';

angular.module('edup.students')

	.directive('studentBalanceHistory', function () {
		return {
			restrict: 'E',
			templateUrl: 'student-balance-history',

			controller: function ($scope, RestService, QueryService, GlobalVariables) {

				$scope.balanceHistory = {
					count: 0,
					values: {},
					spin: false,
					show: true
				};

				$scope.reloadTransactions = function (studentId) {
					var query = QueryService.Query(5, 0, null, 'Created desc', 'StudentId eq ' + studentId, true);
					RestService.Private.Balance.get(query).then(function (response) {
						$scope.balanceHistory.count = response.count;
						if ($scope.balanceHistory.count > 0) {
							$scope.balanceHistory.values = {};
							$scope.balanceHistory.show = true;
							_.forEach(response.values, function (value, index) {
								$scope.balanceHistory.values[index] = {
									date: value.created,
									amount: value.amount / 100,
									description: value.comments,
									icon: GlobalVariables.Icons(value.type)
								};
							});
						} else {
							$scope.balanceHistory.show = false;
							$scope.balanceHistory.values = {};
						}
					});
				};

			},

			link: function (scope) {
			}
		};
	}
);