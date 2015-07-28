'use strict';

angular.module('edup.students')

	.directive('studentDocuments', function () {
		return {
			restrict: 'E',
			templateUrl: 'student-documents',

			controller: function ($scope, QueryService, RestService, PaginationService, UrlService) {

				$scope.initDocumentsPagination = function () {
					$scope.documentsPaging = {
						enabled: false,
						page: 1,
						perPage: 5,
						totalRecords: 0
					};
				};

				$scope.initDocumentsPagination();

				$scope.documentsSearch = {
					spin: false
				};

				$scope.loadDocuments = function (top, skip, search) {
					$scope.basicSearch.spin = true;
					var query = QueryService.Query(top, skip, search, 'Created desc', 'StudentId eq ' + $scope.selectedStudent.id);
					RestService.Private.Documents.get(query).then(function (result) {
						$scope.documentsSearch.spin = false;
						$scope.documentsPaging.totalRecords = result.count;
						$scope.documents = result.values;
						_.forEach($scope.documents, function (document) {
							document.link = UrlService.Files.Download + '/' + document.fileId;
						});
					});
				};

				$scope.refreshDocumentsList = function () {
					$scope.documents = null;
					$scope.initDocumentsPagination();
					$scope.loadDocuments(PaginationService.Top($scope.documentsPaging), PaginationService.Skip($scope.documentsPaging));
				};

				$scope.documentsPageChanged = function (newPage, searchValue) {
					if (!$scope.documentsSearch.spin) {
						$scope.documentsPaging.page = newPage;
						$scope.loadDocuments(PaginationService.Top($scope.documentsPaging), PaginationService.Skip($scope.documentsPaging), searchValue);
					}
				};


			},

			link: function (scope) {
				scope.onShowDownloadToggle = function () {
					scope.showDownloadSection = true;
					scope.documentsButtonLabel = 'Hide';
				};

				scope.offShowDownloadToggle = function () {
					scope.showDownloadSection = false;
					scope.documentsButtonLabel = 'Show';
				};

				scope.toggleDownloadSection = function () {
					scope.showDownloadSection = !scope.showDownloadSection;
					if (scope.showDownloadSection) {
						scope.onShowDownloadToggle();
					} else {
						scope.offShowDownloadToggle();
					}
				};

				scope.offShowDownloadToggle();
			}
		};
	}
);