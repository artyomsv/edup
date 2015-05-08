'use strict';

angular.module('edup.tabs')

    .controller('ClientTabController', function ($scope) {
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
    }
);

