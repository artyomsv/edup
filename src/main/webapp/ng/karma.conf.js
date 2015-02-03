'use strict';

module.exports = function(config){
  config.set({

    basePath : '.',

    files : [
      'vendor/bower_components/jquery/dist/jquery.js',
      'vendor/bower_components/lodash/dist/lodash.js',
      'vendor/bower_components/angular/angular.js',
      'vendor/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
      'vendor/bower_components/restangular/dist/restangular.js',
      'vendor/bower_components/bootstrap/js/tooltip.js',

      'node_modules/angular-mocks/angular-mocks.js',
      'app/components/**/*.js',
      'app/view*/**/*.js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-junit-reporter'
            ],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};
