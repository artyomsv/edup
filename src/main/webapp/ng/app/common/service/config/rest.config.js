'use strict';

angular.module('edup.common').run(function (Restangular, UrlService, NotificationService) {

    Restangular.setBaseUrl(UrlService.BaseUrl);

    Restangular.setErrorInterceptor(function (resp) {
        var msg = 'Failed on: ' + resp.config.method + ' to: ' + resp.config.url;
        NotificationService.Error(msg);
        console.log(angular.toJson(resp, true));
        return false;
    });

    Restangular.setRestangularFields({
        etag: 'versionId'
    });

    //Restangular.setDefaultHeaders({
    //    'Content-Type': 'application/json',
    //    'Accept': 'application/json',
    //    'X-Requested-With': 'XMLHttpRequest'
    //});

});