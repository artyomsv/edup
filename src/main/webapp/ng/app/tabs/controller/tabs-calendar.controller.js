'use strict';

angular.module('edup.tabs')

    .controller('CalendarTabController', function ($scope, $modal, moment, calendarTitle) {

        var update = function () {
            $scope.currentDay = calendarTitle.month();
        };

        update();

        //These variables MUST be set as a minimum for the calendar to work
        $scope.calendarView = 'month';
        $scope.calendarDay = new Date();
        $scope.events = [
            {
                title: 'My event title', // The title of the event
                type: 'info', // The type of the event (determines its color). Can be important, warning, info, inverse, success or special
                startsAt: moment().subtract(2, 'day').toDate(), // A javascript date object for when the event starts
                endsAt: moment().add(2, 'days').toDate(), // A javascript date object for when the event ends
                editable: false, // If edit-event-html is set and this field is explicitly set to false then dont make it editable
                deletable: false, // If delete-event-html is set and this field is explicitly set to false then dont make it deleteable
                incrementsBadgeTotal: true //If set to false then will not count towards the badge total amount on the month and year view
            },
            {
                title: 'This some strange warning event',
                type: 'warning',
                startsAt: moment().startOf('week').subtract(2, 'days').add(8, 'hours').toDate(),
                endsAt: moment().startOf('week').add(1, 'week').add(9, 'hours').toDate()
            },
            {
                title: 'Information event',
                type: 'info',
                startsAt: moment().subtract(1, 'day').toDate(),
                endsAt: moment().add(5, 'days').toDate()
            },
            {
                title: 'This is a really long and importnat event title',
                type: 'important',
                startsAt: moment().startOf('day').add(5, 'hours').toDate(),
                endsAt: moment().startOf('day').add(19, 'hours').toDate()
            }
        ];

        function showModal(action, event) {
            $modal.open({
                templateUrl: 'modalContent.html',
                controller: function ($scope, $modalInstance) {
                    $scope.$modalInstance = $modalInstance;
                    $scope.action = action;
                    $scope.event = event;
                }
            });
        }

        $scope.eventClicked = function (event) {
            $scope.currentDay = this.calendarTitle;
            showModal('Clicked', event);
        };

        $scope.eventEdited = function (event) {
            $scope.currentDay = this.calendarTitle;
            showModal('Edited', event);
        };

        $scope.eventDeleted = function (event) {
            $scope.currentDay = this.calendarTitle;
            showModal('Deleted', event);
        };

        $scope.toggle = function ($event, field, event) {
            $event.preventDefault();
            $event.stopPropagation();
            event[field] = !event[field];
        };

        $scope.setCalendarView = function (view) {
            $scope.calendarView = view;
            $scope.currentDay = this.calendarTitle;
        };

        $scope.updateTitle = function () {
            $scope.currentDay = this.calendarTitle;
        };

    }
);

