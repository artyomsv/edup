'use strict';

angular.module('edup.common')

    .service('NotificationService', function () {

        /* jshint ignore:start */

        alertify.set('notifier', 'position', 'bottom-right');

        return {
            Success: function (msg, title) {
                alertify.success(msg);
            },
            Info: function (msg, title) {
                alertify.message(msg);
            },
            Error: function (msg, title) {
                alertify.error(msg);
            }

        };

        /* jshint ignore:end */

    }
);