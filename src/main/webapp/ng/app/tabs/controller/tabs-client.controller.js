'use strict';

angular.module('edup.tabs')

    .controller('ClientTabController', function ($scope) {

        $scope.clients = [
            {
                'id' : 1,
                'name': 'Artyom',
                'lastName' : 'Stukans',
                'fullName' : 'Artjom Stukans',
                'age' : 33,
                'birthDay' : '28/12/1981',
                'personalId' : '281281-10562',
                'phone' : '28 61 81 25',
                'active' : true,
                'details' : 'Details',
                'balance' : 35,
                'parentsInfo' : 'Information about Artyom parents',
                'essentialInformation' : 'Some essential information about student',
                'photo' : '/images/artyom.jpg'
            },
            {
                'id' : 2,
                'name': 'Julija',
                'lastName' : 'Avdejeva',
                'fullName' : 'Julija Avdejeva',
                'age' : 18,
                'birthDay' : '22/04/1997',
                'personalId' : '220497-12345',
                'phone' : '28 78 45 90',
                'active' : false,
                'details' : 'Details',
                'balance' : 25,
                'parentsInfo' : 'Information about Yuliya parents',
                'essentialInformation' : 'Some essential information about student',
                'photo' : '/images/julija.jpg'
            },
            {
                'id' : 3,
                'name': 'Taisija',
                'lastName' : 'Polakova',
                'fullName' : 'Taisija Polakova',
                'age' : 3,
                'birthDay' : '23/11/2011',
                'personalId' : '231111-12345',
                'phone' : '28 89 00 12',
                'active' : true,
                'details' : 'Details',
                'balance' : 15,
                'parentsInfo' : 'Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents Information about Taisija parents ',
                'essentialInformation' : 'Some essential information about student',
                'photo' : '/images/taja.jpg'
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

