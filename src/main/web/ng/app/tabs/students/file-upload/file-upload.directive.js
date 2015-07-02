'use strict';

angular.module('edup.students')

    .directive('fileUpload', function ($window, $timeout, UrlService, FileUploader, NotificationService) {
        return {
            restrict: 'E',
            templateUrl: 'file-upload',
            scope: false,
            priority: 10,
            controller: function ($scope, $timeout, RestService) {
                $scope.executeDocumentSave = function (studentId, fileId, fileName) {
                    if (studentId && fileId) {

                        var body = {
                            fileId: fileId,
                            studentId: studentId
                        };

                        RestService.Private.Documents.customPOST(body).then(function (response) {
                            NotificationService.Success('Document ' + fileName + ' uploaded!');
                        });
                    }
                };

            },
            link: function (scope) {
                scope.uploader = new FileUploader({
                    url: UrlService.Files.Upload
                });
                scope.uploader.removeAfterUpload = true;

                scope.deleteItem = function (id) {
                    console.log('delete ' + id);
                };

                scope.openDownloadUrl = function (url) {
                    $window.open(url, '_blank');
                };

                //scope.uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
                //    console.info('onWhenAddingFileFailed', item, filter, options);
                //};
                //scope.uploader.onAfterAddingFile = function (fileItem) {
                //    console.info('onAfterAddingFile', fileItem);
                //};
                //scope.uploader.onAfterAddingAll = function (addedFileItems) {
                //    console.info('onAfterAddingAll', addedFileItems);
                //};
                //scope.uploader.onBeforeUploadItem = function (item) {
                //    console.info('onBeforeUploadItem', item);
                //};
                //scope.uploader.onProgressItem = function (fileItem, progress) {
                //    console.info('onProgressItem', fileItem, progress);
                //};
                //scope.uploader.onProgressAll = function (progress) {
                //    console.info('onProgressAll', progress);
                //};
                scope.uploader.onSuccessItem = function (fileItem, response, status, headers) {
                    var fileId = response.payload.id;
                    scope.executeDocumentSave(scope.selectedStudent.id, fileId, fileItem._file.name);
                };
                scope.uploader.onErrorItem = function (fileItem, response, status, headers) {
                    NotificationService.Error('Failed to upload ' + fileItem._file.name);
                };
                //scope.uploader.onCancelItem = function (fileItem, response, status, headers) {
                //    console.info('onCancelItem', fileItem, response, status, headers);
                //};
                //scope.uploader.onCompleteItem = function (fileItem, response, status, headers) {
                //    console.info('onCompleteItem', fileItem, response, status, headers);
                //};
                scope.uploader.onCompleteAll = function () {
                    $timeout(function() {
                        scope.refreshDocumentsList();
                    }, 1000);
                };
            }
        };
    }
);