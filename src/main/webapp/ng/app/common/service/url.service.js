'use strict';

angular.module('edup.common')

    .service('UrlService', function () {

        var location = window.location.hostname;

        var baseUrl;

        if (location.indexOf('127.0.0.1') > -1) {
            baseUrl = 'https://localhost:8443/edup/api';
        } else {
            baseUrl = 'https://' + location + ':8443/edup/api';
        }

        return {
            BaseUrl: baseUrl,
            Files: {
                Info: baseUrl + '/secured/files',
                Upload: baseUrl + '/secured/files/upload',
                Download: baseUrl + '/secured/files/download'
            }
        };

    }
);