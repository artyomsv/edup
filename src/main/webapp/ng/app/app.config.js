'use strict';

angular.module('edup')

    .config(function ($stateProvider) {

        $stateProvider
            .state('clients', {
                templateUrl: 'tabs-client',
                url: '/clients',
                controller: 'ClientTabController'
            })
            .state('calendar', {
                templateUrl: 'tabs-calendar',
                url: '/calendar',
                controller: 'CalendarTabController'
            }
        );

    }
);