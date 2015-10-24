'use strict';

angular.module('edup.common')

	.run(function (Restangular, UrlService, NotificationService) {

		Restangular.setBaseUrl(UrlService.BaseUrl + '/api');

		Restangular.setErrorInterceptor(function (resp) {
			var msg = '';
			if (resp.status === 400) {
				_.forEach(resp.data.errors, function (error) {
					msg += error.message + ',\n\n';
				});
			} else {
				msg = 'Failed on: ' + resp.config.method + ' to: ' + resp.config.url;
			}
			NotificationService.Error(msg);
			return true;
		});

		Restangular.setRestangularFields({
			etag: 'versionId'
		});

		Restangular.setDefaultHeaders({
			'Content-Type': 'application/json',
			'Accept': 'application/json',
			'X-Requested-With': 'XMLHttpRequest'
		});

	});
