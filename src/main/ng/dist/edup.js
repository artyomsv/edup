'use strict';

angular.module('edup.common', [
	'restangular',
	'angularUtils.directives.dirPagination',
]);
'use strict';

angular.module('edup.common')

    .directive('appModal', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissModal = function () {
                    element.modal('hide');
                };

                scope.showModal = function () {
                    element.modal('show');
                };
            }
        };
    }
);

'use strict';

angular.module('edup.common')

    .directive('edupToggle', ['$timeout', function ($timeout) {
        return {
            restrict: 'E',
            templateUrl: 'edup-toggle',
            scope: {
                onEvent: '&',
                offEvent: '&',
                onLabel: '@',
                offLabel: '@',
                toggleId: '@',
                linkWidget: '='
            },
            controller: ['$scope', function ($scope) {

            }],

            link: function (scope) {
                $timeout(function () {
                    if (!scope.onLabel) {
                        scope.onLabel = 'On';
                    }
                    if (!scope.offLabel) {
                        scope.offLabel = 'Off';
                    }

                    var toggle = $('#' + scope.toggleId);

                    toggle.bootstrapToggle();

                    toggle.change(function () {
                        if ($(this).prop('checked')) {
                            scope.onEvent();
                        } else {
                            scope.offEvent();
                        }
                    });

                });
            }
        };
    }]
);

'use strict';

angular.module('edup.common')

    .directive('windowSize', ['$document', '$window', function ($document, $window) {
        return {
            restrict: 'A',
            link: function (scope) {
                var windowHeight = $window.innerHeight || $document.body.clientHeight;
                var headerHeight = $('#edupHeader').height();
                scope.viewScrollPaneHeight = windowHeight - headerHeight - 40;
                console.log(scope.viewScrollPaneHeight);
            }
        };
    }]
);

'use strict';

angular.module('edup.common')

	.filter('toJson', function () {
		return function (input, pretty) {
			return angular.toJson(input, pretty);
		};
	}
);
'use strict';

angular.module('edup.common')

	.config(['paginationTemplateProvider', function (paginationTemplateProvider) {

		var location = window.location.hostname;

		var baseUrl;

		if (location.indexOf('127.0.0.1') > -1) {
			baseUrl = 'http://127.0.0.1:8088/';
		} else {
			baseUrl = 'https://' + location + ':8443/edup/ng';
		}

		paginationTemplateProvider.setPath(baseUrl + '/vendor/bower_components/angular-utils-pagination/dirPagination.tpl.html');
	}]
);
'use strict';

angular.module('edup.common').run(['Restangular', 'UrlService', 'NotificationService', function (Restangular, UrlService, NotificationService) {

    Restangular.setBaseUrl(UrlService.BaseUrl + '/api');

    Restangular.setErrorInterceptor(function (resp) {
        var msg = '';
        if (resp.status === 400) {
            _.forEach(resp.data.errors, function (error) {
                msg += error.message + ',\n\n';
            });
        } else {
            msg = 'Failed on: ' + resp.config.method + ' to: ' + resp.config.url;
        }
        NotificationService.Error(msg);
        return true;
    });

    Restangular.setRestangularFields({
        etag: 'versionId'
    });

    Restangular.setDefaultHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'X-Requested-With': 'XMLHttpRequest'
    });

}]);

'use strict';

angular.module('edup.common')

    .service('NotificationService', function () {

        /* jshint ignore:start */

        alertify.set('notifier', 'position', 'bottom-right');

        return {
            Success: function (msg, title) {
                alertify.success(msg);
            },
            Info: function (msg, title) {
                alertify.message(msg);
            },
            Error: function (msg, title) {
                alertify.error(msg);
            }

        };

        /* jshint ignore:end */

    }
);
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
'use strict';

angular.module('edup.common')

    .service('RestService', ['Restangular', function (Restangular) {

        var privateResource = Restangular.one('private');
        var publicResource = Restangular.one('public');

        return {
            Private: {
                Students: privateResource.one('students'),
                Balance: privateResource.one('balance'),
                Documents: privateResource.one('documents'),
                LogOut: privateResource.one('logout'),
                Subjects: privateResource.one('subjects')
            },
            Public: {
                Ping : publicResource.one('ping'),
                Login: publicResource.one('login')
            }

        };

    }]
);
'use strict';

angular.module('edup.common')

	.service('TypeAheadService', ['UrlService', function (UrlService) {

		var url = UrlService.Subjects;
		var dataSet = {};

		var filterResponse = function (response) {
			dataSet = response.values;
			var pluck = _.pluck(response.values, 'subjectName');
			return pluck;
		};

		/* jshint ignore:start */
		var typeAhead = function () {
			var bloodhound = new Bloodhound({
				datumTokenizer: function (datum) {
					return Bloodhound.tokenizers.whitespace(datum.value);
				},
				queryTokenizer: Bloodhound.tokenizers.whitespace,
				limit: 10,
				remote: {
					url: url + '?$count=true&$orderby=Created+desc&$search=%QUERY&$skip=0&$top=999&count=true',
					filter: filterResponse,
					wildcard: '%QUERY'
				}
			});
			bloodhound.initialize();
			return bloodhound;
		};

		return {
			Build: typeAhead,
			DataSet: function () {
				return dataSet;
			}
		};
		/* jshint ignore:end */
	}]
);
'use strict';

angular.module('edup.common')

	.service('UrlService', function () {

		var location = window.location.hostname;

		var baseUrl;

		if (location.indexOf('127.0.0.1') > -1) {
			baseUrl = 'https://localhost:8443/edup';
		} else {
			baseUrl = 'https://' + location + ':8443/edup';
		}

		return {
			BaseUrl: baseUrl,
			Files: {
				Info: baseUrl + '/api/private/files',
				Upload: baseUrl + '/api/private/files/upload',
				Download: baseUrl + '/api/private/files/download'
			},
			Subjects: baseUrl + '/api/private/subjects',
			Reports: {
				Events : baseUrl + '/api/private/reports/visiting/plan/subject'
			}
		};

	}
);
'use strict';

angular.module('edup.login', []);
'use strict';

angular.module('edup.login')

    .directive('edupLogin', function () {
        return {
            restrict: 'E',
            templateUrl: 'edup-login',

            controller: ['$scope', '$window', 'RestService', 'UrlService', 'NotificationService', function ($scope, $window, RestService, UrlService, NotificationService) {
                $scope.submitLogin = function (user, password) {
                    RestService.Public.Login.customPOST(
                        'j_username=' + user + '&j_password=' + password,
                        undefined,
                        {},
                        {'Content-Type': 'application/x-www-form-urlencoded'})

                        .then(function () {
                            $window.location.href = UrlService.BaseUrl;
                        });
                };
            }],

            link: function () {

            }
        };
    }
);

'use strict';

angular.module('edup.header', ['ui.router']);
'use strict';

angular.module('edup.header')

	.directive('edupHeader', function () {
		return {
			restrict: 'E',
			templateUrl: 'edup-header',

			controller: ['$scope', '$window', '$location', 'RestService', 'UrlService', function ($scope, $window, $location, RestService, UrlService) {

				$scope.isActive = function (viewLocation) {
					return viewLocation === $location.path();
				};

				$scope.logoutUser = function () {
					RestService.Private.LogOut.post().then(function () {
						$window.location.href = UrlService.BaseUrl;
					});
				};
			}],

			link: function () {

			}

		};
	}
);
'use strict';

angular.module('edup.header')

	.controller('NavbarController', ['$scope', '$state', 'RestService', function ($scope, $state, RestService) {

		$scope.application = {
			version: '     '
		};

		RestService.Public.Ping.get().then(function (response) {
			$scope.application.version = response.payload.version;
		});

		$scope.appModel = {
			items: ['Students', 'Subjects', 'Calendar'],
			states: ['students', 'subjects', 'calendar'],
			current: 0
		};

		$scope.$watch(function () {
			return $scope.appModel.current;
		}, function (index) {
			$state.go($scope.appModel.states[index]);
		});

	}]
);
'use strict';

angular.module('edup.footer', []);
'use strict';

angular.module('edup.footer')

    .controller('FooterController', ['$scope', '$location', 'Restangular', function ($scope, $location, Restangular) {
        Restangular.one('ping').get().then(
            function (result) {
                $scope.appVersion = result.version;
            }
        );
    }]
);
'use strict';

angular.module('edup.footer')

	.directive('edupFooter', function () {
		return {
			restrict: 'E',
			templateUrl: 'edup-footer',
			link: function () {

			}

		};
	}
);
'use strict';

angular.module('edup.calendar', [
    'mwl.calendar',
    'ui.bootstrap',
    'ngAnimate'
]);
'use strict';

angular.module('edup.calendar')

	.controller('CalendarController', ['$scope', '$modal', 'moment', 'calendarTitle', function ($scope, $modal, moment, calendarTitle) {

		var update = function () {
			$scope.currentDay = calendarTitle.month();
		};

		update();

		//These variables MUST be set as a minimum for the calendar to work
		$scope.calendarView = 'month';
		$scope.calendarDay = new Date();
		$scope.events = [
			{
				title: 'My event title', // The title of the event
				type: 'info', // The type of the event (determines its color). Can be important, warning, info, inverse, success or special
				startsAt: moment().subtract(2, 'day').toDate(), // A javascript date object for when the event starts
				endsAt: moment().add(2, 'days').toDate(), // A javascript date object for when the event ends
				editable: false, // If edit-event-html is set and this field is explicitly set to false then dont make it editable
				deletable: false, // If delete-event-html is set and this field is explicitly set to false then dont make it deleteable
				incrementsBadgeTotal: true //If set to false then will not count towards the badge total amount on the month and year view
			},
			{
				title: 'This some strange warning event',
				type: 'warning',
				startsAt: moment().startOf('week').subtract(2, 'days').add(8, 'hours').toDate(),
				endsAt: moment().startOf('week').add(1, 'week').add(9, 'hours').toDate()
			},
			{
				title: 'Information event',
				type: 'info',
				startsAt: moment().subtract(1, 'day').toDate(),
				endsAt: moment().add(5, 'days').toDate()
			},
			{
				title: 'This is a really long and importnat event title',
				type: 'important',
				startsAt: moment().startOf('day').add(5, 'hours').toDate(),
				endsAt: moment().startOf('day').add(19, 'hours').toDate()
			}
		];

		function showModal(action, event) {
			$modal.open({
				templateUrl: 'modalContent.html',
				controller: ['$scope', '$modalInstance', function ($scope, $modalInstance) {
					$scope.$modalInstance = $modalInstance;
					$scope.action = action;
					$scope.event = event;
				}]
			});
		}

		$scope.eventClicked = function (event) {
			$scope.currentDay = this.calendarTitle;
			showModal('Clicked', event);
		};

		$scope.eventEdited = function (event) {
			$scope.currentDay = this.calendarTitle;
			showModal('Edited', event);
		};

		$scope.eventDeleted = function (event) {
			$scope.currentDay = this.calendarTitle;
			showModal('Deleted', event);
		};

		$scope.toggle = function ($event, field, event) {
			$event.preventDefault();
			$event.stopPropagation();
			event[field] = !event[field];
		};

		$scope.setCalendarView = function (view) {
			$scope.calendarView = view;
			$scope.currentDay = this.calendarTitle;
		};

		$scope.updateTitle = function () {
			$scope.currentDay = this.calendarTitle;
		};

	}]
);


