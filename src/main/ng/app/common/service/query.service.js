'use strict';

angular.module('edup.common')

    .service('QueryService', function () {

        var prepareQuery = function (top, skip, search, orderBy, filters, count) {
            var queries = {};

            queries.$count = true;

            if (top) {
                queries.$top = top;
            }
            if (top) {
                queries.$skip = skip;
            }
            if (top) {
                queries.$search = search;
            }
            if (orderBy) {
                queries.$orderby = orderBy;
            }
            if (filters) {
                queries.$filter = filters;
            }

            if (count) {
                queries.count = count;
            }

            return queries;
        };

        return {
            Query: prepareQuery
        };
    }
);