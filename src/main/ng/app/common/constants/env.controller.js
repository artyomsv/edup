'use strict';

angular.module('edup.common')

	.controller('ConstantsController', function ($scope, CONTEXT_ROOT) {
		$scope.contextRoot = CONTEXT_ROOT;
	});