'use strict';

angular.module('edup.calendar')

    .directive('сalendar', function () {
        return {
            restrict: 'E',
            templateUrl: 'calendar',
            link: function ($scope) {
            }
        };
    }
);
'use strict';

angular.module('edup.students', []);
'use strict';

angular.module('edup.students')

	.directive('studentAccounting', function () {
		return {
			restrict: 'E',
			templateUrl: 'student-accounting',

			controller: ['$scope', 'RestService', 'NotificationService', function ($scope, RestService, NotificationService) {

				$scope.updateStudentBalance = function (balance, student) {
					if (balance && balance.amount) {
						var balanceDto = {
							studentId: student.id,
							amount: balance.amount * 100,
							comments: balance.comment,
							cash: balance.cash ? balance.cash : true
						};

						RestService.Private.Balance.customPOST(balanceDto).then(function (response) {
							var recordId = response.payload;
							if (recordId) {
								student.balance += balance.amount;
								$scope.balance = null;
								NotificationService.Success(balance.amount + ' EUR was added to ' + student.name + ' ' + student.lastName + ' student!');
							}
						});
					}
				};
			}],

			link: function (scope) {
			}
		};
	}
);
'use strict';

angular.module('edup.students')

	.directive('studentAttendance', function () {
		return {
			restrict: 'E',
			templateUrl: 'student-attendance',

			controller: function () {

				//$scope.studentAttendanceSearch = {
				//	spin: false,
				//	searchValue: '',
				//	values: [],
				//	total: 0
				//};
				//
				//$scope.loadStudentAttendace = function () {
				//	if ($scope.studentAttendanceSearch.values.length !== 0 && ($scope.studentAttendanceSearch.values.length === $scope.studentAttendanceSearch.total)) {
				//		return;
				//	}
				//
				//	if (!$scope.studentAttendanceSearch.spin) {
				//		$scope.studentAttendanceSearch.spin = true;
				//
				//		var query = QueryService.Query(10, $scope.studentAttendanceSearch.values.length + 10, search, 'Name asc, LastName asc', filter);
				//
				//
				//	}
				//
				//};

				//$scope.attendanceHistory = [
				//	{
				//		'subject': 'Math',
				//		'date': '2015/03/03',
				//		'amount': 15
				//	},
				//	{
				//		'subject': 'Literature',
				//		'date': '2015/03/03',
				//		'amount': 15
				//	},
				//	{
				//		'subject': 'Sport',
				//		'date': '2015/03/04',
				//		'amount': 20
				//	},
				//	{
				//		'subject': 'History',
				//		'date': '2015/03/04',
				//		'amount': 10
				//	},
				//	{
				//		'subject': 'Math',
				//		'date': '2015/03/05',
				//		'amount': 15
				//	},
				//	{
				//		'subject': 'English',
				//		'date': '2015/03/07',
				//		'amount': 15
				//	},
				//	{
				//		'subject': 'Sport',
				//		'date': '2015/03/07',
				//		'amount': 20
				//	}
				//];
			},

			link: function ($scope) {

			}
		};
	}
);
'use strict';

angular.module('edup.students')

	.directive('balanceModal', ['RestService', 'NotificationService', function (RestService, NotificationService) {
		return {
			restrict: 'E',
			templateUrl: 'balance-modal',
			link: function (scope) {

				scope.balanceUpdateInProgress = false;

				scope.price = {
					amount: 0,
					cash: true
				};

				scope.saving = function (balance) {
					if (scope.balanceUpdateInProgress) {
						return;
					}

					if (balance && balance.amount) {
						scope.balanceUpdateInProgress = true;
						var balanceDto = {
							studentId: scope.selectedStudent.id,
							amount: balance.amount * 100,
							comments: balance.comment,
							cash: balance.cash ? balance.cash : true
						};

						RestService.Private.Balance.customPOST(balanceDto)
							.then(function (response) {
								var recordId = response.payload;
								if (recordId) {
									scope.selectedStudent.balance += balance.amount;
									scope.balance = null;
									scope.dismissModal();
									NotificationService.Success(balance.amount + ' EUR was added to ' + scope.selectedStudent.name + ' ' + scope.selectedStudent.lastName + ' student!');
									scope.balanceUpdateInProgress = false;
								}
							}, function (error) {
								scope.balanceUpdateInProgress = false;
							});
					}
				};

				scope.resetValue = function () {
					scope.balance = null;
				};
			}
		};
	}]
);
'use strict';

angular.module('edup.students')

    .directive('studentDetails', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-details',
            link : function ($scope) {
                $scope.directiveTest = 'Student input forms';
            }
        };
    }
);
'use strict';

angular.module('edup.students')

	.directive('studentDocuments', function () {
		return {
			restrict: 'E',
			templateUrl: 'student-documents',

			controller: ['$scope', 'QueryService', 'RestService', 'PaginationService', 'UrlService', function ($scope, QueryService, RestService, PaginationService, UrlService) {

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


			}],

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
'use strict';

angular.module('edup.students')

	.directive('editStudent', function () {
		return {
			restrict: 'E',
			templateUrl: 'edit-student',
			controller: ['$scope', 'RestService', 'NotificationService', 'PaginationService', function ($scope, RestService, NotificationService, PaginationService) {

				$scope.executeStudentUpdate = function (student) {
					if ($scope.studentProcessingInProgress) {
						return;
					}

					//$scope.studentProcessingInProgress = true;

					student.id = $scope.selectedStudent.id;
					student.versionId = $scope.selectedStudent.versionId;
					student.birthDate = new Date(student.birthDateString);
					console.log(angular.toJson(student));
					if (student && student.name && student.lastName && student.id && student.versionId) {
						RestService.Private.Students.one(student.id.toString())
							.customPUT(student)
							.then(function (response) {
								$scope.newStudent = null;
								$scope.photoUrl = null;
								$scope.photoUploaded = false;
								student.versionId = response.payload;
								NotificationService.Success(student.name + ' ' + student.lastName + ' updated!');
								$scope.loadStudents(student.id, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), null);
								$scope.studentProcessingInProgress = false;
							}, function () {
								$scope.studentProcessingInProgress = false;
							});
					}
				};

			}],
			link: function ($scope) {

			}
		};
	}
);
'use strict';

angular.module('edup.students')

	.directive('fileUpload', ['$window', '$timeout', 'UrlService', 'FileUploader', 'NotificationService', function ($window, $timeout, UrlService, FileUploader, NotificationService) {
		return {
			restrict: 'E',
			templateUrl: 'file-upload',
			scope: false,
			priority: 10,
			controller: ['$scope', '$timeout', 'RestService', function ($scope, $timeout, RestService) {
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

			}],
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
					if (response && response.errors.length > 0) {
						var errors = _.pluck(response.errors, 'message');
						NotificationService.Error('Failed to upload ' + fileItem._file.name + '. ' + (errors || []).join(','));
					} else {
						NotificationService.Error('Failed to upload ' + fileItem._file.name);
					}
				};
				//scope.uploader.onCancelItem = function (fileItem, response, status, headers) {
				//    console.info('onCancelItem', fileItem, response, status, headers);
				//};
				//scope.uploader.onCompleteItem = function (fileItem, response, status, headers) {
				//    console.info('onCompleteItem', fileItem, response, status, headers);
				//};
				scope.uploader.onCompleteAll = function () {
					$timeout(function () {
						scope.refreshDocumentsList();
					}, 1000);
				};
			}
		};
	}]
);
'use strict';

angular.module('edup.students')

    .directive('studentIdentificationCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-identification-card',
            link : function ($scope) {
            }
        };
    }
);
'use strict';

angular.module('edup.students')

    .directive('studentsListHeader', function () {
        return {
            restrict: 'E',
            templateUrl: 'students-list-header',
            link : function (scope) {

            }
        };
    }
);
'use strict';

angular.module('edup.students')

	.directive('studentsList', function () {
		return {
			restrict: 'E',
			templateUrl: 'students-list',
			link: function (scope) {

				scope.studentsSearch = {
					studentRecordsFound: true
				};

				scope.openStudentDetailModal = function (student) {
					$('#student-details-modal-view').modal('show');
					$('.nav-tabs a[href="#information"]').tab('show');
				};
			}
		};
	}
);
'use strict';

angular.module('edup.students')

	.controller('StudentsController', ['$scope', '$timeout', '$filter', 'RestService', 'PaginationService', 'QueryService', function ($scope, $timeout, $filter, RestService, PaginationService, QueryService) {

		$scope.studentSelected = false;
		$scope.basicSearch = {
			spin: false
		};
		$scope.studentPaging = {
			enabled: false,
			page: 1,
			perPage: 10,
			totalRecords: 0
		};

		$scope.loadFullStudent = function (id) {
			if (id) {
				$scope.basicSearch.spin = true;
				RestService.Private.Students.one(id.toString()).get().then(function (response) {
					$scope.selectedStudent = response.payload;
					if ($scope.selectedStudent) {
						$scope.studentEdit = _.cloneDeep($scope.selectedStudent);
						if ($scope.studentEdit.birthDate) {
							$scope.studentEdit.birthDateString = $filter('date')(new Date($scope.studentEdit.birthDate), 'yyyy-MM-dd');
						}

						$scope.selectedStudent.balance = (response.payload.balance / 100);

						$scope.studentSelected = true;
						$scope.basicSearch.spin = false;
					}
				});
			}
		};

		$scope.loadStudents = function (id, top, skip, search) {
			$scope.basicSearch.spin = true;
			var query = QueryService.Query(top, skip, search, 'Created desc');
			RestService.Private.Students.get(query).then(function (result) {
				$scope.students = result.values;
				_.forEach($scope.students, function (student) {
					student.fullName = student.name + ' ' + student.lastName;
				});

				$scope.studentPaging.totalRecords = result.count;

				if (!$scope.studentsSearch) {
					$scope.studentsSearch = {
					};
				}

				$scope.studentsSearch.studentRecordsFound = result.count !== 0;
				if ($scope.students.length > 0) {
					if (id) {
						$scope.loadFullStudent(id);
					} else {
						$scope.loadFullStudent($scope.students[0].id);
					}
				} else {
					$scope.studentSelected = false;
				}

				$scope.basicSearch.spin = false;
			});
		};

		if (!$scope.basicSearch.spin) {
			$scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging));
		}

		$scope.setSelected = function (studentId) {
			$scope.loadFullStudent(studentId);
		};

		$scope.addToBalance = function (value) {
			console.log('Add new balance ' + value);
			//$scope.selectedStudent.balance += parseInt(value);
		};

		$scope.studentsPageChanged = function (newPage, searchValue) {
			if (!$scope.basicSearch.spin) {
				$scope.studentPaging.page = newPage;
				$scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), searchValue);
			}
		};

		$scope.setRecordsPerPage = function (newRecordsPerPageValue) {
			$scope.studentPaging.perPage = newRecordsPerPageValue;
		};

		var previousSearch = '';

		$scope.executeSearch = function (searchValue) {
			if (searchValue && searchValue.length > 2) {
				$timeout(function () {
					$scope.studentPaging.page = 1;
					$scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), searchValue);
					previousSearch = searchValue;
				}, 300);
			} else if (_.isEmpty(searchValue) && previousSearch !== searchValue) {
				$timeout(function () {
					$scope.studentPaging.page = 1;
					$scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), null);
					previousSearch = searchValue;
				}, 300);
			}
		};

	}]
);


