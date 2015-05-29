'use strict';

angular.module('edup.widgets')

    .directive('fileUpload', function (UrlService, FileUploader, $window) {
        return {
            restrict: 'E',
            templateUrl: 'file-upload',
            scope: false,
            priority: 10,
            link: function (scope) {
                scope.uploader = new FileUploader({
                    url: UrlService.BaseUrl + '/secured/files/upload'
                });
                scope.uploader.removeAfterUpload = true;

                scope.deleteItem = function (id) {
                    console.log('delete ' + id);
                };

                scope.openDownloadUrl = function (url) {
                    $window.open(url, '_blank');
                };

                scope.uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
                    console.info('onWhenAddingFileFailed', item, filter, options);
                };
                scope.uploader.onAfterAddingFile = function(fileItem) {
                    console.info('onAfterAddingFile', fileItem);
                };
                scope.uploader.onAfterAddingAll = function(addedFileItems) {
                    console.info('onAfterAddingAll', addedFileItems);
                };
                scope.uploader.onBeforeUploadItem = function(item) {
                    console.info('onBeforeUploadItem', item);
                };
                scope.uploader.onProgressItem = function(fileItem, progress) {
                    console.info('onProgressItem', fileItem, progress);
                };
                scope.uploader.onProgressAll = function(progress) {
                    console.info('onProgressAll', progress);
                };
                scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {
                    console.info('onSuccessItem', fileItem, response, status, headers);
                };
                scope.uploader.onErrorItem = function(fileItem, response, status, headers) {
                    console.info('onErrorItem', fileItem, response, status, headers);
                };
                scope. uploader.onCancelItem = function(fileItem, response, status, headers) {
                    console.info('onCancelItem', fileItem, response, status, headers);
                };
                scope.uploader.onCompleteItem = function(fileItem, response, status, headers) {
                    console.info('onCompleteItem', fileItem, response, status, headers);
                };
                scope.uploader.onCompleteAll = function() {
                    console.info('onCompleteAll');
                };
            }
        };
    }
);