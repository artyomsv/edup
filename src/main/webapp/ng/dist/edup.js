'use strict';

angular.module('edup.common', ['restangular']);
'use strict';

angular.module('edup.common')

    .config(['RestangularProvider', function (RestangularProvider) {

        var location = window.location.host;

        var baseUrl;

        if (location.indexOf('127.0.0.1') > -1) {
            baseUrl = 'https://localhost:8443/edup/api';
        } else {
            baseUrl = 'https://' + location + '/edup/api';
        }

        console.log(baseUrl);

        RestangularProvider.setBaseUrl(baseUrl);

        RestangularProvider.setErrorInterceptor(function (resp) {
            console.log(angular.toJson(resp, true));
            return false;
        });

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

    .service('RestService', ['Restangular', function (Restangular) {

        var rpc = Restangular.one('secured');

        return {
            Students: rpc.one('students')
        };

    }]
);
'use strict';

angular.module('edup.login', []);
'use strict';

angular.module('edup.login')

    .directive('edupLogin', function () {
        return {
            restrict: 'E',
            templateUrl: 'edup-login',
            link: function () {

            }
        };
    }
);

'use strict';

angular.module('edup.header', ['ui.router']);
'use strict';

angular.module('edup.header')

    .controller('HeaderController', ['$scope', '$location', function ($scope, $location) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };

        $scope.downloadDocument = function () {
            window.open('https://172.20.10.4:8443/edup/api/documents');
        };
    }]
);
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

angular.module('edup.tabs', [
    'mwl.calendar',
    'ui.bootstrap',
    'ngAnimate'
]);
'use strict';

angular.module('edup.tabs')

    .controller('CalendarTabController', ['$scope', '$modal', 'moment', 'calendarTitle', function ($scope, $modal, moment, calendarTitle) {

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

angular.module('edup.tabs')

    .controller('StudentsTabController', ['$scope', 'RestService', function ($scope, RestService) {

        RestService.Students.get().then(function (result) {
            $scope.students = result.values;
            if ($scope.students.length > 0) {
                $scope.selectedStudent = $scope.students[0];
            }
        });

        $scope.setSelected = function (studentId) {
            $scope.selectedStudent = _.find($scope.students, function (student) {
                return studentId === student.id;
            });
        };
    }]
);


'use strict';

angular.module('edup.tabs')

    .controller('IdentificationCardController', ['$scope', function ($scope) {

        $scope.addToBalance = function (value) {
            $scope.selectedStudent.balance = $scope.selectedStudent.balance + parseInt(value);
        };

    }]
);


'use strict';

angular.module('edup.tabs')

    .controller('StudentAttendanceController', ['$scope', function ($scope) {

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
    }]

);


'use strict';

angular.module('edup.tabs')

    .controller('StudentBalanceController', ['$scope', function ($scope) {
        $scope.addToBalance = function (value) {
            $scope.selectedStudent.balance = $scope.selectedStudent.balance + parseInt(value);
        };

    }]
);


'use strict';

angular.module('edup.tabs')

    .controller('StudentDocumentsController', ['$scope', function ($scope) {

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
    }]
);


'use strict';

angular.module('edup.tabs')

    .controller('StudentFormController', ['$scope', function ($scope) {
    }]
);


'use strict';

angular.module('edup.tabs')

    .controller('StudentInputForms', ['$scope', function ($scope) {

    }]
);


'use strict';

angular.module('edup.tabs')

    .controller('StudentsListController', ['$scope', function ($scope) {

    }]
);


'use strict';

angular.module('edup.tabs')

    .directive('tabsCalendar', function () {
        return {
            restrict: 'E',
            templateUrl: 'tabs-calendar',
            link: function ($scope) {
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('tabsStudents', function () {
        return {
            restrict: 'E',
            templateUrl: 'tabs-students',
            link : function ($scope) {
                $scope.directiveTest = 'Students tab directive';
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('identificationCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'identification-card',
            link : function ($scope) {
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('studentAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-attendance',
            link : function ($scope) {
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('studentBalance', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-balance',
            link : function ($scope) {
                $scope.newBalanceValue = '';
                
                $scope.openDatePicker = function() {
                    $('#datetimepicker').datetimepicker();
                };
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('studentDocuments', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-documents',
            link : function ($scope) {
                $scope.directiveTest = 'Student tabbed pane directive';
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('studentForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-form',
            link : function ($scope) {
                $scope.directiveTest = 'Student Form tabbed pane directive';
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('studentInputForms', function () {
        return {
            restrict: 'E',
            templateUrl: 'student-input-forms',
            link : function ($scope) {
                $scope.directiveTest = 'Student input forms';
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('studentsList', function () {
        return {
            restrict: 'E',
            templateUrl: 'students-list',
            link : function ($scope) {
                $scope.directiveTest = 'Students list';
            }
        };
    }
);
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
                templateUrl: 'tabs-students',
                url: '/students',
                controller: 'StudentsTabController'
            })
            .state('calendar', {
                templateUrl: 'tabs-calendar',
                url: '/calendar',
                controller: 'CalendarTabController'
            }
        );

    }]
);angular.module('edup').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('edup-footer',
    "<div ng-controller=FooterController><nav class=\"navbar navbar-default\" style=\"margin-bottom: 0px !important; margin-top: auto !important\"><div class=\"navbar-text text-center\" style=\"width: 100%\">Application version: {{ appVersion }}</div></nav></div>"
  );


  $templateCache.put('edup-header',
    "<div ng-controller=HeaderController><nav class=\"navbar navbar-default\"><div class=container-fluid><div class=navbar-header><button type=button class=\"navbar-toggle collapsed\" data-toggle=collapse data-target=#bs-example-navbar-collapse-1><span class=sr-only>Toggle navigation</span> <span class=icon-bar></span> <span class=icon-bar></span> <span class=icon-bar></span></button> <a class=navbar-brand href=\"\">Educational planning application</a></div><div class=\"collapse navbar-collapse\" id=bs-example-navbar-collapse-1><ul class=\"nav navbar-nav\"><li ng-class=\"{ active: isActive('/students')}\"><a href=#students>Students<span class=sr-only>(current)</span></a></li><li ng-class=\"{ active: isActive('/calendar')}\"><a href=#calendar>Calendar<span class=sr-only>(current)</span></a></li><li class=dropdown><a href=#report class=dropdown-toggle data-toggle=dropdown role=button aria-expanded=false>Reports<span class=caret></span></a><ul class=dropdown-menu role=menu><li ng-click=downloadDocument()><a href=#>Visiting Journal</a></li><li><a href=#>Another action</a></li><li><a href=#>Something else here</a></li><li class=divider></li><li><a href=#>Separated link</a></li><li class=divider></li><li><a href=#>One more separated link</a></li></ul></li></ul><ul class=\"nav navbar-nav navbar-right\"><li><a href=#>Log out</a></li></ul></div></div></nav></div>"
  );


  $templateCache.put('edup-login',
    "<div class=login-container><div id=output></div><div class=avatar></div><div class=form-box><form name=form action=j_security_check method=post ng-submit=login(event)><input name=j_username placeholder=username id=text ng-model=username required> <input type=password name=j_password placeholder=password id=password ng-model=password required> <button class=\"btn btn-info btn-block login\" type=submit>Login</button></form></div></div>"
  );


  $templateCache.put('tabs-calendar',
    "<div class=mainForm ng-controller=CalendarTabController><div class=\"col-md-12 column\"><div class=\"col-md-3 form-inline\"><div class=\"btn-group pull-left\"><button class=\"btn btn-primary btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay decrement=calendarView>&lt;&lt; Prev</button> <button class=\"btn btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay set-to-today>Today</button> <button class=\"btn btn-primary btn-sm\" ng-click=updateTitle() mwl-date-modifier date=calendarDay increment=calendarView>Next &gt;&gt;</button></div></div><div class=col-md-2><div class=btn-group><h4>{{ currentDay }}</h4></div></div><div class=\"col-md-3 form-inline\"><div class=\"btn-group pull-right\"><button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('year')\">Year</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('month')\">Month</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('week')\">Week</button> <button class=\"btn btn-primary btn-sm\" ng-click=\"setCalendarView('day')\">Day</button></div></div><div class=\"col-md-4 form-inline\"><h4>Today events:</h4></div></div><div class=\"col-md-12 column\" style=\"padding-top: 20px\"><div class=\"col-md-8 column\"><mwl-calendar events=events view=calendarView view-title=calendarTitle current-day=calendarDay on-event-click=eventClicked(calendarEvent) edit-event-html=\"'<i class=\\'glyphicon glyphicon-pencil\\'></i>'\" delete-event-html=\"'<i class=\\'glyphicon glyphicon-remove\\'></i>'\" on-edit-event-click=eventEdited(calendarEvent) on-delete-event-click=eventDeleted(calendarEvent)></mwl-calendar></div><div class=\"col-md-4 column\"><div ng-repeat=\"event in events\"><h4><div class=label ng-class=\"{ 'label-primary': event.type === 'info', 'label-warning': event.type === 'warning', 'label-danger': event.type === 'important'}\" data-toggle=tooltip data-placement=top title=\"Tooltip on top\" tooltip=\"{{ event.title }}\" tooltip-trigger=\"{{{true: 'mouseenter', false: 'never'}[myForm.username.$invalid]}}\">({{event.startsAt | date:'HH:mm'}} - {{event.endsAt | date:'HH:mm'}}) {{event.title | limitTo: 33}} {{event.title.length > 33 ? '...' : ''}}</div></h4></div></div></div></div>"
  );


  $templateCache.put('tabs-students',
    "<div class=mainForm ng-controller=StudentsTabController><div class=\"row clearfix\"><div class=\"col-md-7 column\"><students-list></students-list></div><div class=\"col-md-5 column\" style=\"padding-top: 90px\"><identification-card></identification-card></div></div></div>"
  );


  $templateCache.put('identification-card',
    "<div ng-controller=IdentificationCardController><form class=form-horizontal style=\"border: 1px solid #d3d3d3;background-color: #ededed; border-radius: 5px; padding: 10px\"><fieldset><legend>Identification card</legend><div class=\"col-md-12 column\"><table class=identification-card width=100%><tr><td><h4><b>{{ selectedStudent.name }} {{ selectedStudent.lastName}}</b></h4></td><td rowspan=2 width=180px><img alt=140x140 src={{selectedStudent.photo}} class=\"img-rounded pull-right\"></td></tr><tr><td><h4>{{ selectedStudent.personId }}</h4></td></tr></table></div><div class=\"col-md-12 column\" style=\"padding-top: 20px\"><table class=identification-card width=100%><tr><td>Phone number:</td><td>{{ selectedStudent.mobile }}</td></tr><tr><td>Current balance:</td><td>{{ selectedStudent.balance }} EUR</td><td><button type=button class=\"btn btn-success pull-right\" data-toggle=modal data-target=#addToBalanceModalView>Add to balance</button></td></tr></table></div><div class=\"col-md-12 column\" style=\"padding-top: 10px\"><button type=button class=\"btn btn-info\" style=\"width: 100%\">Attendance history</button></div></fieldset></form><div id=addToBalanceModalView class=\"modal fade bs-example-modal-sm\" tabindex=-1 role=dialog aria-labelledby=mySmallModalLabel aria-hidden=true><div class=\"modal-dialog modal-sm\"><div class=\"modal-content modalViewPadding\"><form class=form-horizontal><fieldset><legend>Add money to account</legend><div class=form-group><label class=\"col-md-4 control-label\" for=amountTextInput>Amount</label><div class=col-md-8><input ng-model=newBalanceValue id=amountTextInput name=textinput placeholder=amount class=\"form-control input-md\"></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=addToBalanceButton></label><div class=col-md-4><button ng-click=addToBalance(newBalanceValue) id=addToBalanceButton name=payButton class=\"btn btn-success pull-right\" data-dismiss=modal>Save</button></div></div></fieldset></form></div></div></div></div>"
  );


  $templateCache.put('student-attendance',
    "<div class=mainForm ng-controller=StudentAttendanceController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ selectedStudent.fullName }}</span></h3></td><td rowspan=2 style=\"border-top: none\" width=180px><img alt=140x140 src={{selectedStudent.photo}} class=\"img-rounded pull-right\"></td></tr><tr class=active><td style=\"border-top: none\"><h3><span class=\"label pull-right\" ng-class=\"{'label-danger' :  selectedStudent.balance < 0, 'label-success' :  selectedStudent.balance > 0, 'label-primary' :  selectedStudent.balance == 0}\">{{ selectedStudent.balance }} EUR</span></h3></td></tr></table></div></div><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>History of attendances</legend><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Subject name</th><th>Date</th><th>Amount</th></tr></thead><tbody><tr ng-repeat=\"event in attendanceHistory\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td>{{ event.subject }}</td><td>{{ event.date }}</td><td>{{ event.amount }} EUR</td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div></fieldset><div class=\"row clearfix\"><div class=\"col-md-12 column\"><button type=button class=\"btn btn-success pull-right\">Add new attendance</button></div></div></div>"
  );


  $templateCache.put('student-balance',
    "<div class=mainForm ng-controller=StudentBalanceController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ selectedStudent.fullName }}</span></h3></td><td rowspan=2 style=\"border-top: none\" width=180px><img alt=140x140 src={{selectedStudent.photo}} class=\"img-rounded pull-right\"></td></tr><tr class=active><td style=\"border-top: none\"><h3><span class=\"label pull-right\" ng-class=\"{'label-danger' :  selectedStudent.balance < 0, 'label-success' :  selectedStudent.balance > 0, 'label-primary' :  selectedStudent.balance == 0}\">{{ selectedStudent.balance }} EUR</span></h3></td></tr></table></div></div><form class=form-horizontal><fieldset><legend>Add money to account</legend><div class=form-group><label class=\"col-md-4 control-label\" for=amountTextInput>Amount</label><div class=col-md-4><input ng-model=newBalanceValue id=amountTextInput name=textinput placeholder=amount class=\"form-control input-md\"></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=addToBalanceButton></label><div class=col-md-4><button ng-click=addToBalance(newBalanceValue) id=addToBalanceButton name=payButton class=\"btn btn-success pull-right\">Add to balance</button></div></div></fieldset></form><form class=form-horizontal><fieldset><legend>Make payment</legend><div class=form-group><label class=\"col-md-4 control-label\" for=subjectList>Subject</label><div class=col-md-4><select id=subjectList name=subjectList class=form-control><option value=1>History</option><option value=2>English</option><option value=3>Math</option><option value=4>Biology</option></select></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=datePicketLabel>Date</label><div class=col-md-4><div class=\"input-group date\" id=datetimepicker><input id=datePicketLabel class=\"form-control\"> <span ng-click=openDatePicker() class=input-group-addon><span class=\"glyphicon glyphicon-calendar\"></span></span></div></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=payButton></label><div class=col-md-4><button id=payButton name=payButton class=\"btn btn-success pull-right\">Pay</button></div></div></fieldset></form></div>"
  );


  $templateCache.put('student-documents',
    "<div class=mainForm ng-controller=StudentDocumentsController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ selectedStudent.fullName }}</span></h3></td><td style=\"border-top: none\" width=180px><img alt=140x140 src={{selectedStudent.photo}} class=\"img-rounded pull-right\"></td></tr></table></div></div><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>List of documents</legend><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Document name</th><th>Date loaded</th><th></th></tr></thead><tbody><tr ng-repeat=\"document in documents\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td>{{ document.name }}</td><td>{{ document.date }}</td><td><a href=\"{{ document.link }}\">Download</a></td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div></fieldset><div class=\"row clearfix\"><div class=\"col-md-12 column\"><button type=button class=\"btn btn-success pull-right\">Add new document</button></div></div></div>"
  );


  $templateCache.put('student-form',
    "<div class=mainForm ng-controller=StudentFormController><div class=\"row clearfix\"><div class=\"col-md-6 column\"><form class=form-horizontal><fieldset><div class=form-group><label class=\"col-md-4 control-label\" for=nameInput>Name</label><div class=col-md-4><input ng-model=selectedStudent.name id=nameInput name=lastNameInput placeholder=\"\" class=\"form-control input-md\" required></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=lastNameInput>Last name</label><div class=col-md-4><input ng-model=selectedStudent.lastName id=lastNameInput name=lastNameInput placeholder=\"\" class=\"form-control input-md\" required></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=personalNumberInput>Personal number</label><div class=col-md-4><input ng-model=selectedStudent.personId id=personalNumberInput name=personalNumberInput placeholder=\"\" class=\"form-control input-md\" required></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=phoneInput>Mob. mobile</label><div class=col-md-4><input ng-model=selectedStudent.mobile id=phoneInput name=phoneInput placeholder=\"\" class=\"form-control input-md\" required></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=birthDateInput>Birth date</label><div class=col-md-4><input ng-model=selectedStudent.birthDate id=birthDateInput name=birthDateInput placeholder=DD/MM/YYYY class=\"form-control input-md\" required></div></div></fieldset></form></div><div class=\"col-md-6 column vcenter\"><img alt=280x280 src={{selectedStudent.photo}} class=\"img-rounded pull-right\"></div></div><div class=\"row clearfix\"><div class=\"col-md-12 column\"><form class=form-horizontal><fieldset><legend>Parents information</legend><div class=col-md-12><textarea ng-model=selectedStudent.parentsInfo class=\"form-control fixedTextArea\" id=parentInformationInput name=parentInformationInput></textarea></div></fieldset></form></div></div><div class=\"row clearfix\"><div class=\"col-md-12 column\"><form class=form-horizontal><fieldset><legend>Characteristics</legend><div class=col-md-12><textarea ng-model=selectedStudent.essentialInformation class=\"form-control fixedTextArea\" id=characteristicsInput name=characteristicsInput></textarea></div></fieldset></form></div></div></div>"
  );


  $templateCache.put('student-input-forms',
    "<div role=tabpanel ng-controller=StudentInputForms><ul class=\"nav nav-tabs\" role=tablist><li role=presentation ng-class=\"{ active: isActive('/information')}\"><a href=#information aria-controls=home role=tab data-toggle=tab>Information</a></li><li role=presentation ng-class=\"{ active: isActive('/balance')}\"><a href=#balance aria-controls=profile role=tab data-toggle=tab>Balance</a></li><li role=presentation ng-class=\"{ active: isActive('/documents')}\"><a href=#documents aria-controls=messages role=tab data-toggle=tab>Documents</a></li><li role=presentation ng-class=\"{ active: isActive('/attendance')}\"><a href=#attendance aria-controls=settings role=tab data-toggle=tab>Attendance</a></li></ul><div class=tab-content><div role=tabpanel class=\"tab-pane active\" id=information><student-form></student-form></div><div role=tabpanel class=tab-pane id=balance><student-balance></student-balance></div><div role=tabpanel class=tab-pane id=documents><student-documents></student-documents></div><div role=tabpanel class=tab-pane id=attendance><student-attendance></student-attendance></div></div></div>"
  );


  $templateCache.put('students-list',
    "<div ng-controller=StudentsListController><div class=parent><div class=\"child pull=left\"><input id=searchinput name=searchinput type=search placeholder=search class=\"form-control input-md\"></div><div class=\"child pull-right\"><h4><span class=\"glyphicon glyphicon-user pull-right\"></span></h4></div></div><table class=\"table table-hover\"><thead><tr><th>Name</th><th>Last name</th><th>Age</th><th>ID</th><th>Phone</th><th></th></tr></thead><tbody><tr ng-repeat=\"student in students\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\" ng-click=setSelected(student.id) ng-class=\"{selected: student.id === selectedStudent.id}\"><td>{{ student.name }}</td><td>{{ student.lastName }}</td><td>{{ student.age }}</td><td>{{ student.personId }}</td><td>{{ student.mobile }}</td><td><button type=button class=\"btn btn-primary btn-xs\" data-toggle=modal data-target=#input-forms-modal-view>Details</button></td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div><div id=input-forms-modal-view class=\"modal fade bs-example-modal-lg\" tabindex=-1 role=dialog aria-labelledby=myLargeModalLabel aria-hidden=true><div class=\"modal-dialog modal-lg\"><div class=\"modal-content modalViewPadding\"><student-input-forms></student-input-forms></div></div></div>"
  );

}]);
