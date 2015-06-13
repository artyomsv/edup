'use strict';

angular.module('edup.students')

    .directive('studentDocuments', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-documents',

            controller: function ($scope) {

                $scope.documents = [
                    {
                        'name': 'report.docx',
                        'date': '2015/03/03',
                        'link': 'https:/localhost:8443/edup/api/file/report.docx'
                    },
                    {
                        'name': 'balance.xls',
                        'date': '2015/03/04',
                        'link': 'https:/localhost:8443/edup/api/file/balance.xls'
                    },
                    {
                        'name': 'photo.jpg',
                        'date': '2015/03/04',
                        'link': 'https:/localhost:8443/edup/api/file/photo.jpg'
                    }
                ];
            },

            link: function (scope) {
                scope.onShowDownloadToggle = function () {
                    scope.showDownloadSection = true;
                    scope.documentsButtonLabel = 'Hide'
                };

                scope.offShowDownloadToggle = function () {
                    scope.showDownloadSection = false;
                    scope.documentsButtonLabel = 'Show'
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