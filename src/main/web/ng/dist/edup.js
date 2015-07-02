'use strict';

angular.module('edup.common', [
    'restangular',
    'angularUtils.directives.dirPagination',
    'mgcrea.ngStrap'
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

    .directive('datePicker', ['$timeout', '$filter', function ($timeout, $filter) {
        return {
            restrict: 'E',
            templateUrl: 'date-picker',
            scope: {
                inputField: '=',
                label: '@',
                datePickerId: '@'
            },
            controller: ['$scope', function ($scope) {

            }],

            link: function (scope) {
                $timeout(function () {
                    var datePicker = $('#' + scope.datePickerId);

                    datePicker.datetimepicker({
                        viewMode: 'years',
                        format: 'YYYY-MM-DD'
                    });

                    datePicker.on('dp.change', function (event) {
                        scope.inputField = $filter('date')(new Date(event.date), 'yyyy-MM-dd');

                    });
                });
            }
        };
    }]
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
    });
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
                msg += error.message + '\n';
            });
        } else {
            msg = 'Failed on: ' + resp.config.method + ' to: ' + resp.config.url;
        }
        NotificationService.Error(msg);
        console.log(angular.toJson(resp, true));
        return false;
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

    .service('QueryService', function () {

        var prepareQuery = function (top, skip, search, orderBy, filters) {
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
                LogOut: privateResource.one('logout')
            },
            Public: {
                Login: publicResource.one('login')
            }

        };

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

    .controller('NavbarController', ['$scope', '$state', function ($scope, $state) {
        $scope.calendarModel = {
            items: ['Students', 'Calendar'],
            states: ['students', 'calendar'],
            current: 0
        };

        $scope.$watch(function () {
            return $scope.calendarModel.current;
        }, function (index) {
            $state.go($scope.calendarModel.states[index]);
        });

    }]
);
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

                $scope.downloadReport = function () {
                    window.open('https://172.20.10.4:8443/edup/api/private/reports');
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

                $scope.updateStudentBalance = function(balance, student) {
                    if (balance && balance.amount) {
                        var balanceDto = {
                            studentId: student.id,
                            amount: balance.amount * 100,
                            comments: balance.comment
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

            controller: ['$scope', function ($scope) {
                $scope.attendanceHistory = [
                    {
                        'subject' : 'Math',
                        'date' : '2015/03/03',
                        'amount' : 15
                    },
                    {
                        'subject' : 'Literature',
                        'date' : '2015/03/03',
                        'amount' : 15
                    },
                    {
                        'subject' : 'Sport',
                        'date' : '2015/03/04',
                        'amount' : 20
                    },
                    {
                        'subject' : 'History',
                        'date' : '2015/03/04',
                        'amount' : 10
                    },
                    {
                        'subject' : 'Math',
                        'date' : '2015/03/05',
                        'amount' : 15
                    },
                    {
                        'subject' : 'English',
                        'date' : '2015/03/07',
                        'amount' : 15
                    },
                    {
                        'subject' : 'Sport',
                        'date' : '2015/03/07',
                        'amount' : 20
                    }
                ];
            }],

            link : function ($scope) {
               
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
                scope.price = {
                    amount: 0
                };

                scope.saving = function (balance) {
                    if (balance && balance.amount) {
                        var balanceDto = {
                            studentId: scope.selectedStudent.id,
                            amount: balance.amount * 100,
                            comments: balance.comment
                        };

                        RestService.Private.Balance.customPOST(balanceDto).then(function (response) {
                            var recordId = response.payload;
                            if (recordId) {
                                scope.selectedStudent.balance += balance.amount;
                                scope.balance = null;
                                scope.dismissModal();
                                NotificationService.Success(balance.amount + ' EUR was added to ' + scope.selectedStudent.name + ' ' + scope.selectedStudent.lastName + ' student!');
                            }
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


                //$scope.documents = [
                //    {
                //        'name': 'report.docx',
                //        'date': '2015/03/03',
                //        'link': 'https:/localhost:8443/edup/api/file/report.docx'
                //    },
                //    {
                //        'name': 'balance.xls',
                //        'date': '2015/03/04',
                //        'link': 'https:/localhost:8443/edup/api/file/balance.xls'
                //    },
                //    {
                //        'name': 'photo.jpg',
                //        'date': '2015/03/04',
                //        'link': 'https:/localhost:8443/edup/api/file/photo.jpg'
                //    }
                //];
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
            controller: ['$scope', 'RestService', 'NotificationService', 'PaginationService', function($scope, RestService, NotificationService, PaginationService) {

                $scope.executeStudentUpdate = function (student) {
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
                            });
                    }
                };

            }],
            link : function ($scope) {

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
                $scope.studentPaging.totalRecords = result.count;
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

        function isEmpty(str) {
            return (!str || 0 === str.length);
        }

        var previousSearch = '';

        $scope.executeSearch = function (searchValue) {
            if (searchValue && searchValue.length > 2) {
                $timeout(function () {
                    $scope.studentPaging.page = 1;
                    $scope.loadStudents(null, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), searchValue);
                    previousSearch = searchValue;
                }, 300);
            } else if (isEmpty(searchValue) && previousSearch !== searchValue) {
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
                var dismiss = function() {
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
                    if (student && student.name && student.lastName) {
                        student.birthDate = new Date(student.birthDateString);
                        RestService.Private.Students.customPOST(student).then(function (response) {
                            NotificationService.Success('Student ' + student.name + ' ' + student.lastName + ' created!');
                            $scope.loadStudents(response.payload, PaginationService.Top($scope.studentPaging), PaginationService.Skip($scope.studentPaging), null);
                            $scope.resetNewStudent();
                            dismiss();
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
                scope.dismissNewStudentModalDialog = function() {
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

                scope.$on('clearFileUploadQueue', function (event, args) {
                    scope.uploader.clearQueue();
                    scope.photoUploaded = false;
                });

                //scope.uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
                //    console.info('onWhenAddingFileFailed', item, filter, options);
                //};
                //scope.uploader.onAfterAddingFile = function(fileItem) {
                //    console.info('onAfterAddingFile', fileItem);
                //};
                //scope.uploader.onAfterAddingAll = function(addedFileItems) {
                //    console.info('onAfterAddingAll', addedFileItems);
                //};
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
                    }, 500);
                };
                scope.uploader.onErrorItem = function (fileItem, response, status, headers) {
                    NotificationService.Error('Failed to upload student photo!');
                };
                //scope. uploader.onCancelItem = function(fileItem, response, status, headers) {
                //    console.info('onCancelItem', fileItem, response, status, headers);
                //};
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

angular.module('edup.tabs', [
    'angularFileUpload',
    'fiestah.money',
    'edup.calendar',
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
            .state('calendar', {
                templateUrl: 'calendar',
                url: '/calendar',
                controller: 'CalendarController'
            }
        );

    }]
);angular.module('edup').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('date-picker',
    "<div><label for={{datePickerId}}>{{label}}</label><div class=\"input-group date\" id={{datePickerId}} ng-click=openDatePicker()><input ng-model=inputField class=form-control name=\"date\"> <span class=input-group-addon><span class=\"glyphicon glyphicon-calendar\"></span></span></div></div>"
  );


  $templateCache.put('edup-toggle',
    "<input id={{toggleId}} type=checkbox data-toggle=toggle data-on={{onLabel}} data-off={{offLabel}} data-width=100 data-size=small>"
  );


  $templateCache.put('edup-footer',
    "<div ng-controller=FooterController><nav class=\"navbar navbar-default\" style=\"margin-bottom: 0px !important; margin-top: auto !important\"><div class=\"navbar-text text-center\" style=\"width: 100%\">Application version: {{ appVersion }}</div></nav></div>"
  );


  $templateCache.put('edup-header',
    "<div><nav class=\"navbar navbar-default\"><div class=container-fluid><div class=navbar-header><button type=button class=\"navbar-toggle collapsed\" data-toggle=collapse data-target=#bs-example-navbar-collapse-1><span class=sr-only>Toggle navigation</span> <span class=icon-bar></span> <span class=icon-bar></span> <span class=icon-bar></span></button> <a class=navbar-brand href=\"\">Educational planning application</a></div><div class=\"collapse navbar-collapse\" id=bs-example-navbar-collapse-1><ul class=\"nav navbar-nav\"><li ng-class=\"{ active: isActive('/students')}\"><a href=#students>Students<span class=sr-only>(current)</span></a></li><li ng-class=\"{ active: isActive('/calendar')}\"><a href=#calendar>Calendar</a></li><li class=dropdown><a href=#report class=dropdown-toggle data-toggle=dropdown role=button aria-expanded=false>Reports<span class=caret></span></a><ul class=dropdown-menu role=menu><li ng-click=downloadReport()><a href=#>Visiting Journal</a></li><li><a href=#>Another action</a></li><li><a href=#>Something else here</a></li><li class=divider></li><li><a href=#>Separated link</a></li><li class=divider></li><li><a href=#>One more separated link</a></li></ul></li></ul><ul class=\"nav navbar-nav navbar-right\"><li><a href=# target=_self ng-click=logoutUser()>Log out</a></li></ul></div></div></nav></div>"
  );


  $templateCache.put('edup-login',
    "<div class=login-container><div id=output></div><div class=avatar></div><div class=form-box><form name=form method=post><input name=j_username placeholder=username id=text ng-model=username required> <input type=password name=j_password placeholder=password id=password ng-model=password required> <button class=\"btn btn-info btn-block login\" type=submit ng-click=\"submitLogin(username, password)\">Login</button></form></div></div>"
  );


  $templateCache.put('calendar',
    "<div class=mainForm ng-controller=CalendarController><div class=\"col-md-12 column\"><div class=\"col-md-3 form-inline\"><div class=\"btn-group pull-left\"><button class=\"btn btn-primary btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay decrement=calendarView>&lt;&lt; Prev</button> <button class=\"btn btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay set-to-today>Today</button> <button class=\"btn btn-primary btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay increment=calendarView>Next &gt;&gt;</button></div></div><div class=col-md-2><div class=btn-group><h4>{{ currentDay }}</h4></div></div><div class=\"col-md-3 form-inline\"><div class=\"btn-group pull-right\"><button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('year')\">Year</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('month')\">Month</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('week')\">Week</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('day')\">Day</button></div></div><div class=\"col-md-4 form-inline\"><h4>Today events:</h4></div></div><div class=\"col-md-12 column\" style=\"padding-top: 20px\"><div class=\"col-md-8 column\"><mwl-calendar events=events view=calendarView view-title=calendarTitle current-day=calendarDay on-event-click=eventClicked(calendarEvent) edit-event-html=\"'<i class=\\'glyphicon glyphicon-pencil\\'></i>'\" delete-event-html=\"'<i class=\\'glyphicon glyphicon-remove\\'></i>'\" on-edit-event-click=eventEdited(calendarEvent) on-delete-event-click=eventDeleted(calendarEvent)></mwl-calendar></div><div class=\"col-md-4 column\"><div ng-repeat=\"event in events\"><h4><div class=label ng-class=\"{ 'label-primary': event.type === 'info', 'label-warning': event.type === 'warning', 'label-danger': event.type === 'important'}\" data-toggle=tooltip data-placement=top title=\"Tooltip on top\" tooltip=\"{{ event.title }}\" tooltip-trigger=\"{{{true: 'mouseenter', false: 'never'}[myForm.username.$invalid]}}\">({{event.startsAt| date:'HH:mm'}} - {{event.endsAt | date:'HH:mm'}}) {{event.title | limitTo: 33}} {{event.title.length > 33 ? '...' : ''}}</div></h4></div></div></div></div>"
  );


  $templateCache.put('student-accounting',
    "<div class=mainForm style=\"margin-left: 30px; margin-right: 30px\"><div class=\"row clearfix\"><div class=\"col-md-12 column\"><form role=form name=studentBalanceUpdate><div class=form-group><label for=amount>Enter a monetary amount:</label><div class=input-group><span class=input-group-addon>&euro;</span> <input id=amount class=\"form-control ng-valid ng-valid-min ng-dirty ng-valid-number\" money=\"\" ng-model=balance.amount autofocus precision=2></div></div><div class=form-group><label for=comments>Comments</label><textarea ng-model=balance.comment class=\"form-control fixedTextArea\" id=comments name=comments></textarea></div><div class=\"form-group text-center\"><button ng-click=\"updateStudentBalance(balance, selectedStudent)\" id=addToBalanceButton class=\"btn btn-success btn-sm\">Save</button> <button ng-click=resetValue() id=clearBalanceUpdate type=reset class=\"btn btn-primary btn-sm\">Reset</button></div></form></div></div></div>"
  );


  $templateCache.put('student-attendance',
    "<div class=mainForm style=\"margin-left: 30px; margin-right: 30px\"><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Subject name</th><th>Date</th><th>Amount</th></tr></thead><tbody><tr ng-repeat=\"event in attendanceHistory\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td>{{ event.subject }}</td><td>{{ event.date }}</td><td>{{ event.amount }} EUR</td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div><div class=\"row clearfix\"><div class=\"col-md-12 column\"><button type=button class=\"btn btn-success pull-right\">Add new attendance</button></div></div></div></div>"
  );


  $templateCache.put('balance-modal',
    "<div app-modal id=addToBalanceModalView class=\"modal fade bs-example-modal-sm\" tabindex=-1 role=dialog aria-labelledby=mySmallModalLabel aria-hidden=true><div class=\"modal-dialog modal-sm\"><div class=\"modal-content modalViewPadding\"><form class=form-horizontal><fieldset><legend>Add money to account</legend><form class=\"ng-valid ng-dirty\"><div class=form-group><label for=amount>Enter a monetary amount:</label><div class=input-group><span class=input-group-addon>&euro;</span> <input id=amount class=\"form-control ng-valid ng-valid-min ng-dirty ng-valid-number\" money=\"\" ng-model=balance.amount autofocus precision=2></div></div><div class=form-group><label for=comments>Comments</label><textarea ng-model=balance.comment class=\"form-control fixedTextArea\" id=comments name=comments></textarea></div></form><div class=\"form-group text-center\"><button ng-click=saving(balance) id=addToBalanceButton class=\"btn btn-success btn-sm\">Save</button> <button ng-click=resetValue() id=clearBalanceUpdate type=reset class=\"btn btn-primary btn-sm\">Reset</button> <button id=cancelBalanceUpdate class=\"btn btn-warning btn-sm\" data-dismiss=modal ng-click=\"balance = null\">Cancel</button></div></fieldset></form></div></div></div>"
  );


  $templateCache.put('student-details',
    "<div app-modal id=student-details-modal-view class=\"modal fade bs-example-modal-lg\" tabindex=-1 role=dialog aria-labelledby=myLargeModalLabel aria-hidden=true><div class=\"modal-dialog modal-lg\"><div class=\"modal-content modalViewPadding\"><div class=modal-header style=\"padding: 5px !important;border-bottom: none !important\"><div class=\"parent modal-title\" id=myModalLabel><div class=\"child pull=left\"><h4>{{selectedStudent.name}} {{selectedStudent.lastName}}</h4></div><div class=\"child pull-right\"><h4 class=pull-right><span class=label style=\"font-weight: 200 !important\" ng-class=\"{'label-danger' :  selectedStudent.balance < 0, 'label-success' :  selectedStudent.balance > 0, 'label-primary' :  selectedStudent.balance == 0}\">{{ selectedStudent.balance | number : 2}} EUR</span></h4></div></div></div><div role=tabpanel><ul class=\"nav nav-tabs\" role=tablist style=\"border: none !important\"><li role=presentation class=active ng-class=\"{ active: isActive('/information')}\"><a href=#information aria-controls=home role=tab data-toggle=tab>Information</a></li><li role=presentation ng-class=\"{ active: isActive('/accounting')}\"><a href=#accounting aria-controls=profile role=tab data-toggle=tab>Accounting</a></li><li role=presentation ng-class=\"{ active: isActive('/documents')}\" ng-click=refreshDocumentsList()><a href=#documents aria-controls=messages role=tab data-toggle=tab>Documents</a></li><li role=presentation ng-class=\"{ active: isActive('/attendance')}\"><a href=#attendance aria-controls=settings role=tab data-toggle=tab>Attendance</a></li></ul><div class=tab-content style=\"margin-bottom: 10px\"><div role=tabpanel class=\"tab-pane active first-tab\" id=information><edit-student></edit-student></div><div role=tabpanel class=\"tab-pane next-tab\" id=accounting><student-accounting></student-accounting></div><div role=tabpanel class=\"tab-pane next-tab\" id=documents><student-documents></student-documents></div><div role=tabpanel class=\"tab-pane next-tab\" id=attendance><student-attendance></student-attendance></div></div></div></div></div></div>"
  );


  $templateCache.put('student-documents',
    "<div class=mainForm style=\"margin-left: 30px; margin-right: 30px\"><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Name</th><th>Size</th><th>Created</th><th></th></tr></thead><tbody><tr dir-paginate=\"document in documents | itemsPerPage: documentsPaging.perPage\" current-page=documentsPaging.page total-items=documentsPaging.totalRecords ng-class-odd=\"'success'\" ng-class-even=\"'active'\" pagination-id=documentsPaginationId><td>{{ document.fileName }}</td><td>{{ document.size }}</td><td>{{ document.created | date }}</td><td><a href=\"{{ document.link }}\">Download</a></td></tr></tbody></table><div class=row><div class=\"col-xs-12 text-center\"><dir-pagination-controls on-page-change=\"documentsPageChanged(newPageNumber, searchValue)\" pagination-id=documentsPaginationId></dir-pagination-controls></div></div></div><div style=\"padding-bottom: 20px\"><button type=button class=\"btn btn-primary btn-sm pull-right\" ng-click=toggleDownloadSection()>{{documentsButtonLabel}}</button></div><div ng-show=showDownloadSection><file-upload></file-upload></div></div></div>"
  );


  $templateCache.put('edit-student',
    "<div class=mainForm><div class=\"row clearfix\"><form role=form name=studentUpdateForm><div class=\"col-md-6 column\"><div class=\"col-md-6 column\"><div class=form-group><label for=studentName>Name</label><input class=form-control id=studentName ng-model=studentEdit.name required></div><div class=form-group><label for=studentLastName>Last name</label><input type=tel class=form-control id=studentLastName ng-model=studentEdit.lastName required></div></div><div class=\"col-md-6 column\"><div class=form-group><label for=studentMobile>Mobile</label><input class=form-control id=studentMobile ng-model=\"studentEdit.mobile\"></div><div class=form-group><label for=studentPersonalNumber>Personal number</label><input class=form-control id=studentPersonalNumber ng-model=\"studentEdit.personId\"></div></div><div class=\"col-md-12 column\"><div class=form-group><label for=parentInformationInput>Parent information</label><textarea ng-model=studentEdit.parentsInfo class=\"form-control fixedTextArea\" id=parentInformationInput name=parentInformationInput></textarea></div><div class=form-group><label for=characteristicsInput>Characteristics</label><textarea ng-model=studentEdit.characteristics class=\"form-control fixedTextArea\" id=characteristicsInput name=characteristicsInput></textarea></div></div></div><div class=\"col-md-6 column\"><div class=\"col-md-12 column\"><div class=form-group><label for=studentMail>e-mail</label><input class=form-control id=studentMail ng-model=\"studentEdit.mail\"></div><div class=form-group ng-class=\"{'has-error': studentCreatedForm.date.$invalid}\"><date-picker date-picker-id=editStudentDatePicker input-field=studentEdit.birthDateString label=Birthday></date-picker></div></div><div class=\"col-md-12 row\"><photo-upload photo-id=studentEdit.photoId photo-url=studentEdit.photoUrl></photo-upload></div></div><div class=\"col-md-12 column\" style=\"margin-top: 10px; margin-left: 17px\"><button type=submit class=\"btn btn-primary btn-sm\" ng-click=executeStudentUpdate(studentEdit)>Update</button></div></form></div></div>"
  );


  $templateCache.put('file-upload',
    "<div ng-if=uploader><input type=file nv-file-select uploader=uploader multiple></div><table class=table><thead><tr><th width=50%>Name</th><th ng-show=uploader.isHTML5>Size</th><th ng-show=uploader.isHTML5>Progress</th><th>Status</th><th>Actions</th></tr></thead><tbody><tr ng-repeat=\"item in uploader.queue\"><td><strong>{{ item.file.name }}</strong></td><td ng-show=uploader.isHTML5 nowrap>{{ item.file.size/1024/1024|number:2 }} MB</td><td ng-show=uploader.isHTML5><div class=progress style=\"margin-bottom: 0\"><div class=progress-bar role=progressbar ng-style=\"{ 'width': item.progress + '%' }\"></div></div></td><td class=text-center><span ng-show=item.isSuccess><i class=\"glyphicon glyphicon-ok\"></i></span> <span ng-show=item.isCancel><i class=\"glyphicon glyphicon-ban-circle\"></i></span> <span ng-show=item.isError><i class=\"glyphicon glyphicon-remove\"></i></span></td><td nowrap><button type=button class=\"btn btn-success btn-xs\" ng-click=item.upload() ng-disabled=\"item.isReady || item.isUploading || item.isSuccess\"><span class=\"glyphicon glyphicon-upload\"></span> Upload</button> <button type=button class=\"btn btn-warning btn-xs\" ng-click=item.cancel() ng-disabled=!item.isUploading><span class=\"glyphicon glyphicon-ban-circle\"></span> Cancel</button> <button type=button class=\"btn btn-danger btn-xs\" ng-click=item.remove()><span class=\"glyphicon glyphicon-trash\"></span> Remove</button></td></tr></tbody></table><div><div>Queue progress:<div class=progress><div class=progress-bar role=progressbar ng-style=\"{ 'width': uploader.progress + '%' }\"></div></div></div><button type=button class=\"btn btn-success btn-sm\" ng-click=uploader.uploadAll() ng-disabled=!uploader.getNotUploadedItems().length><span class=\"glyphicon glyphicon-upload\"></span> Upload All</button> <button type=button class=\"btn btn-warning btn-sm\" ng-click=uploader.cancelAll() ng-disabled=!uploader.isUploading><span class=\"glyphicon glyphicon-ban-circle\"></span> Cancel All</button> <button type=button class=\"btn btn-danger btn-sm\" ng-click=uploader.clearQueue() ng-disabled=!uploader.queue.length><span class=\"glyphicon glyphicon-trash\"></span> Remove All</button></div>"
  );


  $templateCache.put('student-identification-card',
    "<div><form class=form-horizontal style=\"border: 1px solid #d3d3d3;background-color: #ededed; border-radius: 5px; padding: 10px\"><fieldset><legend>Identification card</legend><div class=\"col-md-12 column\"><table class=identification-card width=100%><tr><td><h4><b>{{ selectedStudent.name }} {{ selectedStudent.lastName}}</b></h4></td><td rowspan=2 width=180px><img alt=140x140 src={{selectedStudent.photoUrl}} class=\"img-rounded pull-right\"></td></tr><tr><td><h4>{{ selectedStudent.personId }}</h4></td></tr></table></div><div class=\"col-md-12 column\" style=\"padding-top: 20px\"><table class=identification-card width=100%><tr><td>Phone number:</td><td>{{ selectedStudent.mobile }}</td></tr><tr><td>Current balance:</td><td>{{ selectedStudent.balance | number : 2}} EUR</td><td><button type=button class=\"btn btn-primary btn-sm pull-right\" data-toggle=modal data-target=#addToBalanceModalView>Add to balance</button></td></tr></table></div><div class=\"col-md-12 column\" style=\"padding-top: 10px\"><button type=button class=\"btn btn-primary btn-sm\" style=\"width: 100%\">Attendance history</button></div></fieldset></form><balance-modal></balance-modal></div>"
  );


  $templateCache.put('students-list-header',
    "<div><div class=row><div class=col-xs-8><div class=input-group><span class=input-group-addon id=basic-addon1 ng-class=\"{'glyphicon glyphicon-refresh searchTextInput': basicSearch.spin , 'glyphicon glyphicon-search searchTextInput': !basicSearch.spin}\"></span> <input class=form-control placeholder=search ng-model=searchValue ng-keyup=executeSearch(searchValue)></div></div><div class=col-xs-3><div class=btn-group role=group aria-label=...><button type=button class=\"btn btn-default\" ng-class=\"{ active: studentPaging.perPage === 10}\" ng-click=setRecordsPerPage(10)>10</button> <button type=button class=\"btn btn-default\" ng-class=\"{ active: studentPaging.perPage === 25}\" ng-click=setRecordsPerPage(25)>25</button> <button type=button class=\"btn btn-default\" ng-class=\"{ active: studentPaging.perPage === 50}\" ng-click=setRecordsPerPage(50)>50</button></div></div><div class=col-xs-1><h4><span class=\"glyphicon glyphicon-plus pull-right\" style=\"cursor: pointer\" data-toggle=modal data-target=#addNewStudentModalView ng-click=resetNewStudent()></span></h4></div></div><new-student></new-student></div>"
  );


  $templateCache.put('students-list',
    "<div style=\"padding-top: 20px\"><table class=\"table table-hover\"><thead><tr><th>Name</th><th>Last name</th><th>Age</th><th>ID</th><th>Phone</th><th></th></tr></thead><tbody><tr dir-paginate=\"student in students | itemsPerPage: studentPaging.perPage\" current-page=studentPaging.page total-items=studentPaging.totalRecords ng-class-odd=\"'success'\" ng-class-even=\"'active'\" ng-click=setSelected(student.id) ng-class=\"{'missing-data-row': !student.mobile, 'selected-row': student.id === selectedStudent.id}\" pagination-id=studentsPaginationId><td>{{ student.name }}</td><td>{{ student.lastName }}</td><td>{{ student.age }}</td><td>{{ student.personId }}</td><td>{{ student.mobile }}</td><td><button href=#information type=button class=\"btn btn-primary btn-xs\" ng-click=openStudentDetailModal(student)>Details</button></td></tr></tbody></table><div class=row><div class=\"col-xs-12 text-center\"><dir-pagination-controls on-page-change=\"studentsPageChanged(newPageNumber, searchValue)\" pagination-id=studentsPaginationId></dir-pagination-controls></div></div><student-details></student-details></div>"
  );


  $templateCache.put('students',
    "<div class=mainForm ng-controller=StudentsController><div class=\"row clearfix\"><div class=\"col-md-7 column\"><students-list-header></students-list-header><students-list></students-list></div><div class=\"col-md-5 column\" ng-show=studentSelected><student-identification-card></student-identification-card></div></div></div>"
  );


  $templateCache.put('new-student-modal',
    "<div app-modal id=addNewStudentModalView class=\"modal fade bs-example-modal-lg\" tabindex=-1 role=dialog aria-labelledby=myLargeModalLabel aria-hidden=true><div class=\"modal-dialog modal-lg\"><div class=\"modal-content modalViewPadding\"><div class=modal-header><button type=button class=close data-dismiss=modal aria-hidden=true><span class=\"glyphicon glyphicon-remove\"></span></button><h4 class=modal-title id=myModalLabel>Add new student</h4></div><div style=\"padding-top: 20px; padding-bottom: 10px\"><div class=\"row clearfix\"><form role=form name=studentCreatedForm><div class=\"col-md-6 column\"><div class=\"col-md-6 column\"><div class=form-group><label for=studentName>Name</label><input class=form-control id=studentName ng-model=newStudent.name required></div><div class=form-group><label for=studentLastName>Last name</label><input type=tel class=form-control id=studentLastName ng-model=newStudent.lastName required></div></div><div class=\"col-md-6 column\"><div class=form-group><label for=studentMobile>Mobile</label><input class=form-control id=studentMobile ng-model=\"newStudent.mobile\"></div><div class=form-group><label for=studentPersonalNumber>Personal number</label><input class=form-control id=studentPersonalNumber ng-model=\"newStudent.personId\"></div></div><div class=\"col-md-12 column\"><div class=form-group><label for=parentInformationInput>Parent information</label><textarea ng-model=newStudent.parentsInfo class=\"form-control fixedTextArea\" id=parentInformationInput name=parentInformationInput></textarea></div><div class=form-group><label for=characteristicsInput>Characteristics</label><textarea ng-model=newStudent.characteristics class=\"form-control fixedTextArea\" id=characteristicsInput name=characteristicsInput></textarea></div></div></div><div class=\"col-md-6 column\"><div class=\"col-md-12 column\"><div class=form-group><label for=studentMail>e-mail</label><input class=form-control id=studentMail ng-model=\"newStudent.mail\"></div><div class=form-group ng-class=\"{'has-error': studentCreatedForm.date.$invalid}\"><date-picker date-picker-id=newStudentDatePicker input-field=newStudent.birthDateString label=Birthdate></date-picker></div></div><div class=\"col-md-12 row\"><photo-upload photo-id=newStudent.photoId photo-url=newStudent.photoUrl></photo-upload></div></div><div class=\"col-md-12 column\" style=\"padding-top: 10px\"><button type=submit class=\"btn btn-success btn-sm\" ng-click=executeStudentSave(newStudent)>Save</button> <button type=reset class=\"btn btn-primary btn-sm\" ng-click=executeReset()>Reset</button> <button type=reset class=\"btn btn-warning btn-sm\" ng-click=executeCancel()>Cancel</button></div></form></div></div></div></div></div>"
  );


  $templateCache.put('photo-upload',
    "<div style=\"padding: 10px\"><div><div ng-if=uploader><input type=file nv-file-select uploader=\"uploader\"></div><table class=table><thead><tr><th width=50%>Name</th><th ng-show=uploader.isHTML5>Size</th><th ng-show=uploader.isHTML5>Progress</th><th>Status</th><th>Actions</th></tr></thead><tbody><tr ng-repeat=\"item in uploader.queue\"><td><strong>{{ item.file.name }}</strong></td><td ng-show=uploader.isHTML5 nowrap>{{ item.file.size/1024/1024|number:2 }} MB</td><td ng-show=uploader.isHTML5><div class=progress style=\"margin-bottom: 0\"><div class=progress-bar role=progressbar ng-style=\"{ 'width': item.progress + '%' }\"></div></div></td><td class=text-center><span ng-show=item.isSuccess><i class=\"glyphicon glyphicon-ok\"></i></span> <span ng-show=item.isCancel><i class=\"glyphicon glyphicon-ban-circle\"></i></span> <span ng-show=item.isError><i class=\"glyphicon glyphicon-remove\"></i></span></td><td nowrap><button type=button class=\"btn btn-success btn-xs\" ng-click=item.upload() ng-disabled=\"item.isReady || item.isUploading || item.isSuccess\"><span class=\"glyphicon glyphicon-upload\"></span> Upload</button></td></tr></tbody></table></div><div ng-show=photoUrl><img alt=140x140 src={{photoUrl}} class=\"img-rounded text-center\"></div></div>"
  );

}]);
