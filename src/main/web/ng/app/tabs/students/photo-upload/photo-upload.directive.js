'use strict';

angular.module('edup.students')

	.directive('photoUpload', function (UrlService, FileUploader, $window, $timeout, NotificationService, RestService) {
		return {
			restrict: 'E',
			templateUrl: 'photo-upload',
			scope: {
				photoId: '=',
				photoUrl: '='
			},
			priority: 10,
			link: function (scope) {
				scope.photoIsSelected = false;

				scope.uploader = new FileUploader({
					url: UrlService.Files.Upload
				});
				scope.uploader.removeAfterUpload = true;

				scope.deleteItem = function (id) {
					scope.photoIsSelected = false;
				};

				scope.openDownloadUrl = function (url) {
					$window.open(url, '_blank');
				};

				scope.handleItemRemoval = function (item) {
					document.getElementById('photoUploader').value = '';
					item.remove();
					scope.photoIsSelected = false;
				};

				scope.$on('clearFileUploadQueue', function (event, args) {
					scope.photoIsSelected = false;
					scope.uploader.clearQueue();
					scope.photoUploaded = false;
				});

				scope.uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
					scope.photoIsSelected = false;
				};
				scope.uploader.onAfterAddingFile = function (fileItem) {
					scope.photoIsSelected = true;
				};
				scope.uploader.onAfterAddingAll = function (addedFileItems) {
					scope.photoIsSelected = true;
				};
				//scope.uploader.onBeforeUploadItem = function(item) {
				//    console.info('onBeforeUploadItem', item);
				//};
				//scope.uploader.onProgressItem = function(fileItem, progress) {
				//    console.info('onProgressItem', fileItem, progress);
				//};
				//scope.uploader.onProgressAll = function(progress) {
				//    console.info('onProgressAll', progress);
				//};
				scope.uploader.onSuccessItem = function (fileItem, response, status, headers) {
					scope.photoId = response.payload.id;
					scope.photoUrl = UrlService.Files.Download + '/' + scope.photoId;
					NotificationService.Success('Student photo uploaded!');

					$timeout(function () {
						scope.photoUploaded = true;
						scope.photoIsSelected = false;
						document.getElementById('photoUploader').value = '';
					}, 500);
				};
				scope.uploader.onErrorItem = function (fileItem, response, status, headers) {
					NotificationService.Error('Failed to upload student photo!');
					scope.photoIsSelected = false;
					document.getElementById('photoUploader').value = '';
				};
				scope.uploader.onCancelItem = function (fileItem, response, status, headers) {
					scope.photoIsSelected = false;
					document.getElementById('photoUploader').value = '';
				};
				//scope.uploader.onCompleteItem = function(fileItem, response, status, headers) {
				//    scope.photoUrl = UrlService.Download + '/' + response.id;
				//    scope.photoUploaded = true;
				//};
				//scope.uploader.onCompleteAll = function() {
				//    console.info('onCompleteAll');
				//};

			}
		};
	}
);