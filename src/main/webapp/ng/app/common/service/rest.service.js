'use strict';

angular.module('edup.common')

    .service('RestService', function (Restangular) {

        var rpc = Restangular.one('secured');

        return {
            Students: rpc.one('students')
        };

    }
);