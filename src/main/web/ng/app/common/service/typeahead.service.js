'use strict';

angular.module('edup.common')

	.service('TypeAheadService', function (UrlService) {

		var url = UrlService.Subjects;
		var dataSet = {};

		var filterResponse = function (response) {
			dataSet = response.values;
			var pluck = _.pluck(response.values, 'subjectName');
			return pluck;
		};

		/* jshint ignore:start */
		var typeAhead = function () {
			var bloodhound = new Bloodhound({
				datumTokenizer: function (datum) {
					return Bloodhound.tokenizers.whitespace(datum.value);
				},
				queryTokenizer: Bloodhound.tokenizers.whitespace,
				limit: 10,
				remote: {
					url: url + '?$count=true&$orderby=Created+desc&$search=%QUERY&$skip=0&$top=999&count=true',
					filter: filterResponse,
					wildcard: '%QUERY'
				}
			});
			bloodhound.initialize();
			return bloodhound;
		};

		return {
			Build: typeAhead,
			DataSet: function () {
				return dataSet;
			}
		};
		/* jshint ignore:end */
	}
);