'use strict';

angular.module('edup.students')

    .directive('students', function () {
        return {
            restrict: 'E',
            templateUrl: 'students',
            link : function ($scope) {
                $scope.directiveTest = 'Students tab directive';
            }
        };
    }
);
'use strict';

angular.module('edup.students')

	.directive('newStudent', function () {
		return {
			restrict: 'E',
			templateUrl: 'new-student-modal',

			controller: ['$scope', 'RestService', 'PaginationService', 'NotificationService', function ($scope, RestService, PaginationService, NotificationService) {
				var dismiss = function () {
					var view = $('#addNewStudentModalView');
					if (view) {
						view.modal('hide');
					}
				};

				$scope.resetNewStudent = function () {
					$scope.$broadcast('clearFileUploadQueue', {message: 'reset'});

					if ($scope.newStudent) {
						$scope.newStudent.birthDate = null;
						$scope.newStudent.photoUrl = null;
						$scope.newStudent.photoId = null;
					}
					$scope.newStudent = null;
					$scope.photoUrl = null;
					$scope.photoUploaded = false;
				};

				$scope.executeStudentSave = function (student) {
					if ($scope.studentProcessingInProgress) {
						return;
					}

					if (student && student.name && student.lastName) {
						$scope.studentProcessingInProgress = true;
						student.birthDate = new Date(student.birthDateString);
						RestService.Private.Students.customPOST(student).then(function (response) {
							NotificationService.Success('Student ' + student.name + ' ' + student.lastName + ' created!');
							$scope.loadStudents(response.payload, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), null);
							$scope.resetNewStudent();
							dismiss();
							$scope.studentProcessingInProgress = false;
						}, function () {
							$scope.studentProcessingInProgress = false;
						});
					}
				};

				$scope.executeReset = function () {
					$scope.resetNewStudent();
				};

				$scope.executeCancel = function () {
					$scope.resetNewStudent();
					dismiss();
				};

			}],

			link: function (scope) {
				scope.dismissNewStudentModalDialog = function () {
					scope.dismissModal();
				};
			}
		};
	}
);
'use strict';

angular.module('edup.students')

	.directive('photoUpload', ['UrlService', 'FileUploader', '$window', '$timeout', 'NotificationService', 'RestService', function (UrlService, FileUploader, $window, $timeout, NotificationService, RestService) {
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
	}]
);
'use strict';

angular.module('edup.subjects', [

]);
'use strict';

angular.module('edup.subjects')

	.directive('confirmEventFinishedModal', function () {
		return {
			restrict: 'E',
			templateUrl: 'confirm-event-finished-modal',
			controller: ['$scope', '$timeout', 'moment', 'RestService', function ($scope, $timeout, moment, RestService) {

				$scope.confirmEventIsFinished = function () {
					if ($scope.studentsManagemnt.confirmation) {
						return;
					}

					$scope.studentsManagemnt.confirmation = true;

					var payload = _.cloneDeep($scope.selectedEvent);
					payload.status = 'FINALIZED';

					RestService.Private.Subjects
						.one('events')
						.one(payload.eventId.toString())
						.customPUT(payload)
						.then(function (response) {
							$scope.selectedEvent.status = payload.status;
							$scope.selectedEvent.currentStatus = 'CONFIRMED';
							var event = _.find($scope.events.values, function (event) {
								return event.eventId === payload.eventId;
							});

							if (event) {
								event.status = $scope.selectedEvent.status;
								event.currentStatus = $scope.selectedEvent.currentStatus;
							}

							$scope.dismissModal();
							$scope.studentsManagemnt.confirmation = false;
						}, function (error) {
							$scope.studentsManagemnt.confirmation = false;
						});

				};


			}],
			link: function (scope) {

			}
		};


	}
);
'use strict';

angular.module('edup.subjects')

	.directive('eventInfo', function () {
		return {
			restrict: 'E',
			templateUrl: 'event-info',
			controller: ['$scope', 'moment', '$filter', 'RestService', 'QueryService', function ($scope, moment, $filter, RestService, QueryService) {

				$scope.studentsManagemnt = {
					expanded: false,
					confirmation: false
				};

				$scope.selectedEvent = {
					loaded: false
				};

				$scope.loadEventDetails = function (event) {
					if (event && event.eventId) {

						RestService.Private.Subjects.one('events').one(event.eventId.toString()).get().then(function (response) {
							if (response.payload) {
								$scope.selectedEvent = response.payload;
								$scope.selectedEvent.loaded = true;
								$scope.selectedEvent.adjustedPrice = $scope.selectedEvent.price / 100;
								$scope.selectedEvent.havePassed = moment().isAfter($scope.selectedEvent.eventDate);
								$scope.selectedEvent.currentStatus = event.currentStatus;

								$scope.resetEventStudentsSearch($scope.selectedEvent.havePassed);

								$scope.loadAttendance(event.eventId);
							}
						});
					}
				};

				$scope.loadAttendance = function (eventId) {
					if (eventId) {
						var query = QueryService.Query(999, 0, '*', null, 'EventId eq ' + eventId, false);
						RestService.Private.Subjects
							.one('events')
							.one('attendance')
							.get(query)
							.then(function (response) {
								$scope.eventStudentsSearch.attendance = response.values;
								$scope.executeSearch();
							});
					}
				};

				$scope.processExpand = function () {
					$scope.studentsManagemnt.expanded = !$scope.studentsManagemnt.expanded;
					$scope.executeSearch();
				};


			}],
			link: function (scope) {

			}
		};
	}
);
'use strict';

angular.module('edup.subjects')

	.directive('eventsHeader', function () {
		return {
			restrict: 'E',
			templateUrl: 'events-header',
			controller: ['$scope', '$timeout', '$filter', 'moment', 'QueryService', 'RestService', 'TypeAheadService', 'NotificationService', function ($scope, $timeout, $filter, moment, QueryService, RestService, TypeAheadService, NotificationService) {

				$scope.subjects = [];
				$scope.selectedSubject = null;

				var selectSubject = function (selectedSubjectName) {
					$scope.selectedSubject = _.find(TypeAheadService.DataSet(), function (subject) {
						return subject.subjectName === selectedSubjectName;
					});
					if (!!$scope.selectedSubject) {
						$scope.subjectEvent.subjectName = $scope.selectedSubject.subjectName;
					}
				};

				var typeAhead = TypeAheadService.Build();

				var $bloodhound = $('#subject-event-typeahead .typeahead');

				$bloodhound.typeahead({
						hint: true,
						highlight: true,
						minLength: 1
					},
					{
						name: 'subjectsTypeAhead',
						source: typeAhead
					}
				);

				$bloodhound.bind('typeahead:selected', function (obj, datum) {
					selectSubject(datum);
				});

				var isSubjectDataFilled = function (newSubjectEvent) {
					if (!newSubjectEvent) {
						return false;
					}
					if (!newSubjectEvent.amount) {
						return false;
					}
					if (!newSubjectEvent.eventDate) {
						return false;
					}
					if (!newSubjectEvent.eventTimeFrom) {
						return false;
					}
					if (!newSubjectEvent.eventTimeTo) {
						return false;
					}
					return true;
				};

				var reset = function () {
					$bloodhound.typeahead('val', '');
					$scope.subjectEvent = null;
					$scope.selectedSubject = null;
				};

				var calculateTime = function (date, time) {
					return $filter('date')(date, 'yyyy-MM-dd') + ' ' + $filter('date')(time, 'HH:mm');
				};

				$scope.renderDatePicker = function ($view, $dates, $leftDate, $upDate, $rightDate) {
					_.forEach($dates, function (date) {
						if (moment(date.utcDateValue).isBefore(moment())) {
							date.selectable = false;
						}
					});
				};

				$scope.renderTimePicker = function ($view, $dates, $leftDate, $upDate, $rightDate) {
					$leftDate.selectable = false;
					$upDate.selectable = false;
					$rightDate.selectable = false;
				};

				$scope.saveNewEvent = function (newSubjectEvent) {
					if (isSubjectDataFilled(newSubjectEvent)) {

						var shouldSave = false;

						var payload = {};
						payload.eventDate = newSubjectEvent.eventDate;
						payload.from = new Date(calculateTime(newSubjectEvent.eventDate, newSubjectEvent.eventTimeFrom));
						payload.to = new Date(calculateTime(newSubjectEvent.eventDate, newSubjectEvent.eventTimeTo));
						payload.price = newSubjectEvent.amount * 100;
						payload.subject = {};
						if (!!$scope.selectedSubject) {
							payload.subject.subjectName = $scope.selectedSubject.subjectName;
							payload.subject.subjectId = $scope.selectedSubject.subjectId;
							shouldSave = true;
						} else if (!_.isEmpty(newSubjectEvent.subjectName)) {
							payload.subject.subjectName = newSubjectEvent.subjectName;
							shouldSave = true;
						}

						if (shouldSave) {
							RestService.Private.Subjects.one('events').customPOST(payload).then(function () {
								reset();
								if (payload.subject.subjectId) {
									NotificationService.Success('Event have been registered to subject \"' + payload.subject.subjectName + '\"');
								} else {
									NotificationService.Success('New subject have been created.\n Event have been registered to subject \"' + payload.subject.subjectName + '\"');
								}

								$scope.loadMoreEvens(true);

							});
						} else {
							NotificationService.Error(angular.toJson(newSubjectEvent, true));
						}
					} else {
						console.log(angular.toJson(newSubjectEvent, true));
					}
				};

				$scope.updateSelectedSubject = function () {
					if (!$scope.subjectEvent) {
						$scope.subjectEvent = {};
					}

					var inputValue = $bloodhound.typeahead('val');
					if ($scope.selectedSubject) {
						if ($scope.selectedSubject.subjectName === inputValue) {
							console.log('value same in input and typeahead');
							$scope.subjectEvent.subjectName = inputValue;
						} else {
							console.log('value not same in input and typeahead');
							$scope.selectedSubject = null;
							$scope.subjectEvent.subjectName = inputValue;
						}
					} else {
						console.log('typeahead not selected');
						$scope.subjectEvent.subjectName = inputValue;
					}
				};

			}],
			link: function (scope) {

			}
		};
	}
);
'use strict';

