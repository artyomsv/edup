'use strict';

angular.module('edup.common', [
        'edup'
    ]
);
'use strict';

angular.module('edup.login', [
        'edup'
    ]
);
'use strict';

angular.module('edup.login')

    .directive('edupLogin', function () {
        return {
            restrict: 'E',
            templateUrl: 'edup-login',
            link: function (scope) {
                scope.test = 'test';
            }
        };
    }
);

'use strict';

angular.module('edup',
    [
        'edup.common',
        'edup.login'
    ]
);
angular.module('edup').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('edup-login',
    "<form name=form ng-submit=login() role=form action=j_security_check method=post><div class=form-group><label for=username>Username</label><i class=\"fa fa-key\"></i> <input name=j_username id=username class=form-control ng-model=username required> <span ng-show=\"form.username.$dirty && form.username.$error.required\" class=help-block>Username is required</span></div><div class=form-group><label for=password>Password</label><i class=\"fa fa-lock\"></i> <input type=password name=j_password id=password class=form-control ng-model=password required> <span ng-show=\"form.password.$dirty && form.password.$error.required\" class=help-block>Password is required</span></div><div class=form-actions><button type=submit ng-disabled=\"form.$invalid || dataLoading\" class=\"btn btn-danger\">Login</button> <button type=reset ng-disabled=\"form.$invalid || dataLoading\" class=\"btn btn-danger\">Reset</button> <img ng-if=dataLoading src=\"data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==\"></div></form>"
  );

}]);
