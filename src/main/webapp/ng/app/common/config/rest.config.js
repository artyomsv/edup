'use strict';

angular.module('edup.common')

    .config(function (RestangularProvider) {

        RestangularProvider.setBaseUrl('https://localhost:8443/edup/api');
        //RestangularProvider.setBaseUrl('https://192.168.1.104:8443/edup/api');

        RestangularProvider.setErrorInterceptor(function (resp) {
            console.log(angular.toJson(resp, true));
            return false;
        });

    }
);