angular.module('edup.subjects')

	.directive('eventsList', function () {
		return {
			restrict: 'E',
			templateUrl: 'events-list',
			controller: ['$scope', '$timeout', 'moment', 'QueryService', 'RestService', function ($scope, $timeout, moment, QueryService, RestService) {

				$scope.events = {
					values: [],
					total: 0,
					loading: false,
					firstLoad: true,
					eventRecordsFound: true
				};

				$scope.setSelected = function (event) {
					$scope.events.firstLoad = false;
					$scope.loadEventDetails(event);
				};

				$scope.loadMoreEvens = function (force) {
					if (!force && $scope.events.values.length !== 0 && ($scope.events.values.length === $scope.events.total)) {
						return;
					}

					if (force) {
						$scope.events = {
							values: [],
							total: 0,
							loading: false
						};
					}

					if (!$scope.events.loading) {
						$scope.events.loading = true;

						//var query = QueryService.Query(10, $scope.events.values.length, '*', 'Created desc', null, true);
						var query = QueryService.Query(13, $scope.events.values.length, '*', 'EventDate desc,From desc', null, true);

						$timeout(function () {
							RestService.Private.Subjects.one('events').get(query).then(function (response) {
								var events = response.values;
								if ($scope.events.firstLoad && events.length !== 0) {
									$scope.setSelected(events[0]);
								}
								_.forEach(events, function (event) {
									event.priceAdjusted = event.price / 100;
									$scope.events.values.push(event);

									if (event.status === 'FINALIZED') {
										event.currentStatus = 'CONFIRMED';
									} else if (moment().isAfter(event.eventDate)) {
										event.currentStatus = 'PAST';
									} else {
										event.currentStatus = 'FUTURE';
									}

								});

								$scope.events.total = response.count;
								$scope.events.loading = false;

								$scope.events.eventRecordsFound = response.count !== 0;
							});
						}, 300);
					}
				};

			}],
			link: function (scope) {

			}
		};
	}
);
'use strict';

angular.module('edup.subjects')

    .controller('SubjectsController', ['$scope', function ($scope) {

        $scope.subjectsView1 = 'subjectsView1';
        $scope.subjectsView2 = 'subjectsView2';

    }]
);


'use strict';

angular.module('edup.subjects')

    .directive('subjects', function () {
        return {
            restrict: 'E',
            templateUrl: 'subjects',
            link: function (scope) {

            }
        };
    }
);
'use strict';

angular.module('edup.subjects')

	.directive('studentsAttendanceList', function () {
		return {
			restrict: 'E',
			templateUrl: 'students-attendance-list',

			controller: ['$scope', '$timeout', 'QueryService', 'RestService', function ($scope, $timeout, QueryService, RestService) {

				$scope.resetEventStudentsSearch = function (havePassed) {
					$scope.eventStudentsSearch = {
						spin: false,
						formats: [
							'Registered',
							'All'
							//'Not registered'
						],
						searchValue: '',
						values: [],
						total: 0,
						attendance: [],
						studentRecordsFound: true
					};

					$scope.eventStudentsSearch.format = havePassed ? $scope.eventStudentsSearch.formats[0] : $scope.eventStudentsSearch.formats[1];
				};

				$scope.mapAttendance = function (students) {
					_.forEach(students, function (student) {
						student.fullName = student.name + ' ' + student.lastName;
						student.active = false;
						student.showAbsenceToggle = false;
						_.forEach($scope.eventStudentsSearch.attendance, function (attendance) {
							if (student.id === attendance.studentId) {
								student.active = true;
								student.attendanceId = attendance.attendanceId;
								student.showAbsenceToggle = true;
								student.participated = attendance.participated;
							}
						});
					});
				};

				var studentsQuery = function (skip) {
					var search = _.isEmpty($scope.eventStudentsSearch.searchValue) ? '*' : $scope.eventStudentsSearch.searchValue;
					var filter = null;
					var attendanceStudents = _.pluck($scope.eventStudentsSearch.attendance, 'studentId');
					switch ($scope.eventStudentsSearch.format) {
						case 'Registered' :
							filter = 'Id eq (' + (attendanceStudents || []).join(',') + ')';
							break;
						case 'Not registered' :
							filter = 'Id ne (' + (attendanceStudents || []).join(',') + ')';
							break;
					}
					return QueryService.Query(10, skip, search, 'Name asc, LastName asc', filter);
				};

				$scope.executeSearch = function () {
					if ($scope.studentsManagemnt.expanded && !$scope.eventStudentsSearch.spin) {

						$scope.eventStudentsSearch.spin = true;

						var query = studentsQuery(0);

						$timeout(function () {
							RestService.Private.Students.get(query).then(function (response) {
								$scope.eventStudentsSearch.values = response.values;
								$scope.eventStudentsSearch.total = response.count;
								$scope.mapAttendance($scope.eventStudentsSearch.values);
								$scope.eventStudentsSearch.spin = false;

								$scope.eventStudentsSearch.studentRecordsFound = response.count !== 0;
							});
						}, 100);
					}
				};

				$scope.loadMoreStudents = function () {
					if (!$scope.eventStudentsSearch || $scope.eventStudentsSearch.spin) {
						return;
					}

					if ($scope.eventStudentsSearch.values.length !== 0 && ($scope.eventStudentsSearch.values.length === $scope.eventStudentsSearch.total)) {
						return;
					}

					if (!$scope.eventStudentsSearch.spin && $scope.studentsManagemnt.expanded) {
						$scope.eventStudentsSearch.spin = true;

						var query = studentsQuery($scope.eventStudentsSearch.values.length);

						RestService.Private.Students.get(query).then(function (response) {
							var students = response.values;
							_.forEach(students, function (student) {
								$scope.eventStudentsSearch.values.push(student);
							});

							$scope.eventStudentsSearch.total = response.count;
							$scope.mapAttendance(students);
							$scope.eventStudentsSearch.spin = false;
						});
					}
				};

				$scope.updateStudentAttendance = function (student) {
					if (!student.attendanceId) {
						RestService.Private.Subjects
							.one('events')
							.one($scope.selectedEvent.eventId.toString())
							.one('attendance')
							.customPOST({
								eventId: $scope.selectedEvent.eventId,
								studentId: student.id
							})
							.then(function (response) {
								student.attendanceId = response.payload;
								$scope.selectedEvent.students += 1;
								if ($scope.selectedEvent.havePassed) {
									student.showAbsenceToggle = true;

									var attendance = {
										attendanceId: response.payload,
										studentId: student.id,
										participated: true,
										eventId: $scope.selectedEvent.eventId
									};

									$scope.eventStudentsSearch.attendance.push(attendance);
									student.participated = attendance.participated;
								}
							});
					} else {
						RestService.Private.Subjects
							.one('events')
							.one($scope.selectedEvent.eventId.toString())
							.one('attendance')
							.one(student.attendanceId.toString())
							.remove()
							.then(function () {
								_.remove($scope.eventStudentsSearch.attendance, function (attendance) {
									return student.id === attendance.studentId;
								});

								if ($scope.eventStudentsSearch.format === $scope.eventStudentsSearch.formats[0]) {
									_.remove($scope.eventStudentsSearch.values, function (listStudent) {
										return student.id === listStudent.id;
									});
								}

								student.attendanceId = null;
								$scope.selectedEvent.students -= 1;
							});
					}
				};

				$scope.formatChanged = function () {
					$scope.executeSearch();
				};

				$scope.onStudentAbsenceEvent = function (student) {
					if (student.attendanceId) {
						student.updateInProgress = true;
						RestService.Private.Subjects
							.one('events')
							.one($scope.selectedEvent.eventId.toString())
							.one('attendance')
							.one(student.attendanceId.toString())
							.customPUT({
								eventId: $scope.selectedEvent.eventId,
								studentId: student.id,
								participated: student.participated
							})
							.then(function () {
								student.updateInProgress = false;
							}, function () {
								student.updateInProgress = false;
							});
					}
				};

			}],

			link: function (scope) {

			}
		};
	}
)
;
'use strict';

angular.module('edup.reports', [

]);
'use strict';

angular.module('edup.reports')

	.directive('eventAttendanceModal', ['$filter', 'UrlService', function ($filter, UrlService) {
		return {
			restrict: 'E',
			templateUrl: 'event-attendance-modal',
			controller: ['$scope', '$timeout', 'moment', 'RestService', 'TypeAheadService', function ($scope, $timeout, moment, RestService, TypeAheadService) {

				$scope.plannedEventDetails = {
					selectedSubject: null,
					dateFromPicker: null,
					dateToPicker: null,
				};

				var selectSubject = function (selectedSubjectName) {
					$scope.plannedEventDetails.selectedSubject = _.find(TypeAheadService.DataSet(), function (subject) {
						return subject.subjectName === selectedSubjectName;
					});
					console.log(angular.toJson($scope.selectedSubject));
				};


				var typeAhead = TypeAheadService.Build();

				var $bloodhound = $('#subject-event-typeahead-modal .typeahead');

				$bloodhound.typeahead({
						hint: true,
						highlight: true,
						minLength: 1
					},
					{
						name: 'subjectsTypeAhead',
						source: typeAhead
					}
				);

				$bloodhound.bind('typeahead:selected', function (obj, datum) {
					selectSubject(datum);
				});

				$scope.plannedEventJournal = function () {
					$scope.plannedEventDetails = {
						selectedSubject: null,
						dateFromPicker: null,
						dateToPicker: null,
						subjectName: null,
						showAttendance: false
					};
				};

				$scope.finishedEventReport = function () {
					$scope.plannedEventDetails = {
						selectedSubject: null,
						dateFromPicker: null,
						dateToPicker: null,
						subjectName: null,
						showAttendance: true
					};
				};


			}],
			link: function (scope) {

				scope.renderDatePickerForReport = function ($view, $dates, $leftDate, $upDate, $rightDate) {

				};

				scope.performReportDownload = function (details) {
					if (details && details.selectedSubject && details.dateFromPicker && details.dateToPicker) {
						var query = {
							from: $filter('date')(details.dateFromPicker, 'ddMMyyyy'),
							to: $filter('date')(details.dateToPicker, 'ddMMyyyy'),
							attendance: scope.plannedEventDetails.showAttendance
						};
						var url = UrlService.Reports.Events + '/' + details.selectedSubject.subjectId + '?from=' + query.from + '&to=' + query.to + '&attendance=' + query.attendance;
						window.open(url);
						scope.dismissModal();
					}

				};

			}
		};


	}]
);
'use strict';

