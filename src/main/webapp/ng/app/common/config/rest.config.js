'use strict';

angular.module('edup.common')

    .config(function (RestangularProvider) {

        var baseUrl = window.location.host + '/edup/api';

        if (baseUrl.indexOf('http://127.0.0.1:8088/') > -1) {
            RestangularProvider.setBaseUrl('https://192.168.1.104:8443/edup/api');
        } else {
            RestangularProvider.setBaseUrl(baseUrl);
        }

        RestangularProvider.setErrorInterceptor(function (resp) {
            console.log(angular.toJson(resp, true));
            return false;
        });

    }
);