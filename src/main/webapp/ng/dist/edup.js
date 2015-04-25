'use strict';

angular.module('edup.common', ['restangular']);
'use strict';

angular.module('edup.common')

    .config(['RestangularProvider', function (RestangularProvider) {

        RestangularProvider.setBaseUrl('https://localhost:8443/edup/api');
        //RestangularProvider.setBaseUrl('https://192.168.1.104:8443/edup/api');

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
        $scope.balance = 25;

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

    .directive('clientAttendance', function () {
        return {
            restrict: 'E',
            templateUrl: 'client-attendance',
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
    "<div class=container style=\"background-color: #f4f4f4; padding-bottom: 20px; padding-top: 20px\" ng-controller=ClientAttendanceController><div class=\"row clearfix\"><div class=\"col-md-12 column\"><table class=pull-right width=400px><tr><td style=\"border-top: none\"><h3><span class=\"label label-info pull-right\">{{ student }}</span></h3></td><td rowspan=2 style=\"border-top: none\" width=180px><img alt=140x140 src=./../images/taja.png class=img-rounded style=\"float: right\"></td></tr><tr class=active><td style=\"border-top: none\"><h3><span class=\"label pull-right\" ng-class=\"{'label-danger' :  balance < 0, 'label-success' :  balance > 0, 'label-primary' :  balance == 0}\">{{ balance }} EUR</span></h3></td></tr></table></div></div><fieldset class=\"row clearfix scheduler-border\"><legend class=scheduler-border>History of attendances</legend><div class=\"col-md-12 column\"><table class=\"table table-hover\"><thead><tr><th>Subject name</th><th>Date</th><th>Amount</th></tr></thead><tbody><tr ng-repeat=\"event in attendanceHistory\" ng-class-odd=\"'success'\" ng-class-even=\"'active'\"><td>{{ event.subject }}</td><td>{{ event.date }}</td><td>{{ event.amount }} EUR</td></tr></tbody></table><div class=text-center><ul class=\"pagination pagination-sm\"><li><a href=#>Prev</a></li><li><a href=#>1</a></li><li><a href=#>2</a></li><li><a href=#>3</a></li><li><a href=#>4</a></li><li><a href=#>5</a></li><li><a href=#>Next</a></li></ul></div></div></fieldset><div class=\"row clearfix\"><div class=\"col-md-12 column\"><button type=button class=\"btn btn-default\" style=\"float: right\">Add new attendance</button></div></div></div>"
  );


  $templateCache.put('edup-header',
    "<div ng-controller=HeaderController>{{ appName }} : {{ appVersion }}</div>"
  );


  $templateCache.put('edup-login',
    "<div class=container><div class=login-container><div id=output></div><div class=avatar></div><div class=form-box><form name=form action=j_security_check method=post ng-submit=login(event)><input name=j_username placeholder=username id=text ng-model=username required> <input type=password name=j_password placeholder=password id=password ng-model=password required> <button class=\"btn btn-info btn-block login\" type=submit>Login</button></form></div></div></div>"
  );

}]);
