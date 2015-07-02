'use strict';

angular.module('edup.common')

    .service('RestService', function (Restangular) {

        var privateResource = Restangular.one('private');
        var publicResource = Restangular.one('public');

        return {
            Private: {
                Students: privateResource.one('students'),
                Balance: privateResource.one('balance'),
                Documents: privateResource.one('documents'),
                LogOut: privateResource.one('logout')
            },
            Public: {
                Login: publicResource.one('login')
            }

        };

    }
);