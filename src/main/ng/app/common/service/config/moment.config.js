'use strict';

angular.module('edup.common')

	.run(function (amMoment, moment) {

		amMoment.changeLocale('en');

		moment.locale('en', {
			week : {
				dow : 1 // Monday is the first day of the week
			}
		});

	});
