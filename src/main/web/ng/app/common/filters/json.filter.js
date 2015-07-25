'use strict';

angular.module('edup.common')

	.filter('toJson', function () {
		return function (input, pretty) {
			return angular.toJson(input, pretty);
		};
	}
);