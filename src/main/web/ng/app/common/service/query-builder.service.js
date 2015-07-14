'use strict';

angular.module('edup.common')

	.service('QueryBuilderService', function () {

		function QueryBuilder() {

			var $top = 10;
			var $skip = 0;
			var $search = '';
			var $orderby = '';
			var $filter = '';
			var $count = false;
			var $all = false;
			var $head = false;

			var top = function (top) {
				$top = top;
				return this;
			};

			var skip = function (skip) {
				$skip = skip;
				return this;
			};

			var search = function (search) {
				$search = search;
				return this;
			};

			var count = function () {
				$count = true;
				return this;
			};

			var all = function () {
				$all = true;
				return this;
			};

			var orderby = function (parameter, order) {
				if (_.isBlankString($orderby)) {
					$orderby += parameter + ' ' + order;
				} else {
					$orderby += ',' + parameter + ' ' + order;
				}
				return this;
			};

			var filter = function (filterValue) {
				$filter = filterValue;
				return this;
			};

			var build = function () {
				var query = {};
				query.$top = $top;
				query.$skip = $skip;
				query.$count = $count;
				query.$all = $all;
				query.$head = $head;
				if (!_.isBlankString($search)) {
					query.$search = $search;
				}
				if (!_.isBlankString($orderby)) {
					query.$orderby = $orderby;
				}
				if (!_.isBlankString($filter)) {
					query.$filter = $filter;
				}

			};

		}

		return new QueryBuilder();
	}
);