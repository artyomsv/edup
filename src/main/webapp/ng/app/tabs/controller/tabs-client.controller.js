'use strict';

angular.module('edup.tabs')

    .controller('ClientTabController', function ($scope) {
        $scope.student = 'Taisija Polakova';
        $scope.balance = 25;//Math.floor((Math.random() * 100) - 50);

        $scope.clients = [
            {
                'name': 'Artyom',
                'lastName' : 'Stukans',
                'age' : 33,
                'id' : '281281-10562',
                'phone' : '28 61 81 25',
                'active' : true,
                'details' : 'details',
                'balance' : 35,
                'parentsInfo' : 'Information about Artyom parents',
                'essentialInformation' : 'Some essential information about student'
            },
            {
                'name': 'Julija',
                'lastName' : 'Avdejeva',
                'age' : 30,
                'id' : '220484-12345',
                'phone' : '28 78 45 90',
                'active' : false,
                'details' : 'details',
                'balance' : 25,
                'parentsInfo' : 'Information about Yuliya parents',
                'essentialInformation' : 'Some essential information about student'
            },
            {
                'name': 'Taisija',
                'lastName' : 'Polakova',
                'age' : 3,
                'id' : '231111-12345',
                'phone' : '28 89 00 12',
                'active' : true,
                'details' : 'details',
                'balance' : 15,
                'parentsInfo' : 'Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents ',
                'essentialInformation' : 'Some essential information about student'
            }

        ];

        $scope.selectedClient = $scope.clients[2];
    }
);

