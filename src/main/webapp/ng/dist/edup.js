'use strict';

angular.module('edup.common', ['restangular']);
'use strict';

angular.module('edup.common')

    .config(['RestangularProvider', function (RestangularProvider) {

        var baseUrl = window.location.host + '/edup/api';

        if (baseUrl.indexOf('http://127.0.0.1:8088/') > -1) {
            RestangularProvider.setBaseUrl('https://192.168.1.104:8443/edup/api');
        } else {
            RestangularProvider.setBaseUrl(baseUrl);
        }

        RestangularProvider.setErrorInterceptor(function (resp) {
            console.log(angular.toJson(resp, true));
            return false;
        });

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

    .controller('HeaderController', ['$scope', 'Restangular', function ($scope, Restangular) {
        $scope.appName = 'unknown';
        $scope.appVersion = 'unknown';
        Restangular.one('ping').get().then(
            function (result) {
                $scope.appName = result.app;
                $scope.appVersion = result.version;
            }
        );
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

angular.module('edup',
    [
        'edup.common',
        'edup.login',
        'edup.header',
        'edup.client',
        'restangular'
    ]
);
angular.module('edup').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('client-attendance',
    "<div class=container style=\"background-color: #f4f4f4; padding-bottom: 20px; padding-top: 20px\" ng-controller=ClientAttendanceController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ student }}</span></h3></td><td rowspan=2 style=\"border-top: none\" width=180px><img alt=140x140 src=./../images/taja.png class=\"img-rounded pull-right\"></td></tr><tr class=active><td style=\"border-top: none\"><h3><span class=\"label pull-right\" ng-class=\"{'label-danger' :  balance < 0, 'label-success' :  balance > 0, 'label-primary' :  balance == 0}\">{{ balance }} EUR</span></h3></td></tr></table></div></div><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>History of attendances</legend><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Subject name</th><th>Date</th><th>Amount</th></tr></thead><tbody><tr ng-repeat=\"event in attendanceHistory\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td>{{ event.subject }}</td><td>{{ event.date }}</td><td>{{ event.amount }} EUR</td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div></fieldset><div class=\"row clearfix\"><div class=\"col-md-12 column\"><button type=button class=\"btn btn-success pull-right\">Add new attendance</button></div></div></div>"
  );


  $templateCache.put('client-balance',
    "<div class=container style=\"background-color: #f4f4f4; padding-bottom: 20px; padding-top: 20px\" ng-controller=ClientBalanceController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ student }}</span></h3></td><td rowspan=2 style=\"border-top: none\" width=180px><img alt=140x140 src=./../images/taja.png class=\"img-rounded pull-right\"></td></tr><tr class=active><td style=\"border-top: none\"><h3><span class=\"label pull-right\" ng-class=\"{'label-danger' :  balance < 0, 'label-success' :  balance > 0, 'label-primary' :  balance == 0}\">{{ balance }} EUR</span></h3></td></tr></table></div></div><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>Add money to account</legend><div class=row><div class=col-xs-4 style=\"border: solid\">1</div><div class=col-xs-8 style=\"border: solid\">2</div><div class=col-xs-12 style=\"border: solid\"><button type=button class=\"btn btn-success pull-right\">Add to balance</button></div></div></fieldset><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>Make payment</legend><div class=row><div class=col-xs-4>1</div><div class=col-xs-8>2</div><div class=col-xs-4>3</div><div class=col-xs-8>4</div><div class=col-xs-12><button type=button class=\"btn btn-success pull-right\">Pay</button></div></div></fieldset></div>"
  );


  $templateCache.put('client-documents',
    "<div class=container style=\"background-color: #f4f4f4; padding-bottom: 20px; padding-top: 20px\" ng-controller=ClientDocumentsController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ student }}</span></h3></td><td style=\"border-top: none\" width=180px><img alt=140x140 src=./../images/taja.png class=\"img-rounded pull-right\"></td></tr></table></div></div><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>List of documents</legend><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Document name</th><th>Date loaded</th></tr></thead><tbody><tr ng-repeat=\"document in documents\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td>{{ document.name }}</td><td>{{ document.date }}</td><td><a href=\"{{ document.link }}\">Download</a></td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div></fieldset><div class=\"row clearfix\"><div class=\"col-md-12 column\"><button type=button class=\"btn btn-success pull-right\">Add new document</button></div></div></div>"
  );


  $templateCache.put('edup-header',
    "<div ng-controller=HeaderController>{{ appName }} : {{ appVersion }}</div>"
  );


  $templateCache.put('edup-login',
    "<div class=container><div class=login-container><div id=output></div><div class=avatar></div><div class=form-box><form name=form action=j_security_check method=post ng-submit=login(event)><input name=j_username placeholder=username id=text ng-model=username required> <input type=password name=j_password placeholder=password id=password ng-model=password required> <button class=\"btn btn-info btn-block login\" type=submit>Login</button></form></div></div></div>"
  );

}]);
