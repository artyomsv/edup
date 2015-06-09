'use strict';

angular.module('edup.common')

    .service('NotificationService', function () {

        alertify.set('notifier', 'position', 'bottom-right');

        return {
            Success: function (msg, title) {
                alertify.success(msg);
            },
            Info: function (msg, title) {
                alertify.info(msg);
            },
            Warn: function (msg, title) {
                alertify.notify(msg, 'custom', 2);
            },
            Error: function (msg, title) {
                Alertify.error(msg);
            }


        };

    }
);