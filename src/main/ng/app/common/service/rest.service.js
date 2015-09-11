'use strict';

angular.module('edup.common')

	.service('RestService', function (Restangular) {

		var authenticationResource = Restangular.one('authentication');
		var privateResource = Restangular.one('private');
		var publicResource = Restangular.one('public');

		return {
			Private: {
				Students: privateResource.one('students'),
				Balance: privateResource.one('balance'),
				Documents: privateResource.one('documents'),
				Subjects: privateResource.one('subjects')
			},
			Public: {
				Ping: publicResource.one('ping')
			},
			Authentication: {
				LogOut: authenticationResource.one('logout'),
				Login: authenticationResource.one('login')
			}

		};

	}
);