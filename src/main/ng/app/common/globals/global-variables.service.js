'use strict';

angular.module('edup.common')

	.service('GlobalVariables', function (RestService) {

		var values = {};
		RestService.Private.Balance.one('types').get().then(function (response) {
			_.forEach(response.values, function (value) {
				values[value.id] = value;
			});
		});

		return {
			Icons: function (key) {
				if (values[key]) {
					return values[key].icon;
				}
			}
		};

	}
);