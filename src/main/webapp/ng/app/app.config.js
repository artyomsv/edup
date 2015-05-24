'use strict';

angular.module('edup')

    .config(function ($stateProvider) {

        $stateProvider
            .state('students', {
                templateUrl: 'tabs-students',
                url: '/students',
                controller: 'StudentsTabController'
            })
            .state('calendar', {
                templateUrl: 'tabs-calendar',
                url: '/calendar',
                controller: 'CalendarTabController'
            }
        );

    }
);