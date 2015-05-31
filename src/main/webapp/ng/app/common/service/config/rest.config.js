'use strict';

angular.module('edup.common').run(function (Restangular, UrlService) {

    Restangular.setBaseUrl(UrlService.BaseUrl);

    Restangular.setErrorInterceptor(function (resp) {
        console.log(angular.toJson(resp, true));
        return false;
    });
});