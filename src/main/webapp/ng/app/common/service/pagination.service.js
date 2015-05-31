'use strict';

angular.module('edup.common')

    .service('PaginationService', function () {

        var getTop = function (paging) {
            return paging.page * paging.perPage;
        };

        var getSkip = function (paging) {
            return (paging.page * paging.perPage) - paging.perPage;
        };

        return {
            Top: getTop,
            Skip: getSkip
        };

    }
);