'use strict';

angular.module('edup.common')

	.service('UrlService', function () {

		var location = window.location.hostname;

		var baseUrl;

		if (location.indexOf('127.0.0.1') > -1) {
			baseUrl = 'https://localhost:8443/edup';
		} else {
			baseUrl = 'https://' + location + ':8443/edup';
		}

		return {
			BaseUrl: baseUrl,
			Files: {
				Info: baseUrl + '/api/private/files',
				Upload: baseUrl + '/api/private/files/upload',
				Download: baseUrl + '/api/private/files/download'
			},
			Subjects: baseUrl + '/api/private/subjects'
		};

	}
);