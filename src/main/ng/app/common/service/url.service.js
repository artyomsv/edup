'use strict';

angular.module('edup.common')

    .service('UrlService', function (PREFIX, PORT, CONTEXT_ROOT) {

            var location = window.location.hostname;

            var baseUrl;

            if (location.indexOf('127.0.0.1') > -1) {
                baseUrl = PREFIX + '://localhost:' + PORT + CONTEXT_ROOT;
            } else {
                var portValue = PORT ? ':' + PORT : '';
                baseUrl = PREFIX + '://' + location + portValue + CONTEXT_ROOT;
            }

            return {
                BaseUrl: baseUrl,
                Files: {
                    Info: baseUrl + '/api/private/files',
                    Upload: baseUrl + '/api/private/files',
                    Download: baseUrl + '/api/private/files'
                },
                Subjects: baseUrl + '/api/private/subjects',
                Reports: {
                    Events: baseUrl + '/api/private/reports/subject'
                }
            };

        }
    );