angular.module('edup.reports')

	.controller('ReportsController', ['$scope', function ($scope) {

	}]
);


'use strict';

angular.module('edup.tabs', [
	'ngSanitize',
	'toggle-switch',
	'ui.bootstrap.datetimepicker',
	'infinite-scroll',
	'angularFileUpload',
	'fiestah.money',
	'edup.reports',
	'edup.calendar',
	'edup.subjects',
	'edup.students'
]);
'use strict';

angular.module('edup',
    [
        'edup.tabs',
        'edup.login',
        'edup.header',
        'edup.footer',
        'edup.common'
    ]
);

'use strict';

angular.module('edup')

    .config(['$stateProvider', function ($stateProvider) {

        $stateProvider
            .state('students', {
                templateUrl: 'students',
                url: '/students',
                controller: 'StudentsController'
            })
            .state('subjects', {
                templateUrl: 'subjects',
                url: '/subjects',
                controller: 'SubjectsController'
            })
            .state('calendar', {
                templateUrl: 'calendar',
                url: '/calendar',
                controller: 'CalendarController'
            }
        );

    }]
);angular.module('edup').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('edup-toggle',
    "<input id={{toggleId}} type=checkbox data-toggle=toggle data-on={{onLabel}} data-off={{offLabel}} data-width=100 data-size=small>"
  );


  $templateCache.put('edup-footer',
    "<div ng-controller=FooterController><nav class=\"navbar navbar-default\" style=\"margin-bottom: 0px !important; margin-top: auto !important\"><div class=\"navbar-text text-center\" style=\"width: 100%\">Application version: {{ appVersion }}</div></nav></div>"
  );


  $templateCache.put('edup-header',
    "<div><nav class=\"navbar navbar-default\"><div class=container-fluid><div class=navbar-header style=\"margin-right: 30px\"><button type=button class=\"navbar-toggle collapsed\" data-toggle=collapse data-target=#bs-example-navbar-collapse-1><span class=sr-only>Toggle navigation</span> <span class=icon-bar></span> <span class=icon-bar></span> <span class=icon-bar></span></button><div class=navbar-brand>Educational planning application: {{application.version}}</div></div><div class=\"collapse navbar-collapse\" id=bs-example-navbar-collapse-1><ul class=\"nav navbar-nav\"><li ng-class=\"{ active: isActive('/students')}\"><a href=#students>Students<span class=sr-only>(current)</span></a></li><li ng-class=\"{ active: isActive('/subjects')}\"><a href=#subjects>Subjects</a></li><li ng-controller=ReportsController class=dropdown><a href=#report class=dropdown-toggle data-toggle=dropdown role=button aria-expanded=false>Reports<span class=caret></span></a><ul class=dropdown-menu role=menu><li ng-click=plannedEventJournal() data-toggle=modal data-target=#eventAttendanceModalView><a href=#>Planned event journal</a></li><li ng-click=finishedEventReport() data-toggle=modal data-target=#eventAttendanceModalView><a href=#>Finished event report</a></li></ul></li></ul><ul class=\"nav navbar-nav navbar-right\"><li><a href=# target=_self ng-click=logoutUser()>Log out</a></li></ul></div></div></nav></div><event-attendance-modal></event-attendance-modal>"
  );


  $templateCache.put('edup-login',
    "<div class=login-container><div id=output></div><div class=avatar></div><div class=form-box><form name=form method=post><input name=j_username placeholder=username id=text ng-model=username required> <input type=password name=j_password placeholder=password id=password ng-model=password required> <button class=\"btn btn-info btn-block login\" type=submit ng-click=\"submitLogin(username, password)\">Login</button></form></div></div>"
  );


  $templateCache.put('calendar',
    "<div class=mainForm ng-controller=CalendarController><div class=\"col-md-12 column\"><div class=\"col-md-3 form-inline\"><div class=\"btn-group pull-left\"><button class=\"btn btn-primary btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay decrement=calendarView>&lt;&lt; Prev</button> <button class=\"btn btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay set-to-today>Today</button> <button class=\"btn btn-primary btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay increment=calendarView>Next &gt;&gt;</button></div></div><div class=col-md-2><div class=btn-group><h4>{{ currentDay }}</h4></div></div><div class=\"col-md-3 form-inline\"><div class=\"btn-group pull-right\"><button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('year')\">Year</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('month')\">Month</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('week')\">Week</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('day')\">Day</button></div></div><div class=\"col-md-4 form-inline\"><h4>Today events:</h4></div></div><div class=\"col-md-12 column\" style=\"padding-top: 20px\"><div class=\"col-md-8 column\"><mwl-calendar events=events view=calendarView view-title=calendarTitle current-day=calendarDay on-event-click=eventClicked(calendarEvent) edit-event-html=\"'<i class=\\'glyphicon glyphicon-pencil\\'></i>'\" delete-event-html=\"'<i class=\\'glyphicon glyphicon-remove\\'></i>'\" on-edit-event-click=eventEdited(calendarEvent) on-delete-event-click=eventDeleted(calendarEvent)></mwl-calendar></div><div class=\"col-md-4 column\"><div ng-repeat=\"event in events\"><h4><div class=label ng-class=\"{ 'label-primary': event.type === 'info', 'label-warning': event.type === 'warning', 'label-danger': event.type === 'important'}\" data-toggle=tooltip data-placement=top title=\"Tooltip on top\" tooltip=\"{{ event.title }}\" tooltip-trigger=\"{{{true: 'mouseenter', false: 'never'}[myForm.username.$invalid]}}\">({{event.startsAt| date:'HH:mm'}} - {{event.endsAt | date:'HH:mm'}}) {{event.title | limitTo: 33}} {{event.title.length > 33 ? '...' : ''}}</div></h4></div></div></div></div>"
  );


  $templateCache.put('event-attendance-modal',
    "<div app-modal id=eventAttendanceModalView class=\"modal fade\" tabindex=-1 role=dialog aria-labelledby=mySmallModalLabel aria-hidden=true><div class=\"modal-dialog modal-sm\"><div class=\"modal-content modalViewPadding\"><form role=form name=EventAttendanceForm><div class=row><div class=form-group id=subject-event-typeahead-modal><div class=input-group id=subjectTypeAheadInput><span class=input-group-addon id=basic-addon2 ng-class=\"{'glyphicon glyphicon-ok searchTextInput': !selectedSubject , 'glyphicon glyphicon-pencil searchTextInput': !!selectedSubject}\"></span><div id=scrollable-dropdown-menu><input required class=\"form-control typeahead tt-query\" placeholder=Subject ng-model=plannedEventDetails.subjectName ng-keyup=updateSelectedSubject()></div></div></div></div><div class=row><div class=form-group><div class=dropdown><a class=dropdown-toggle id=subjectEventDatefromIdModal role=button data-toggle=dropdown data-target=# href=#><div class=input-group><input required datepicker-popup=yyyy-MM-dd class=form-control data-ng-model=plannedEventDetails.dateFromPicker placeholder=From> <span class=input-group-addon><i class=\"glyphicon glyphicon-calendar\"></i></span></div></a><ul class=dropdown-menu role=menu aria-labelledby=dLabel><datetimepicker data-ng-model=plannedEventDetails.dateFromPicker data-before-render=\"renderDatePickerForReport($view, $dates, $leftDate, $upDate, $rightDate)\" data-datetimepicker-config=\"{ dropdownSelector: '#subjectEventDatefromIdModal' , startView:'day', minView:'day'}\"></datetimepicker></ul></div></div></div><div class=row><div class=form-group><div class=dropdown><a class=dropdown-toggle id=subjectEventDateToIdModal role=button data-toggle=dropdown data-target=# href=#><div class=input-group><input required datepicker-popup=yyyy-MM-dd class=form-control data-ng-model=plannedEventDetails.dateToPicker placeholder=To> <span class=input-group-addon><i class=\"glyphicon glyphicon-calendar\"></i></span></div></a><ul class=dropdown-menu role=menu aria-labelledby=dLabel><datetimepicker data-ng-model=plannedEventDetails.dateToPicker data-before-render=\"renderDatePickerForReport($view, $dates, $leftDate, $upDate, $rightDate)\" data-datetimepicker-config=\"{ dropdownSelector: '#subjectEventDateToIdModal' , startView:'day', minView:'day'}\"></datetimepicker></ul></div></div></div><div class=row><button style=\"margin-top: 10px\" class=\"btn btn-success btn-sm pull-right\" type=button ng-click=performReportDownload(plannedEventDetails)>Submit</button></div></form></div></div></div>"
  );


  $templateCache.put('student-accounting',
    "<div class=mainForm style=\"margin-left: 30px; margin-right: 30px\"><div class=\"row clearfix\"><div class=\"col-md-12 column\"><form role=form name=studentBalanceUpdate><div class=form-group><label for=amount>Enter a monetary amount:</label><div class=input-group><span class=input-group-addon>&euro;</span> <input id=amount class=form-control money=\"\" ng-model=balance.amount autofocus precision=2></div></div><div class=form-group><label for=comments>Comments</label><textarea ng-model=balance.comment class=\"form-control fixedTextArea\" id=comments name=comments></textarea></div><div class=\"form-group col-md-12 column\"><div class=\"col-md-6 column\"><label><input type=radio name=operationType value=true ng-model=balance.cash ng-checked=true> Cash</label></div><div class=\"col-md-6 column\"><label><input type=radio name=operationType value=false ng-model=balance.cash> Transfer</label></div></div><div class=\"form-group text-center\"><button ng-click=\"updateStudentBalance(balance, selectedStudent)\" id=addToBalanceButton class=\"btn btn-success btn-sm\">Save</button> <button ng-click=resetValue() id=clearBalanceUpdate type=reset class=\"btn btn-primary btn-sm\">Reset</button></div></form></div></div></div>"
  );


  $templateCache.put('student-attendance',
    "<div class=mainForm style=\"margin-left: 30px; margin-right: 30px\"><div class=row><div class=col-md-12><h3>Under development</h3></div></div></div>"
  );


  $templateCache.put('balance-modal',
    "<div app-modal id=addToBalanceModalView class=\"modal fade bs-example-modal-sm\" tabindex=-1 role=dialog aria-labelledby=mySmallModalLabel aria-hidden=true><div class=\"modal-dialog modal-sm\"><div class=\"modal-content modalViewPadding\"><form><fieldset><legend>Add money to account</legend><div class=form-group><label for=amount>Enter a monetary amount:</label><div class=input-group><span class=input-group-addon>&euro;</span> <input required id=amount class=form-control money=\"\" ng-model=balance.amount autofocus precision=2></div></div><div class=form-group><label for=comments>Comments</label><textarea ng-model=balance.comment class=\"form-control fixedTextArea\" id=comments name=comments></textarea></div><div class=\"form-group col-md-12 column\"><div class=\"col-md-6 column\"><label><input type=radio name=operationType value=true ng-model=balance.cash ng-checked=true> Cash</label></div><div class=\"col-md-6 column\"><label><input type=radio name=operationType value=false ng-model=balance.cash> Transfer</label></div></div><div ng-init=\"balanceUpdateInProgress = false\" class=\"form-group text-center\"><button ng-click=saving(balance) id=addToBalanceButton ng-disabled=balanceUpdateInProgress class=\"btn btn-success btn-sm\">Save</button> <button ng-click=resetValue() id=clearBalanceUpdate type=reset ng-disabled=balanceUpdateInProgress class=\"btn btn-primary btn-sm\">Reset</button> <button id=cancelBalanceUpdate class=\"btn btn-warning btn-sm\" data-dismiss=modal ng-disabled=balanceUpdateInProgress ng-click=\"balance = null\">Cancel</button></div></fieldset></form></div></div></div>"
  );


  $templateCache.put('student-details',
    "<div app-modal id=student-details-modal-view class=\"modal fade bs-example-modal-lg\" tabindex=-1 role=dialog aria-labelledby=myLargeModalLabel aria-hidden=true><div class=\"modal-dialog modal-lg\"><div class=\"modal-content modalViewPadding\"><div class=modal-header style=\"padding: 5px !important;border-bottom: none !important\"><div class=\"parent modal-title\" id=myModalLabel><div class=\"child pull=left\"><h4>{{selectedStudent.name}} {{selectedStudent.lastName}}</h4></div><div class=\"child pull-right\"><h4 class=pull-right><span class=label style=\"font-weight: 200 !important\" ng-class=\"{'label-danger' :  selectedStudent.balance < 0, 'label-success' :  selectedStudent.balance > 0, 'label-primary' :  selectedStudent.balance == 0}\">{{ selectedStudent.balance | number : 2}} EUR</span></h4></div></div></div><div role=tabpanel><ul class=\"nav nav-tabs\" role=tablist style=\"border: none !important\"><li role=presentation class=active ng-class=\"{ active: isActive('/information')}\"><a href=#information aria-controls=home role=tab data-toggle=tab>Information</a></li><li role=presentation ng-class=\"{ active: isActive('/accounting')}\"><a href=#accounting aria-controls=profile role=tab data-toggle=tab>Accounting</a></li><li role=presentation ng-class=\"{ active: isActive('/documents')}\" ng-click=refreshDocumentsList()><a href=#documents aria-controls=messages role=tab data-toggle=tab>Documents</a></li><li role=presentation ng-class=\"{ active: isActive('/attendance')}\"><a href=#attendance aria-controls=settings role=tab data-toggle=tab>Attendance</a></li></ul><div class=tab-content style=\"margin-bottom: 10px\"><div role=tabpanel class=\"tab-pane active first-tab\" id=information><edit-student></edit-student></div><div role=tabpanel class=\"tab-pane next-tab\" id=accounting><student-accounting></student-accounting></div><div role=tabpanel class=\"tab-pane next-tab\" id=documents><student-documents></student-documents></div><div role=tabpanel class=\"tab-pane next-tab\" id=attendance><student-attendance></student-attendance></div></div></div></div></div></div>"
  );


  $templateCache.put('student-documents',
    "<div class=mainForm style=\"margin-left: 30px; margin-right: 30px\"><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Name</th><th>Size</th><th>Created</th><th></th></tr></thead><tbody><tr dir-paginate=\"document in documents | itemsPerPage: documentsPaging.perPage\" current-page=documentsPaging.page total-items=documentsPaging.totalRecords ng-class-odd=\"'success'\" ng-class-even=\"'active'\" pagination-id=documentsPaginationId><td>{{ document.fileName }}</td><td>{{ document.size }}</td><td>{{ document.created | date:'MMM d, y HH:mm' }}</td><td><a href=\"{{ document.link }}\">Download</a></td></tr></tbody></table><div class=row><div class=\"col-xs-12 text-center\"><dir-pagination-controls on-page-change=\"documentsPageChanged(newPageNumber, searchValue)\" pagination-id=documentsPaginationId></dir-pagination-controls></div></div></div><div style=\"padding-bottom: 20px\"><button type=button class=\"btn btn-primary btn-sm pull-right\" ng-click=toggleDownloadSection()>{{documentsButtonLabel}}</button></div><div ng-show=showDownloadSection><file-upload></file-upload></div></div></div>"
  );


  $templateCache.put('edit-student',
    "<div class=mainForm><div class=\"row clearfix\"><form role=form name=studentUpdateForm><div class=\"col-md-6 column\"><div class=\"col-md-6 column\"><div class=form-group><label for=studentName>Name</label><input class=form-control id=studentName ng-model=studentEdit.name required></div><div class=form-group><label for=studentLastName>Last name</label><input type=tel class=form-control id=studentLastName ng-model=studentEdit.lastName required></div></div><div class=\"col-md-6 column\"><div class=form-group><label for=studentMobile>Mobile</label><input class=form-control id=studentMobile ng-model=\"studentEdit.mobile\"></div><div class=form-group><label for=studentPersonalNumber>Personal number</label><input class=form-control id=studentPersonalNumber ng-model=\"studentEdit.personId\"></div></div><div class=\"col-md-12 column\"><div class=form-group><label for=parentInformationInput>Parent information</label><textarea ng-model=studentEdit.parentsInfo class=\"form-control fixedTextArea\" id=parentInformationInput name=parentInformationInput></textarea></div><div class=form-group><label for=characteristicsInput>Characteristics</label><textarea ng-model=studentEdit.characteristics class=\"form-control fixedTextArea\" id=characteristicsInput name=characteristicsInput></textarea></div></div></div><div class=\"col-md-6 column\"><div class=\"col-md-12 column\"><div class=form-group><label for=studentMail>e-mail</label><input class=form-control id=studentMail ng-model=\"studentEdit.mail\"></div><div class=dropdown><label for=editStudentDatePicker>Birthdate</label><a class=dropdown-toggle id=editStudentDatePicker role=button data-toggle=dropdown data-target=# href=#><div class=input-group><input datepicker-popup=yyyy-MM-dd class=form-control data-ng-model=studentEdit.birthDateString placeholder=Birthdate> <span class=input-group-addon><i class=\"glyphicon glyphicon-calendar\"></i></span></div></a><ul class=dropdown-menu role=menu aria-labelledby=dLabel><datetimepicker data-ng-model=studentEdit.birthDateString data-datetimepicker-config=\"{ dropdownSelector: '#editStudentDatePicker' , startView:'day', minView:'day'}\"></datetimepicker></ul></div></div><div class=\"col-md-12 row\"><photo-upload photo-id=studentEdit.photoId photo-url=studentEdit.photoUrl></photo-upload></div></div><div ng-init=\"studentProcessingInProgress = false\" class=\"col-md-12 column\" style=\"margin-top: 10px; margin-left: 17px\"><button type=submit ng-disabled=studentProcessingInProgress class=\"btn btn-primary btn-sm\" ng-click=executeStudentUpdate(studentEdit)>Update</button></div></form></div></div>"
  );


  $templateCache.put('file-upload',
    "<div ng-if=uploader><input type=file nv-file-select uploader=uploader multiple></div><table class=table><thead><tr><th width=50%>Name</th><th ng-show=uploader.isHTML5>Size</th><th ng-show=uploader.isHTML5>Progress</th><th>Status</th><th>Actions</th></tr></thead><tbody><tr ng-repeat=\"item in uploader.queue\"><td><strong>{{ item.file.name }}</strong></td><td ng-show=uploader.isHTML5 nowrap>{{ item.file.size/1024/1024|number:2 }} MB</td><td ng-show=uploader.isHTML5><div class=progress style=\"margin-bottom: 0\"><div class=progress-bar role=progressbar ng-style=\"{ 'width': item.progress + '%' }\"></div></div></td><td class=text-center><span ng-show=item.isSuccess><i class=\"glyphicon glyphicon-ok\"></i></span> <span ng-show=item.isCancel><i class=\"glyphicon glyphicon-ban-circle\"></i></span> <span ng-show=item.isError><i class=\"glyphicon glyphicon-remove\"></i></span></td><td nowrap><button type=button class=\"btn btn-success btn-xs\" ng-click=item.upload() ng-disabled=\"item.isReady || item.isUploading || item.isSuccess\"><span class=\"glyphicon glyphicon-upload\"></span> Upload</button> <button type=button class=\"btn btn-warning btn-xs\" ng-click=item.cancel() ng-disabled=!item.isUploading><span class=\"glyphicon glyphicon-ban-circle\"></span> Cancel</button> <button type=button class=\"btn btn-danger btn-xs\" ng-click=item.remove()><span class=\"glyphicon glyphicon-trash\"></span> Remove</button></td></tr></tbody></table><div><div>Queue progress:<div class=progress><div class=progress-bar role=progressbar ng-style=\"{ 'width': uploader.progress + '%' }\"></div></div></div><button type=button class=\"btn btn-success btn-sm\" ng-click=uploader.uploadAll() ng-disabled=!uploader.getNotUploadedItems().length><span class=\"glyphicon glyphicon-upload\"></span> Upload All</button> <button type=button class=\"btn btn-warning btn-sm\" ng-click=uploader.cancelAll() ng-disabled=!uploader.isUploading><span class=\"glyphicon glyphicon-ban-circle\"></span> Cancel All</button> <button type=button class=\"btn btn-danger btn-sm\" ng-click=uploader.clearQueue() ng-disabled=!uploader.queue.length><span class=\"glyphicon glyphicon-trash\"></span> Remove All</button></div>"
  );


  $templateCache.put('student-identification-card',
    "<div><div class=form-horizontal><div><div class=\"col-md-6 column\"><div tooltip=\"{{selectedStudent.name }}\" class=tooltip-300max tooltip-enable=\"selectedStudent.name.length > 25\"><h4><b>{{ selectedStudent.name | limitTo: 25}}{{selectedStudent.name.length > 25 ? '...' : ''}}</b></h4></div><div tooltip=\"{{selectedStudent.lastName }}\" class=tooltip-300max tooltip-enable=\"selectedStudent.lastName.length > 25\"><h4><b>{{ selectedStudent.lastName | limitTo: 25}}{{selectedStudent.lastName.length > 25 ? '...' : ''}}</b></h4></div><div><h4>{{ selectedStudent.personId }}</h4></div></div><div class=\"col-md-6 column\"><img alt=140x140 width=140 height=140 src={{selectedStudent.photoUrl}} class=\"img-rounded pull-right\"></div></div><div class=\"col-md-12 column\" style=\"padding-top: 20px\"><table class=identification-card width=100%><tr><td>Phone number:</td><td>{{ selectedStudent.mobile }}</td></tr><tr><td>Current balance:</td><td>{{ selectedStudent.balance | number : 2}} EUR</td><td><button type=button class=\"btn btn-success btn-sm pull-right\" data-toggle=modal data-target=#addToBalanceModalView>Add to balance</button></td></tr></table></div><div ng-show=false class=\"col-md-12 column\" style=\"padding-top: 10px\"><button type=button class=\"btn btn-success btn-sm\" style=\"width: 100%\">Attendance history</button></div></div><balance-modal></balance-modal></div>"
  );


  $templateCache.put('students-list-header',
    "<div class=container-fluid><div class=row><div class=\"col-md-8 column\"><div class=input-group><span class=input-group-addon id=basic-addon1 ng-class=\"{'glyphicon glyphicon-refresh searchTextInput': basicSearch.spin , 'glyphicon glyphicon-search searchTextInput': !basicSearch.spin}\"></span> <input class=form-control placeholder=search ng-model=searchValue ng-keyup=executeSearch(searchValue)></div></div><div class=\"col-md-3 column\"><div class=\"btn-group btn-group-sm\" role=group><button type=button class=\"btn btn-default\" ng-class=\"{ active: studentPaging.perPage === 10}\" ng-click=setRecordsPerPage(10)>10</button> <button type=button class=\"btn btn-default\" ng-class=\"{ active: studentPaging.perPage === 25}\" ng-click=setRecordsPerPage(25)>25</button> <button type=button class=\"btn btn-default\" ng-class=\"{ active: studentPaging.perPage === 50}\" ng-click=setRecordsPerPage(50)>50</button></div></div><div class=col-md-1><h3><span class=\"glyphicon glyphicon-plus pull-right\" style=\"cursor: pointer;position: absolute; padding-top: 5px\" data-toggle=modal data-target=#addNewStudentModalView ng-click=resetNewStudent()></span></h3></div></div><new-student></new-student></div>"
  );


  $templateCache.put('students-list',
    "<div class=container-fluid style=\"padding-top: 20px\"><div ng-show=!studentsSearch.studentRecordsFound><div class=\"jumbotron well\"><h3>No students found!</h3><p>Change search query for result.</p></div></div><table ng-show=studentsSearch.studentRecordsFound class=\"table table-hover\" style=table-layout:fixed><thead><tr><th>Name</th><th class=text-center>Age</th><th class=text-center>ID</th><th class=text-center>Phone</th><th></th></tr></thead><tbody><tr dir-paginate=\"student in students | itemsPerPage: studentPaging.perPage\" current-page=studentPaging.page total-items=studentPaging.totalRecords ng-class-odd=\"'success'\" ng-class-even=\"'active'\" ng-click=setSelected(student.id) ng-class=\"{'missing-data-row': !student.mobile, 'selected-row': student.id === selectedStudent.id}\" pagination-id=studentsPaginationId><td style=\"width: 50%!important;word-wrap:break-word\"><div tooltip=\"{{student.fullName }}\" class=tooltip-300max tooltip-enable=\"student.fullName.length > 30\">{{ student.fullName | limitTo: 30}}{{student.fullName.length > 30 ? '...' : ''}}</div></td><td style=\"width: 10%!important\" class=text-center>{{ student.age }}</td><td style=\"width: 10%!important;word-wrap:break-word\" class=text-center>{{ student.personId }}</td><td style=\"width: 10%!important;word-wrap:break-word\" class=text-center>{{ student.mobile }}</td><td style=\"width: 10%!important\" class=text-center><button href=#information type=button class=\"btn btn-success btn-xs\" ng-click=openStudentDetailModal(student)>Details</button></td></tr></tbody></table><div class=row><div class=\"col-xs-12 text-center\"><dir-pagination-controls on-page-change=\"studentsPageChanged(newPageNumber, searchValue)\" pagination-id=studentsPaginationId></dir-pagination-controls></div></div><student-details></student-details></div>"
  );


  $templateCache.put('students',
    "<div class=mainForm ng-controller=StudentsController><div class=\"row clearfix\"><div ng-class=\"{'col-md-7 column' :  studentsSearch.studentRecordsFound, 'col-md-12 column' :  !studentsSearch.studentRecordsFound}\"><div class=\"panel panel-success\"><div class=\"panel-heading panel-success-override\">Students</div><div class=\"panel-body panel-body-override\"><div class=row><students-list-header></students-list-header></div><div class=row><students-list></students-list></div></div></div></div><div ng-show=studentsSearch.studentRecordsFound class=\"col-md-5 column\"><div class=row><div class=\"panel panel-success\"><div class=\"panel-heading panel-success-override\">Identification card</div><div class=\"panel-body panel-body-override\" ng-show=studentSelected><student-identification-card></student-identification-card></div></div></div><div class=row><div class=\"panel panel-success\"><div class=\"panel-heading panel-success-override\">Transactions history</div><div class=\"panel-body panel-body-override\" ng-show=studentSelected><div class=row><div class=col-md-12><h3>Under development</h3></div></div></div></div></div><div class=row><div class=\"panel panel-success\"><div class=\"panel-heading panel-success-override\">Latest files</div><div class=\"panel-body panel-body-override\" ng-show=studentSelected><div class=row><div class=col-md-12><h3>Under development</h3></div></div></div></div></div></div></div></div>"
  );


  $templateCache.put('new-student-modal',
    "<div app-modal id=addNewStudentModalView class=\"modal fade bs-example-modal-lg\" tabindex=-1 role=dialog aria-labelledby=myLargeModalLabel aria-hidden=true><div class=\"modal-dialog modal-lg\"><div class=\"modal-content modalViewPadding\"><div class=modal-header><button type=button class=close data-dismiss=modal aria-hidden=true><span class=\"glyphicon glyphicon-remove\"></span></button><h4 class=modal-title id=myModalLabel>Add new student</h4></div><div style=\"padding-top: 20px; padding-bottom: 10px\"><div class=\"row clearfix\"><form role=form name=studentCreatedForm><div class=\"col-md-6 column\"><div class=\"col-md-6 column\"><div class=form-group><label for=studentName>Name</label><input class=form-control id=studentName ng-model=newStudent.name required></div><div class=form-group><label for=studentLastName>Last name</label><input type=tel class=form-control id=studentLastName ng-model=newStudent.lastName required></div></div><div class=\"col-md-6 column\"><div class=form-group><label for=studentMobile>Mobile</label><input class=form-control id=studentMobile ng-model=\"newStudent.mobile\"></div><div class=form-group><label for=studentPersonalNumber>Personal number</label><input placeholder=123456-12345 class=form-control id=studentPersonalNumber ng-model=\"newStudent.personId\"></div></div><div class=\"col-md-12 column\"><div class=form-group><label for=parentInformationInput>Parent information</label><textarea ng-model=newStudent.parentsInfo class=\"form-control fixedTextArea\" id=parentInformationInput name=parentInformationInput></textarea></div><div class=form-group><label for=characteristicsInput>Characteristics</label><textarea ng-model=newStudent.characteristics class=\"form-control fixedTextArea\" id=characteristicsInput name=characteristicsInput></textarea></div></div></div><div class=\"col-md-6 column\"><div class=\"col-md-12 column\"><div class=form-group><label for=studentMail>e-mail</label><input class=form-control id=studentMail ng-model=\"newStudent.mail\"></div><div class=dropdown><label for=newStudentDatePicker>Birthdate</label><a class=dropdown-toggle id=newStudentDatePicker role=button data-toggle=dropdown data-target=# href=#><div class=input-group><input datepicker-popup=yyyy-MM-dd class=form-control data-ng-model=newStudent.birthDateString placeholder=Birthdate> <span class=input-group-addon><i class=\"glyphicon glyphicon-calendar\"></i></span></div></a><ul class=dropdown-menu role=menu aria-labelledby=dLabel><datetimepicker data-ng-model=newStudent.birthDateString data-datetimepicker-config=\"{ dropdownSelector: '#newStudentDatePicker' , startView:'day', minView:'day'}\"></datetimepicker></ul></div></div><div class=\"col-md-12 row\"><photo-upload photo-id=newStudent.photoId photo-url=newStudent.photoUrl></photo-upload></div></div><div ng-init=\"studentProcessingInProgress = false\" class=\"col-md-12 column\" style=\"padding-top: 10px\"><button type=submit ng-disabled=studentProcessingInProgress class=\"btn btn-success btn-sm\" ng-click=executeStudentSave(newStudent)>Save</button> <button type=reset ng-disabled=studentProcessingInProgress class=\"btn btn-primary btn-sm\" ng-click=executeReset()>Reset</button> <button type=reset ng-disabled=studentProcessingInProgress class=\"btn btn-warning btn-sm\" ng-click=executeCancel()>Cancel</button></div></form></div></div></div></div></div>"
  );


  $templateCache.put('photo-upload',
    "<div style=\"padding: 10px\"><div><div ng-if=uploader><input id=photoUploader ng-disabled=photoIsSelected type=file nv-file-select uploader=\"uploader\"></div></div><table class=table><thead><tr><th width=50%>Name</th><th ng-show=uploader.isHTML5>Size</th><th ng-show=uploader.isHTML5>Progress</th><th>Status</th><th>Actions</th></tr></thead><tbody><tr ng-repeat=\"item in uploader.queue\"><td><strong>{{ item.file.name }}</strong></td><td ng-show=uploader.isHTML5 nowrap>{{ item.file.size/1024/1024|number:2 }} MB</td><td ng-show=uploader.isHTML5><div class=progress style=\"margin-bottom: 0\"><div class=progress-bar role=progressbar ng-style=\"{ 'width': item.progress + '%' }\"></div></div></td><td class=text-center><span ng-show=item.isSuccess><i class=\"glyphicon glyphicon-ok\"></i></span> <span ng-show=item.isCancel><i class=\"glyphicon glyphicon-ban-circle\"></i></span> <span ng-show=item.isError><i class=\"glyphicon glyphicon-remove\"></i></span></td><td nowrap><button type=button class=\"btn btn-success btn-xs\" ng-click=item.upload() ng-disabled=\"item.isReady || item.isUploading || item.isSuccess\"><span class=\"glyphicon glyphicon-upload\"></span></button> <button type=button class=\"btn btn-warning btn-xs\" ng-click=item.cancel() ng-disabled=!item.isUploading><span class=\"glyphicon glyphicon-ban-circle\"></span></button> <button type=button class=\"btn btn-danger btn-xs\" ng-click=handleItemRemoval(item)><span class=\"glyphicon glyphicon-trash\"></span></button></td></tr></tbody></table></div><div ng-show=photoUrl><img alt=140x140 width=140 height=140 src={{photoUrl}} class=\"img-rounded text-center\"></div>"
  );


  $templateCache.put('confirm-event-finished-modal',
    "<div app-modal id=eventFinalizingConfirmationModal class=\"modal fade\" tabindex=-1 role=dialog aria-labelledby=mySmallModalLabel aria-hidden=true><div class=\"modal-dialog modal-sm\"><div class=\"modal-content modalViewPadding\" style=\"width: 300px !important\"><div class=row><div class=col-md-12><h4>Event confirmation:</h4><p>Please approve event is finalized.</p><p>{{selectedEvent.students}} student balance will be adjusted by {{selectedEvent.adjustedPrice | number: 2}} &euro;</p></div></div><div class=row><div class=\"col-md-12 column\" style=\"padding-top: 10px\"><button style=\"margin-left: 15px\" class=\"btn btn-success btn-sm pull-right\" type=button data-dismiss=modal>No</button> <button class=\"btn btn-warning btn-sm pull-right\" type=button ng-click=confirmEventIsFinished()>Yes</button></div></div></div></div></div>"
  );


  $templateCache.put('event-info',
    "<div class=\"panel panel-success\"><div class=\"panel-heading panel-success-override\">Event information</div><div class=\"panel-body panel-body-override container-fluid\"><div class=row><div class=col-md-12><div class=row><div class=col-md-8><table><tr><td><b>Subject name:</b></td><td style=\"padding-left: 10px\">{{selectedEvent.subject.subjectName}} ({{selectedEvent.eventId}})</td></tr><tr><td><b>Event date:</b></td><td style=\"padding-left: 10px\">{{selectedEvent.eventDate | date:'yyyy/MM/dd'}}</td></tr><tr><td><b>Time:</b></td><td style=\"padding-left: 10px\">{{selectedEvent.from | date:'HH:mm'}} - {{selectedEvent.to | date:'HH:mm'}}</td></tr><tr><td><b>Price:</b></td><td style=\"padding-left: 10px\">{{selectedEvent.adjustedPrice | number : 2}} &euro;</td></tr></table></div><div class=col-md-4><div class=pull-right ng-show=selectedEvent.loaded><div>Students <span class=badge>{{selectedEvent.students}}</span></div></div></div></div></div></div><div class=row><confirm-event-finished-modal></confirm-event-finished-modal><div class=col-md-12><button class=\"btn btn-success btn-sm pull-right\" style=\"margin-left: 15px\" type=button data-toggle=collapse data-target=#studentManageView aria-expanded=false aria-controls=studentManageView ng-click=processExpand()>Manage</button> <button ng-show=\"selectedEvent.currentStatus ==='PAST'\" class=\"btn btn-success btn-sm pull-right\" data-toggle=modal data-target=#eventFinalizingConfirmationModal type=button>Confirm finished</button></div></div><div class=collapse id=studentManageView style=\"margin-top: 15px\"><students-attendance-list></students-attendance-list></div></div></div>"
  );


  $templateCache.put('events-header',
    "<div class=container-fluid><form role=form name=newSubjectEventForm><div class=row><div class=\"col-md-8 column\"><div class=form-group id=subject-event-typeahead><div class=input-group id=subjectTypeAheadInput><span class=input-group-addon id=basic-addon2 ng-class=\"{'glyphicon glyphicon-ok searchTextInput': !selectedSubject , 'glyphicon glyphicon-pencil searchTextInput': !!selectedSubject}\"></span><div id=scrollable-dropdown-menu><input required class=\"form-control typeahead tt-query\" placeholder=Subject ng-model=subjectEvent.subjectName ng-keyup=updateSelectedSubject()></div></div></div></div><div class=\"col-md-4 column\"><div class=form-group><div class=input-group><span class=input-group-addon>&euro;</span> <input required id=amount class=\"form-control ng-valid ng-valid-min ng-dirty ng-valid-number\" money=\"\" ng-model=subjectEvent.amount autofocus placeholder=Amount precision=2></div></div></div></div><div class=row><div class=\"col-md-4 column\"><div class=form-group><div class=dropdown><a class=dropdown-toggle id=subjectEventDateId role=button data-toggle=dropdown data-target=# href=#><div class=input-group><input required datepicker-popup=yyyy-MM-dd class=form-control data-ng-model=subjectEvent.eventDate placeholder=\"Event date\"> <span class=input-group-addon><i class=\"glyphicon glyphicon-calendar\"></i></span></div></a><ul class=dropdown-menu role=menu aria-labelledby=dLabel><datetimepicker data-ng-model=subjectEvent.eventDate data-before-render=\"renderDatePicker($view, $dates, $leftDate, $upDate, $rightDate)\" data-datetimepicker-config=\"{ dropdownSelector: '#subjectEventDateId' , startView:'day', minView:'day'}\"></datetimepicker></ul></div></div></div><div class=\"col-md-4 column\"><div class=form-group><div class=dropdown><a class=dropdown-toggle id=subjectEventTimeFromId role=button data-toggle=dropdown data-target=# href=#><div class=input-group><input required datepicker-popup=HH:mm class=form-control data-ng-model=subjectEvent.eventTimeFrom placeholder=\"Time from\"> <span class=input-group-addon><i class=\"glyphicon glyphicon-time\"></i></span></div></a><ul class=dropdown-menu role=menu aria-labelledby=dLabel><datetimepicker data-ng-model=subjectEvent.eventTimeFrom data-before-render=\"renderTimePicker($view, $dates, $leftDate, $upDate, $rightDate)\" data-datetimepicker-config=\"{ dropdownSelector: '#subjectEventTimeFromId' , startView:'hour', minView:'minute'}\"></datetimepicker></ul></div></div></div><div class=\"col-md-4 column\"><div class=form-group><div class=dropdown><a class=dropdown-toggle id=subjectEventTimeToId role=button data-toggle=dropdown data-target=# href=#><div class=input-group><input required datepicker-popup=HH:mm class=form-control data-ng-model=subjectEvent.eventTimeTo placeholder=\"Time till\"> <span class=input-group-addon><i class=\"glyphicon glyphicon-time\"></i></span></div></a><ul class=dropdown-menu role=menu aria-labelledby=dLabel><datetimepicker data-ng-model=subjectEvent.eventTimeTo data-before-render=\"renderTimePicker($view, $dates, $leftDate, $upDate, $rightDate)\" data-datetimepicker-config=\"{ dropdownSelector: '#subjectEventTimeToId' , startView:'hour', minView:'minute', minuteStep: 10}\"></datetimepicker></ul></div></div></div></div><div class=row><div class=\"col-md-3 column\"><div class=form-group><button style=\"width: 100%\" type=submit class=\"btn btn-success btn-sm\" ng-click=saveNewEvent(subjectEvent)>Save</button></div></div></div></form></div>"
  );


  $templateCache.put('events-list',
    "<div class=container-fluid><div id=events_list_contaner style=\"height:400px;overflow: auto\"><div ng-show=!events.eventRecordsFound><div class=\"jumbotron well\"><h3>No events found!</h3><p>Please register new event.</p></div></div><table ng-show=events.eventRecordsFound class=\"table table-hover\"><thead><tr><th class=text-center>Name</th><th class=text-center>Date</th><th class=text-center>From</th><th class=text-center>To</th><th></th></tr></thead><tbody><div infinite-scroll=loadMoreEvens(false) infinite-scroll-distance=3 infinite-scroll-container=\"'#events_list_contaner'\"><tr ng-repeat=\"event in events.values\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\" ng-class=\"{'selected-row': event.eventId === selectedEvent.eventId}\" ng-click=setSelected(event)><td>{{ event.subject.subjectName }}</td><td class=text-center>{{ event.eventDate | date:'yyyy/MM/dd'}}</td><td class=text-center>{{ event.from | date:'HH:mm'}}</td><td class=text-center>{{ event.to | date:'HH:mm'}}</td><td class=text-center width=40px><span><i ng-class=\"{'glyphicon glyphicon-chevron-up event_future': event.currentStatus === 'FUTURE' , 'glyphicon glyphicon-chevron-down event_past': event.currentStatus === 'PAST'  , 'glyphicon glyphicon-thumbs-up event_confirmed': event.currentStatus === 'CONFIRMED'}\"></i></span></td></tr></div></tbody></table></div></div>"
  );


  $templateCache.put('subjects',
    "<div class=mainForm ng-controller=SubjectsController><div class=\"row clearfix\"><div class=\"col-md-6 column\"><div class=\"panel panel-success\"><div class=\"panel-heading panel-success-override\">Events list</div><div class=\"panel-body panel-body-override\"><div class=row><events-header></events-header></div><div class=row><events-list></events-list></div></div></div></div><div class=\"col-md-6 column\"><event-info></event-info></div></div></div>"
  );


  $templateCache.put('students-attendance-list',
    "<div style=height:418px><div class=row><div class=col-md-8><div class=input-group><span class=input-group-addon id=basic-addon1 ng-class=\"{'glyphicon glyphicon-refresh searchTextInput': eventStudentsSearch.spin , 'glyphicon glyphicon-search searchTextInput': !eventStudentsSearch.spin}\"></span> <input class=form-control placeholder=search ng-model=eventStudentsSearch.searchValue ng-keyup=executeSearch()></div></div><div class=col-md-4><div class=\"dropdown pull-right\" style=\"width: 100% !important\"><select id=formatId class=form-control ng-model=eventStudentsSearch.format ng-options=\"format for format in eventStudentsSearch.formats\" ng-change=formatChanged()></select></div></div></div><div class=row><div class=col-md-12><div id=events_students_contaner style=\"height:370px;overflow: auto;margin-top: 10px\"><div ng-show=!eventStudentsSearch.studentRecordsFound><div class=\"jumbotron well\"><h3>No students found!</h3><p>Please select another event or change search filter.</p></div></div><table ng-show=eventStudentsSearch.studentRecordsFound class=\"table table-hover\"><thead><tr><th>Name</th><th class=text-center>ID</th><th class=text-center>Registered</th><th class=text-center ng-show=selectedEvent.havePassed>Participated</th></tr></thead><tbody><div infinite-scroll=loadMoreStudents() nfinite-scroll-distance=4 infinite-scroll-container=\"'#events_students_contaner'\"><tr ng-repeat=\"student in eventStudentsSearch.values\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td style=width:30%><div tooltip={{student.fullName}} tooltip-enable=\"student.fullName.length > 25\" class=tooltip-300max>{{ student.fullName | limitTo: 25}}{{student.fullName.length > 25 ? '...' : ''}}</div></td><td class=text-center>{{ student.personId}}</td><td style=width:50px class=text-center><input ng-disabled=\"selectedEvent.status ==='FINALIZED'\" type=checkbox name=checkboxes id=registeredStudentId ng-checked=student.active ng-click=\"updateStudentAttendance(student)\"></td><td style=width:50px ng-show=selectedEvent.havePassed><div ng-show=student.showAbsenceToggle toggle-switch ng-init=\"student.updateInProgress = false\" is-disabled=\"student.updateInProgress || selectedEvent.status ==='FINALIZED'\" class=\"switch-success switch-mini\" on-label=Yes off-label=No knob-label=\"\" ng-model=student.participated ng-change=onStudentAbsenceEvent(student)></div></td></tr></div></tbody></table></div></div></div></div>"
  );

}]);
