'use strict';

angular.module('edup.common')

	.config(function (paginationTemplateProvider, PREFIX, PORT, CONTEXT_ROOT) {

		var location = window.location.hostname;

		var baseUrl;

		if (location.indexOf('127.0.0.1') > -1) {
			baseUrl = 'http://127.0.0.1:8088/';
		} else {
			var portValue = PORT ? ':' + PORT : '';
			baseUrl = PREFIX + '://' + location + portValue + CONTEXT_ROOT + '/ng';
		}

		paginationTemplateProvider.setPath(baseUrl + '/vendor/bower_components/angular-utils-pagination/dirPagination.tpl.html');
	}
);