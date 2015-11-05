'use strict';

angular.module('edup.common')

	.service('UrlService', function (PREFIX, PORT) {

		var location = window.location.hostname;

		var baseUrl;

		if (location.indexOf('127.0.0.1') > -1) {
			baseUrl = 'https://localhost:8443/edup';
		} else {
			baseUrl = PREFIX + '://' + location + ':' + PORT + '/edup';
		}

		return {
			BaseUrl: baseUrl,
			Files: {
				Info: baseUrl + '/api/private/files',
				Upload: baseUrl + '/api/private/files',
				Download: baseUrl + '/api/private/files'
			},
			Subjects: baseUrl + '/api/private/subjects',
			Reports: {
				Events: baseUrl + '/api/private/reports/subject'
			}
		};

	}
);