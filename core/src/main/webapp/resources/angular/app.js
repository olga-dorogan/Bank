// Declare app level module which depends on views
angular.module('bankApp', [
    'ngRoute',
    'bankApp.person',
    'bankApp.transaction'
])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.otherwise({redirectTo: '/person'});
    }])
    .factory('transactionRest', function ($resource) {
        return $resource('transaction');
    });