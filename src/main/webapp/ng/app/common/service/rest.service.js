'use strict';

angular.module('edup.common')

    .service('RestService', function (Restangular) {

        var rpc = Restangular.one('private');

        return {
            Students: rpc.one('students'),
            Balance: rpc.one('balance')
        };

    }
);