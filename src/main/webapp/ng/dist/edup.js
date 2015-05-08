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

        Restangular.one();

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

angular.module('edup.header', []);
'use strict';

angular.module('edup.header')

    .controller('HeaderController', ['$scope', '$location', function ($scope, $location) {
        $scope.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };
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

angular.module('edup.client', []);
'use strict';

angular.module('edup.client')

    .controller('ClientAttendanceController', ['$scope', function ($scope) {
        $scope.student = 'Taisija Polakova';
        $scope.balance = 25;//Math.floor((Math.random() * 100) - 50);

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

angular.module('edup.client')

    .controller('ClientBalanceController', ['$scope', function ($scope) {
        $scope.student = 'Taisija Polakova';
        $scope.balance = Math.floor((Math.random() * 100) - 50);
    }]
);


'use strict';

angular.module('edup.client')

    .controller('ClientDocumentsController', ['$scope', function ($scope) {
        $scope.student = 'Taisija Polakova';

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

angular.module('edup.client')

    .controller('ClientFormController', ['$scope', function ($scope) {
        $scope.student = 'Taisija Polakova';
    }]
);


'use strict';

angular.module('edup.client')

    .directive('clientAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-attendance',
            link : function ($scope) {
                $scope.directiveTest = 'Client attendance tabbed pane directive';
            }
        };
    }
);
'use strict';

angular.module('edup.client')

    .directive('clientBalance', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-balance',
            link : function ($scope) {
                $scope.directiveTest = 'Client balance tabbed pane directive';
            }
        };
    }
);
'use strict';

angular.module('edup.client')

    .directive('clientDocuments', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-documents',
            link : function ($scope) {
                $scope.directiveTest = 'Client tabbed pane directive';
            }
        };
    }
);
'use strict';

angular.module('edup.client')

    .directive('clientForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-form',
            link : function ($scope) {
                $scope.directiveTest = 'Client Form tabbed pane directive';
            }
        };
    }
);
'use strict';

angular.module('edup.tabs', []);
'use strict';

angular.module('edup.tabs')

    .controller('CalendarTabController', ['$scope', function ($scope) {
        $scope.statusTab = 'Under construction';
    }]
);


'use strict';

angular.module('edup.tabs')

    .controller('ClientTabController', ['$scope', function ($scope) {
        $scope.student = 'Taisija Polakova';
        $scope.balance = 25;//Math.floor((Math.random() * 100) - 50);

        $scope.clients = [
            {
                'id' : 1,
                'name': 'Artyom',
                'lastName' : 'Stukans',
                'age' : 33,
                'personalId' : '281281-10562',
                'phone' : '28 61 81 25',
                'active' : true,
                'details' : 'Details',
                'balance' : 35,
                'parentsInfo' : 'Information about Artyom parents',
                'essentialInformation' : 'Some essential information about student'
            },
            {
                'id' : 2,
                'name': 'Julija',
                'lastName' : 'Avdejeva',
                'age' : 18,
                'personalId' : '220497-12345',
                'phone' : '28 78 45 90',
                'active' : false,
                'details' : 'Details',
                'balance' : 25,
                'parentsInfo' : 'Information about Yuliya parents',
                'essentialInformation' : 'Some essential information about student'
            },
            {
                'id' : 3,
                'name': 'Taisija',
                'lastName' : 'Polakova',
                'age' : 3,
                'personalId' : '231111-12345',
                'phone' : '28 89 00 12',
                'active' : true,
                'details' : 'Details',
                'balance' : 15,
                'parentsInfo' : 'Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents ',
                'essentialInformation' : 'Some essential information about student'
            }

        ];

        $scope.selectedClient = $scope.clients[2];

        $scope.setSelected = function (clientId) {
            $scope.selectedClient = _.find($scope.clients, function(client) {
                return clientId === client.id;
            });
        };
    }]
);


'use strict';

