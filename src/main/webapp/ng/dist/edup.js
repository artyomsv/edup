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
        Restangular.one('ping').get().then(function (result) {
            $scope.appName = result.app;
            $scope.appVersion = result.version;
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

angular.module('edup',
    [
        'edup.common',
        'edup.login',
        'edup.header',
        'restangular'
    ]
);
angular.module('edup').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('edup-header',
    "<div ng-controller=HeaderController>{{ appName }} : {{ appVersion }}</div>"
  );


  $templateCache.put('edup-login',
    "<div class=container><div class=login-container><div id=output></div><div class=avatar></div><div class=form-box><form action=j_security_check method=post ng-submit=login(event)><input name=j_username placeholder=username ng-model=username required> <input type=j_password placeholder=password id=password ng-model=password required> <button class=\"btn btn-info btn-block login\" type=submit>Login</button></form></div></div></div>"
  );

}]);
