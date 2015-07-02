'use strict';

angular.module('edup.common')

    .service('PaginationService', function () {

        var getTop = function (paging) {
            if (paging.page) {
                return paging.page * paging.perPage;
            } else {
                return 1 * paging.perPage;
            }

        };

        var getSkip = function (paging) {
            if (paging.page) {
                return (paging.page * paging.perPage) - paging.perPage;
            } else {
                return (1 * paging.perPage) - paging.perPage;
            }
        };

        return {
            Top: getTop,
            Skip: getSkip
        };

    }
);