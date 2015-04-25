'use strict';
/* jshint node: true */

module.exports = function (grunt) {
    require('load-grunt-tasks')(grunt);

    var testsuite = grunt.option('suite') || 'full';

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        dist: 'dist',
        karmaConfig: 'karma.conf.js',
        dest: '<%= dist %>/<%= pkg.name %>',
        banner: '/*! <%= pkg.description %>#<%= pkg.version %> Copyright ASV*/\n',

        jshint: {
            options: {
                jshintrc: '.jshintrc',
                reporter: require('jshint-stylish')
            },
            files: ['<%= concat.js.src %>']
        },

        less: {
            bootstrap: {
                options: {
                    paths: ['app/style/less/bootstrap']
                },
                files: {
                    '<%= dest %>-bootstrap.css': 'app/style/less/bootstrap/edup-bootstrap.less'
                }
            },
            edup: {
                options: {
                    paths: ['app/style/less/edup'],
                    strictImports: true,
                    strictMath: true,
                    banner: '<%= banner %>'
                },
                files: {
                    '<%= dest %>.css': 'app/style/less/edup/edup.less'
                }
            }
        },

        karma: {
            run: {
                configFile: '<%= karmaConfig %>',
                autoWatch: true,
                singleRun: true
            },
            auto: {
                configFile: '<%= karmaConfig %>',
                autoWatch: true,
                singleRun: true
            }
        },

        concat: {
            js: {
                src: [
                    'app/common/common.module.js',
                    'app/common/**/*.js',

                    'app/login/login.module.js',
                    'app/login/**/*.js',

                    'app/header/header.module.js',
                    'app/header/**/*.js',

                    'app/client/client.module.js',
                    'app/client/**/*.js',

                    'app/app.module.js',

                    '!app/**/*.spec.js'

                ],
                dest: '<%= dest %>.js'
            },
            bundle: {
                src: [
                    'vendor/bower_components/jquery/dist/jquery.js',
                    'vendor/bower_components/jquery-placeholder/jquery.placeholder.js',
                    'vendor/bower_components/lodash/dist/lodash.js',
                    'vendor/bower_components/angular/angular.js',
                    'vendor/bower_components/angular-sanitize/angular-sanitize.js',
                    'vendor/bower_components/restangular/dist/restangular.js',
                    'vendor/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',

                    'vendor/bower_components/bootstrap/js/tooltip.js',
                    'vendor/bower_components/ng-sortable/dist/ng-sortable.js',
                    'vendor/bower_components/bootstrap/js/dropdown.js',

                    //'vendor/bower_components/angular-ui-utils/highlight.js',

                    '<%= dest %>.js'
                ],
                dest: '<%= dest %>-bundle.js'
            }
        },

        copy: {
            images: {
                expand: true,
                cwd: 'assets/img',
                src: ['**'],
                dest: '<%= dist %>/img/'

            }
        },

        complexity: {
            generic: {
                src: '<%= concat.js.src %>',
                //exclude: []
                options: {
                    breakOnErrors: true,
                    errorsOnly: false,
                    cyclomatic: [3, 7, 12],
                    halstead: [9, 10, 20],
                    maintainability: 90,
                    hideComplexFunctions: false,
                    broadcast: false
                }
            }
        },

        cssmin: {
            src: {
                files: [
                    {
                        expand: true,
                        src: ['<%= dest %>.css'],
                        ext: '.min.css'
                    },
                    {
                        expand: true,
                        src: ['<%= dest %>-bootstrap.css'],
                        ext: '.min.css'
                    }
                ]
            }
        },

        uglify: {
            options: {
                banner: '<%= banner %>',
                sourceMap: false
            },
            js: {
                files: {
                    '<%= dest %>.min.js': ['<%= concat.js.dest %>']
                }
            },
            bundle: {
                files: {
                    '<%= dest %>-bundle.min.js': ['<%= dest %>-bundle.js']
                }
            }
        },

        clean: {
            dist: {
                src: ['<%= dist %>']
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            src: {
                files: [
                    {
                        src: '<%= concat.js.dest %>'
                    }
                ]
            }
        },

        exec: {
            sync: 'mvn -f ../../../../pom.xml org.apache.maven.plugins:maven-antrun-plugin:1.3:run'
        },

        watch: {
            app: {
                files: ['app/**/*.js', 'app/**/*.html', '!app/**/*.spec.js'],
                tasks: ['dist:js']
            },
            'edup-less': {
                files: ['app/style/less/edup/**/*'],
                tasks: ['less:edup', 'cssmin']
            },
            'bootstrap-less': {
                files: ['app/style/less/bootstrap/**/*'],
                tasks: ['less:bootstrap', 'cssmin']
            }
        },

        ngtemplates: {
            'templates': {
                src: ['app/**/*.html'],
                dest: '<%= concat.js.dest %>',
                options: {
                    module: 'edup',
                    append: true,
                    htmlmin: {
                        collapseBooleanAttributes: true,
                        collapseWhitespace: true,
                        removeAttributeQuotes: true,
                        removeComments: true,
                        removeEmptyAttributes: true,
                        removeRedundantAttributes: true,
                        removeScriptTypeAttributes: true,
                        removeStyleLinkTypeAttributes: true
                    },
                    url: function (url) {
                        return url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.template.html'));
                    }
                }
            }
        },

        connect: {
            server: {
                options: {
                    port: 8088,
                    hostname: '127.0.0.1',
                    open: 'http://127.0.0.1:8088/standalone'
                }
            }
        },

        protractor: {
            options: {
                configFile: 'test/protractor.conf.js',
                keepAlive: true,
                ngColor: false
            },
            suite: {
                options: {
                    args: {
                        suite: '<%= testsuite %>'
                    }
                }
            }
        }
    });

    grunt.registerTask('test', ['karma:run']);
    grunt.registerTask('test:run', ['karma:run']);
    grunt.registerTask('test:auto', ['karma:auto']);
    grunt.registerTask('lint', ['jshint']);
    grunt.registerTask('dist:js', ['concat:js', 'ngAnnotate:src', 'ngtemplates', 'uglify:js']);
    grunt.registerTask('dist:style', ['less:bootstrap', 'less:edup', 'cssmin']);
    grunt.registerTask('dist', ['lint', 'complexity', 'clean', 'dist:js', 'dist:style', 'concat:bundle', 'uglify:bundle', 'copy']);
    grunt.registerTask('standalone', ['clean', 'dist', 'connect', 'watch']);
    grunt.registerTask('e2e', ['protractor:suite']);

};