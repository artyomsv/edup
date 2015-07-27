'use strict';

angular.module('edup')

    .config(function ($stateProvider) {

        $stateProvider
            .state('students', {
                templateUrl: 'students',
                url: '/students',
                controller: 'StudentsController'
            })
            .state('subjects', {
                templateUrl: 'subjects',
                url: '/subjects',
                controller: 'SubjectsController'
            })
            .state('calendar', {
                templateUrl: 'calendar',
                url: '/calendar',
                controller: 'CalendarController'
            }
        );

    }
);