angular.module('edup.tabs')

    .directive('tabsCalendar', function () {
        return {
            restrict: 'E',
            templateUrl: 'tabs-calendar',
            link: function ($scope) {
                $scope.dateTitle = 'Calendar tab directive';

                var options = {
                    events_source: function () {
                        return [
                            {
                                "success": 1,
                                "result": [
                                    {
                                        "id": "293",
                                        "title": "This is warning class event with very long title to check how it fits to evet in day view",
                                        "url": "http://www.example.com/",
                                        "class": "event-warning",
                                        "start": "1362938400000",
                                        "end":   "1363197686300"
                                    },
                                    {
                                        "id": "256",
                                        "title": "Event that ends on timeline",
                                        "url": "http://www.example.com/",
                                        "class": "event-warning",
                                        "start": "1363155300000",
                                        "end":   "1363227600000"
                                    },
                                    {
                                        "id": "276",
                                        "title": "Short day event",
                                        "url": "http://www.example.com/",
                                        "class": "event-success",
                                        "start": "1363245600000",
                                        "end":   "1363252200000"
                                    },
                                    {
                                        "id": "294",
                                        "title": "This is information class ",
                                        "url": "http://www.example.com/",
                                        "class": "event-info",
                                        "start": "1363111200000",
                                        "end":   "1363284086400"
                                    },
                                    {
                                        "id": "297",
                                        "title": "This is success event",
                                        "url": "http://www.example.com/",
                                        "class": "event-success",
                                        "start": "1363234500000",
                                        "end":   "1363284062400"
                                    },
                                    {
                                        "id": "54",
                                        "title": "This is simple event",
                                        "url": "http://www.example.com/",
                                        "class": "",
                                        "start": "1363712400000",
                                        "end":   "1363716086400"
                                    },
                                    {
                                        "id": "532",
                                        "title": "This is inverse event",
                                        "url": "http://www.example.com/",
                                        "class": "event-inverse",
                                        "start": "1364407200000",
                                        "end":   "1364493686400"
                                    },
                                    {
                                        "id": "548",
                                        "title": "This is special event",
                                        "url": "http://www.example.com/",
                                        "class": "event-special",
                                        "start": "1363197600000",
                                        "end":   "1363629686400"
                                    },
                                    {
                                        "id": "295",
                                        "title": "Event 3",
                                        "url": "http://www.example.com/",
                                        "class": "event-important",
                                        "start": "1364320800000",
                                        "end":   "1364407286400"
                                    }
                                ]
                            }

                        ];
                    },
                    view: 'month',
                    tmpl_path: '/vendor/bower_components/bootstrap-calendar/tmpls/',
                    tmpl_cache: false,
                    day: '2013-03-12',
                    onAfterEventsLoad: function(events) {
                        if(!events) {
                            return;
                        }
                        var list = $('#eventlist');
                        list.html('');

                        $.each(events, function(key, val) {
                            $(document.createElement('li'))
                                .html('<a href="' + val.url + '">' + val.title + '</a>')
                                .appendTo(list);
                        });
                    },
                    onAfterViewLoad: function(view) {
                        $('.page-header h3').text(this.getTitle());
                        $('.btn-group button').removeClass('active');
                        $('button[data-calendar-view="' + view + '"]').addClass('active');
                    },
                    classes: {
                        months: {
                            general: 'label'
                        }
                    }
                };

                var calendar = $('#calendar').calendar(options);

                $('.btn-group button[data-calendar-nav]').each(function() {
                    var $this = $(this);
                    $this.click(function() {
                        calendar.navigate($this.data('calendar-nav'));
                    });
                });

                $('.btn-group button[data-calendar-view]').each(function() {
                    var $this = $(this);
                    $this.click(function() {
                        calendar.view($this.data('calendar-view'));
                    });
                });

                $('#first_day').change(function(){
                    var value = $(this).val();
                    value = value.length ? parseInt(value) : null;
                    calendar.setOptions({first_day: value});
                    calendar.view();
                });

                $('#language').change(function(){
                    calendar.setLanguage($(this).val());
                    calendar.view();
                });

                $('#events-in-modal').change(function(){
                    var val = $(this).is(':checked') ? $(this).val() : null;
                    calendar.setOptions({modal: val});
                });
                $('#events-modal .modal-header, #events-modal .modal-footer').click(function(e){
                    //e.preventDefault();
                    //e.stopPropagation();
                });

                var $this = $(this);
                $this.click(function() {
                    console.log(Calendar.getTitle());
                    $scope.dateTitle = Calendar.getTitle();
                });
            }
        };
    }
);
'use strict';

