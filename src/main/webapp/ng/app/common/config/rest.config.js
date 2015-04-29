'use strict';

angular.module('edup.common')

    .config(function (RestangularProvider) {

        var location = window.location.host;

        var baseUrl;

        if (location.indexOf('127.0.0.1') > -1) {
            baseUrl = 'https://localhost:8443/edup/api';
        } else {
            baseUrl = 'https://' + location + '/edup/api';
        }

        console.log(baseUrl);

        RestangularProvider.setBaseUrl(baseUrl);

        RestangularProvider.setErrorInterceptor(function (resp) {
            console.log(angular.toJson(resp, true));
            return false;
        });

    }
);