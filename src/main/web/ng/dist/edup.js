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

    .directive('edupToggle', function ($timeout) {
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
            controller: function ($scope) {

            },

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
    }
);

'use strict';

angular.module('edup.common')

    .directive('windowSize', function ($document, $window) {
        return {
            restrict: 'A',
            link: function (scope) {
                var windowHeight = $window.innerHeight || $document.body.clientHeight;
                var headerHeight = $('#edupHeader').height();
                scope.viewScrollPaneHeight = windowHeight - headerHeight - 40;
                console.log(scope.viewScrollPaneHeight);
            }
        };
    }
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

    .config(function (paginationTemplateProvider) {

        var location = window.location.hostname;

        var baseUrl;

        if (location.indexOf('127.0.0.1') > -1) {
            baseUrl = 'http://127.0.0.1:8088/';
        } else {
            baseUrl = 'https://' + location + ':8443/edup/ng';
        }

        paginationTemplateProvider.setPath(baseUrl + '/vendor/bower_components/angular-utils-pagination/dirPagination.tpl.html');
    }
);
'use strict';

angular.module('edup.common').run(function (Restangular, UrlService, NotificationService) {

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

});

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

    .service('RestService', function (Restangular) {

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

    }
);
'use strict';

angular.module('edup.common')

	.service('TypeAheadService', function (UrlService) {

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
	}
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
			Subjects: baseUrl + '/api/private/subjects'
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

            controller: function ($scope, $window, RestService, UrlService, NotificationService) {
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
            },

            link: function () {

            }
        };
    }
);

'use strict';

angular.module('edup.header', ['ui.router']);
'use strict';

angular.module('edup.header')

	.controller('NavbarController', function ($scope, $state, RestService) {

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

	}
);
'use strict';

angular.module('edup.header')

    .directive('edupHeader', function () {
        return {
            restrict: 'E',
            templateUrl: 'edup-header',

            controller: function ($scope, $window, $location, RestService, UrlService) {

                $scope.isActive = function (viewLocation) {
                    return viewLocation === $location.path();
                };

                $scope.downloadReport = function () {
                };

                $scope.logoutUser = function () {
                    RestService.Private.LogOut.post().then(function () {
                        $window.location.href = UrlService.BaseUrl;
                    });
                };
            },

            link: function () {

            }

        };
    }
);
'use strict';

angular.module('edup.footer', []);
'use strict';

angular.module('edup.footer')

    .controller('FooterController', function ($scope, $location, Restangular) {
        Restangular.one('ping').get().then(
            function (result) {
                $scope.appVersion = result.version;
            }
        );
    }
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

    .controller('CalendarController', function ($scope, $modal, moment, calendarTitle) {

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
                controller: function ($scope, $modalInstance) {
                    $scope.$modalInstance = $modalInstance;
                    $scope.action = action;
                    $scope.event = event;
                }
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

    }
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

            controller: function ($scope, RestService, NotificationService) {

                $scope.updateStudentBalance = function(balance, student) {
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
            },

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

	.directive('balanceModal', function (RestService, NotificationService) {
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
	}
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
'use strict';

angular.module('edup.students')

	.directive('editStudent', function () {
		return {
			restrict: 'E',
			templateUrl: 'edit-student',
			controller: function ($scope, RestService, NotificationService, PaginationService) {

				$scope.executeStudentUpdate = function (student) {
					if ($scope.studentProcessingInProgress) {
						return;
					}

					$scope.studentProcessingInProgress = true;

					student.id = $scope.selectedStudent.id;
					student.versionId = $scope.selectedStudent.versionId;
					student.birthDate = new Date(student.birthDateString);
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

			},
			link: function ($scope) {

			}
		};
	}
);
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
	}
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

	.controller('StudentsController', function ($scope, $timeout, $filter, RestService, PaginationService, QueryService) {

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

	}
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

			controller: function ($scope, RestService, PaginationService, NotificationService) {
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

			},

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
'use strict';

angular.module('edup.subjects', [

]);
'use strict';

angular.module('edup.subjects')

	.directive('eventInfo', function () {
		return {
			restrict: 'E',
			templateUrl: 'event-info',
			controller: function ($scope, moment, RestService, QueryService) {

				$scope.studentsManagemnt = {
					expanded: false
				};

				$scope.selectedEvent = {
					loaded: false
				};

				$scope.loadEventDetails = function (eventId) {
					if (eventId) {

						RestService.Private.Subjects.one('events').one(eventId.toString()).get().then(function (response) {
							if (response.payload) {
								$scope.selectedEvent = response.payload;
								$scope.selectedEvent.loaded = true;
								$scope.selectedEvent.adjustedPrice = $scope.selectedEvent.price / 100;
								$scope.selectedEvent.havePassed = moment().isAfter($scope.selectedEvent.eventDate);

								$scope.resetEventStudentsSearch($scope.selectedEvent.havePassed);

								$scope.loadAttendance(eventId);
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

			},
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
			controller: function ($scope, $timeout, $filter, moment, QueryService, RestService, TypeAheadService, NotificationService) {

				$scope.subjects = [];
				$scope.selectedSubject = null;

				var dismiss = function () {
					var view = $('#addNewSubjectEventModal');
					if (view) {
						view.modal('hide');
					}
				};

				var selectSubject = function (selectedSubjectName) {
					$scope.selectedSubject = _.find(TypeAheadService.DataSet(), function (subject) {
						return subject.subjectName === selectedSubjectName;
					});
					if (!!$scope.selectedSubject) {
						$scope.subjectEvent.subjectName = $scope.selectedSubject.subjectName;
					}
				};

				var typeAhead = TypeAheadService.Build();

				var $bloodhound = $('#bloodhound .typeahead');

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

			},
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
			controller: function ($scope, $timeout, QueryService, RestService) {

				$scope.events = {
					values: [],
					total: 0,
					loading: false,
					firstLoad : true,
					eventRecordsFound: true
				};

				$scope.setSelected = function (event) {
					$scope.events.firstLoad = false;
					$scope.loadEventDetails(event.eventId);
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
								});

								$scope.events.total = response.count;
								$scope.events.loading = false;

								$scope.events.eventRecordsFound = response.count !== 0;
							});
						}, 300);
					}
				};

			},
			link: function (scope) {

			}
		};
	}
);
'use strict';

angular.module('edup.subjects')

    .controller('SubjectsController', function ($scope) {

        $scope.subjectsView1 = 'subjectsView1';
        $scope.subjectsView2 = 'subjectsView2';

    }
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

			controller: function ($scope, $timeout, QueryService, RestService) {

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
					console.log(student.participated);
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
								//_.forEach($scope.eventStudentsSearch.attendance, function (attendance) {
								//	if (student.id === attendance.studentId) {
								//		attendance.participated = student.participated;
								//	}
								//});
							}, function () {
								student.updateInProgress = false;
							});
					}
				};

			},

			link: function (scope) {

			}
		};
	}
)
;
'use strict';

angular.module('edup.tabs', [
	'ngSanitize',
	'toggle-switch',
	'ui.bootstrap.datetimepicker',
	'infinite-scroll',
	'angularFileUpload',
	'fiestah.money',
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

    .config(function ($stateProvider) {

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

    }
);