angular.module('edup.tabs')

    .directive('tabsClient', function () {
        return {
            restrict: 'E',
            templateUrl: 'tabs-client',
            link : function ($scope) {
                $scope.directiveTest = 'Client tab directive';
            }
        };
    }
);
'use strict';

angular.module('edup.navbar', ['ui.router']);
'use strict';

angular.module('edup.navbar')

    .controller('NavbarController', ['$scope', '$state', function ($scope, $state) {
        $scope.calendarModel = {
            items: ['Clients', 'Calendar'],
            states: ['clients', 'calendar'],
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

angular.module('edup',
    [
        'edup.navbar',
        'edup.tabs',
        'edup.client',
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
            .state('clients', {
                templateUrl: 'tabs-client',
                url: '/clients',
                controller: 'ClientTabController'
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

  $templateCache.put('client-attendance',
    "<div class=mainForm ng-controller=ClientAttendanceController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ student }}</span></h3></td><td rowspan=2 style=\"border-top: none\" width=180px><img alt=140x140 src=./../images/taja.png class=\"img-rounded pull-right\"></td></tr><tr class=active><td style=\"border-top: none\"><h3><span class=\"label pull-right\" ng-class=\"{'label-danger' :  balance < 0, 'label-success' :  balance > 0, 'label-primary' :  balance == 0}\">{{ balance }} EUR</span></h3></td></tr></table></div></div><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>History of attendances</legend><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Subject name</th><th>Date</th><th>Amount</th></tr></thead><tbody><tr ng-repeat=\"event in attendanceHistory\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td>{{ event.subject }}</td><td>{{ event.date }}</td><td>{{ event.amount }} EUR</td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div></fieldset><div class=\"row clearfix\"><div class=\"col-md-12 column\"><button type=button class=\"btn btn-success pull-right\">Add new attendance</button></div></div></div>"
  );


  $templateCache.put('client-balance',
    "<div class=mainForm ng-controller=ClientBalanceController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ student }}</span></h3></td><td rowspan=2 style=\"border-top: none\" width=180px><img alt=140x140 src=./../images/taja.png class=\"img-rounded pull-right\"></td></tr><tr class=active><td style=\"border-top: none\"><h3><span class=\"label pull-right\" ng-class=\"{'label-danger' :  balance < 0, 'label-success' :  balance > 0, 'label-primary' :  balance == 0}\">{{ balance }} EUR</span></h3></td></tr></table></div></div><form class=form-horizontal><fieldset><legend>Add money to account</legend><div class=form-group><label class=\"col-md-4 control-label\" for=amountTextInput>Amount</label><div class=col-md-4><input id=amountTextInput name=textinput placeholder=amount class=\"form-control input-md\"></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=addToBalanceButton></label><div class=col-md-4><button id=addToBalanceButton name=payButton class=\"btn btn-success pull-right\">Add to balance</button></div></div></fieldset></form><form class=form-horizontal><fieldset><legend>Form Name</legend><div class=form-group><label class=\"col-md-4 control-label\" for=subjectList>Subject</label><div class=col-md-4><select id=subjectList name=subjectList class=form-control><option value=1>History</option><option value=2>English</option><option value=3>Math</option><option value=4>Biology</option></select></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=datePicketLabel>Date</label><div class=col-md-4><div class=\"input-group date\" id=datetimepicker1><input id=datePicketLabel class=\"form-control\"> <span class=input-group-addon><span class=\"glyphicon glyphicon-calendar\"></span></span></div></div><script>$(function () {\n" +
    "                        $('#datetimepicker1').datetimepicker();\n" +
    "                    });</script></div><div class=form-group><label class=\"col-md-4 control-label\" for=payButton></label><div class=col-md-4><button id=payButton name=payButton class=\"btn btn-success pull-right\">Pay</button></div></div></fieldset></form></div>"
  );


  $templateCache.put('client-documents',
    "<div class=mainForm ng-controller=ClientDocumentsController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ student }}</span></h3></td><td style=\"border-top: none\" width=180px><img alt=140x140 src=./../images/taja.png class=\"img-rounded pull-right\"></td></tr></table></div></div><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>List of documents</legend><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Document name</th><th>Date loaded</th></tr></thead><tbody><tr ng-repeat=\"document in documents\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td>{{ document.name }}</td><td>{{ document.date }}</td><td><a href=\"{{ document.link }}\">Download</a></td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div></fieldset><div class=\"row clearfix\"><div class=\"col-md-12 column\"><button type=button class=\"btn btn-success pull-right\">Add new document</button></div></div></div>"
  );


  $templateCache.put('client-form',
    "<div class=mainForm ng-controller=ClientFormController><div class=\"row clearfix\"><div class=\"col-md-6 column\"><form class=form-horizontal><fieldset><div class=form-group><label class=\"col-md-4 control-label\" for=nameInput>Name</label><div class=col-md-4><input id=nameInput name=nameInput placeholder=\"\" class=\"form-control input-md\" required></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=lastNameInput>Last name</label><div class=col-md-4><input id=lastNameInput name=lastNameInput placeholder=\"\" class=\"form-control input-md\" required></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=personalNumberInput>Personal number</label><div class=col-md-4><input id=personalNumberInput name=personalNumberInput placeholder=\"\" class=\"form-control input-md\" required></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=phoneInput>Mob. phone</label><div class=col-md-4><input id=phoneInput name=phoneInput placeholder=\"\" class=\"form-control input-md\" required></div></div><div class=form-group><label class=\"col-md-4 control-label\" for=birthDateInput>Birth date</label><div class=col-md-4><input id=birthDateInput name=birthDateInput placeholder=DD/MM/YYYY class=\"form-control input-md\" required></div></div></fieldset></form></div><div class=\"col-md-6 column vcenter\"><img alt=280x280 src=./../images/taja.png class=\"img-rounded pull-right\"></div></div><div class=\"row clearfix\"><div class=\"col-md-12 column\"><form class=form-horizontal><fieldset><legend>Parents information</legend><div class=col-md-12><textarea class=\"form-control fixedTextArea\" id=parentInformationInput name=parentInformationInput></textarea></div></fieldset></form></div></div><div class=\"row clearfix\"><div class=\"col-md-12 column\"><form class=form-horizontal><fieldset><legend>Characteristics</legend><div class=col-md-12><textarea class=\"form-control fixedTextArea\" id=characteristicsInput name=characteristicsInput></textarea></div></fieldset></form></div></div></div>"
  );


  $templateCache.put('edup-footer',
    "<div ng-controller=FooterController><nav class=\"navbar navbar-default\" style=\"margin-bottom: 0px !important; margin-top: auto !important\"><div class=\"navbar-text text-center\" style=\"width: 100%\">Application version: {{ appVersion }}</div></nav></div>"
  );


  $templateCache.put('edup-header',
    "<div ng-controller=HeaderController><nav class=\"navbar navbar-default\"><div class=container-fluid><div class=navbar-header><button type=button class=\"navbar-toggle collapsed\" data-toggle=collapse data-target=#bs-example-navbar-collapse-1><span class=sr-only>Toggle navigation</span> <span class=icon-bar></span> <span class=icon-bar></span> <span class=icon-bar></span></button> <a class=navbar-brand href=\"\">Educational planning application</a></div><div class=\"collapse navbar-collapse\" id=bs-example-navbar-collapse-1><ul class=\"nav navbar-nav\"><li ng-class=\"{ active: isActive('/clients')}\"><a href=#clients>Clients <span class=sr-only>(current)</span></a></li><li ng-class=\"{ active: isActive('/calendar')}\"><a href=#calendar>Calendar</a></li><li class=dropdown><a href=#report class=dropdown-toggle data-toggle=dropdown role=button aria-expanded=false>Reports<span class=caret></span></a><ul class=dropdown-menu role=menu><li><a href=#>Action</a></li><li><a href=#>Another action</a></li><li><a href=#>Something else here</a></li><li class=divider></li><li><a href=#>Separated link</a></li><li class=divider></li><li><a href=#>One more separated link</a></li></ul></li></ul><ul class=\"nav navbar-nav navbar-right\"><li><a href=#>Log out</a></li></ul></div></div></nav></div>"
  );


  $templateCache.put('app/',
    "<!DOCTYPE html><html><head><title>Login standalone</title><link rel=stylesheet href=../dist/edup-bootstrap.css><link rel=stylesheet href=../dist/edup.css><link rel=stylesheet href=../vendor/bower_components/bootstrap-calendar/css/calendar.min.css><script src=../vendor/bower_components/jquery/dist/jquery.js></script><script src=../vendor/bower_components/lodash/dist/lodash.js></script><script src=../vendor/bower_components/angular/angular.js></script><script src=../vendor/bower_components/ui-router/release/angular-ui-router.js></script><script src=../vendor/bower_components/angular-sanitize/angular-sanitize.js></script><script src=../vendor/bower_components/restangular/dist/restangular.js></script><script src=../vendor/bower_components/bootstrap/dist/js/bootstrap.js></script><script src=../vendor/bower_components/bootstrap-calendar/js/calendar.js></script><script src=../vendor/bower_components/ng-sortable/dist/ng-sortable.js></script><script src=../vendor/bower_components/moment/min/moment.min.js></script><script src=../vendor/bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js></script><script src=../dist/edup.js></script></head><body ng-app=edup><div ng-controller=NavbarController id=edupHeader><edup-header></edup-header></div><div class=container ui-view window-size=\"\" style=\"width: 80%;border: 1px solid #d3d3d3;border-radius: 10px;background-color: rgba(244, 244, 244, 0.7);margin-bottom: 20px\"></div></body></html>"
  );


  $templateCache.put('edup-login',
    "<div class=login-container><div id=output></div><div class=avatar></div><div class=form-box><form name=form action=j_security_check method=post ng-submit=login(event)><input name=j_username placeholder=username id=text ng-model=username required> <input type=password name=j_password placeholder=password id=password ng-model=password required> <button class=\"btn btn-info btn-block login\" type=submit>Login</button></form></div></div>"
  );


  $templateCache.put('tabs-calendar',
    "<div class=mainForm ng-controller=CalendarTabController><div class=\"col-md-12 column\"><div class=\"col-md-3 form-inline\"><div class=\"btn-group pull-left\"><button class=\"btn btn-primary\" data-calendar-nav=prev>&lt;&lt; Prev</button> <button class=btn data-calendar-nav=today>Today</button> <button class=\"btn btn-primary\" data-calendar-nav=next>Next &gt;&gt;</button></div></div><div class=\"col-md-2 form-inline\"><div class=text-center>{{dateTitle}}</div></div><div class=\"col-md-3 form-inline\"><div class=\"btn-group pull-right\"><button class=\"btn btn-warning\" data-calendar-view=year>Year</button> <button class=\"btn btn-warning\" data-calendar-view=month>Month</button> <button class=\"btn btn-warning\" data-calendar-view=week>Week</button> <button class=\"btn btn-warning\" data-calendar-view=day>Day</button></div></div><div class=\"col-md-4 form-inline\"><h4>Events</h4></div></div><div class=\"col-md-12 column\"><div class=\"col-md-8 column\"><div id=calendar class=cal-context style=\"width: 60%\"></div></div><div class=\"col-md-4 column\"><ul class=\"nav nav-list\"><li><a href=\"http://www.example.com/\">This is warning class event with very long title to check how it fits to evet in day view</a></li><li><a href=\"http://www.example.com/\">This is information class</a></li><li><a href=\"http://www.example.com/\">Event that ends on timeline</a></li><li><a href=\"http://www.example.com/\">This is special event</a></li><li><a href=\"http://www.example.com/\">This is success event</a></li><li><a href=\"http://www.example.com/\">Short day event</a></li><li><a href=\"http://www.example.com/\">This is simple event</a></li><li><a href=\"http://www.example.com/\">Event 3</a></li><li><a href=\"http://www.example.com/\">This is inverse event</a></li></ul></div></div></div>"
  );


  $templateCache.put('tabs-client',
    "<div class=mainForm ng-controller=ClientTabController><div class=\"row clearfix scrollbar-container\"><div class=\"col-md-7 column\"><div class=parent><div class=\"child pull=left\"><input id=searchinput name=searchinput type=search placeholder=search class=\"form-control input-md\"></div><div class=\"child pull-right\"><h4><span class=\"glyphicon glyphicon-user pull-right\"></span></h4></div></div><table class=\"table table-hover\"><thead><tr><th>Name</th><th>Last name</th><th>Age</th><th>ID</th><th>Phone</th><th>Active</th><th></th></tr></thead><tbody><tr ng-repeat=\"client in clients\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\" ng-click=setSelected(client.id) ng-class=\"{selected: client.id === selectedClient.id}\"><td>{{ client.name }}</td><td>{{ client.lastName }}</td><td>{{ client.age }}</td><td>{{ client.personalId }}</td><td>{{ client.phone }}</td><td><input type=checkbox name=checkboxes id=checkboxes-1 ng-disabled=true ng-checked=client.active></td><td><a href=#>{{ client.details }}</a></td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div><div class=\"col-md-5 column\"><form class=form-horizontal style=\"border: 1px solid #d3d3d3;background-color: #ededed; border-radius: 5px; padding: 10px\"><fieldset><legend>Identification card</legend><div class=\"col-md-12 column\"><table class=identification-card width=100%><tr><td><h4><b>{{ selectedClient.name }} {{ selectedClient.lastName}}</b></h4></td><td rowspan=2 width=180px><img alt=140x140 src=./../images/taja.png class=\"img-rounded pull-right\"></td></tr><tr><td><h4>{{ selectedClient.personalId }}</h4></td></tr></table></div><div class=\"col-md-12 column\" style=\"padding-top: 20px\"><table class=identification-card width=100%><tr><td>Phone number:</td><td>{{ selectedClient.phone }}</td></tr><tr><td>Current balance:</td><td>{{ selectedClient.balance }} EUR</td><td><button type=button class=\"btn btn-success pull-right\">Add to balance</button></td></tr></table></div><div class=\"col-md-12 column\" style=\"padding-top: 10px\"><button type=button class=\"btn btn-info\" style=\"width: 100%\">Attendance history</button></div><div class=\"col-md-12 column\" style=\"padding-top: 30px\"><label for=parentInformation>Information about parents</label><textarea readonly class=\"form-control fixedTextArea\" id=parentInformation name=textarea>{{ selectedClient.parentsInfo }}</textarea></div><div class=\"col-md-12 column\" style=\"padding-top: 30px; padding-bottom: 20px\"><label for=essentialInformation>Essential information</label><textarea readonly class=\"form-control fixedTextArea\" id=essentialInformation name=textarea>{{ selectedClient.essentialInformation }}</textarea></div></fieldset></form></div></div></div>"
  );

}]);
