angular.module('bankApp.person', ['ngRoute', 'ngResource'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/person', {
                templateUrl: 'angular/person/person.html',
                controller: 'PersonCtrl'
            })
            .when('/person/:clientId/account', {
                templateUrl: 'angular/person/account.html',
                controller: 'AccountCtrl'
            });
    }])

    .controller('PersonCtrl', ['$scope', '$route', 'clientRest', function ($scope, $route, clientRest) {
        clientRest.query(function (data) {
            $scope.clients = data;
        });
        $scope.newClient = {};
        $scope.addClient = function () {
            clientRest.save($scope.newClient);
            $route.reload();
        };
    }])

    .controller('AccountCtrl', [
        '$routeParams', '$route', '$scope', 'accountRest', 'transactionRest',
        function ($routeParams, $route, $scope, accountRest, transactionRest) {
            accountRest.get({id: $routeParams.clientId}, function (data) {
                $scope.accounts = data.accounts;
                $scope.client.name = data.name;
                $scope.client.surname = data.surname;

                $scope.transaction.accountFrom.id = getIdOfBestAccount(data.accounts);
                $scope.transaction.accountTo.id = getIdOfWorstAccount(data.accounts);
                $scope.transaction.amount = 0;

                $scope.credit.accountTo.id = getIdOfWorstAccount(data.accounts);
                $scope.debit.accountFrom.id = getIdOfBestAccount(data.accounts);
                $scope.credit.amount = 0;
                $scope.debit.amount = 0;

            });

            $scope.client = {};
            $scope.newAccount = {};
            $scope.addAccount = function () {
                accountRest.save({id: $routeParams.clientId}, $scope.newAccount);
                $route.reload();
            };
            $scope.transaction = {"accountFrom": {}, "accountTo": {}};
            $scope.credit = {"accountTo": {}};
            $scope.debit = {"accountFrom": {}};
            $scope.debitMax = 0;
            $scope.transactionMax = 0;

            function getIdOfBestAccount(accounts) {
                if (accounts.length == 0) {
                    return null;
                }
                var best = {"id": accounts[0].id, "amount": accounts[0].amount};
                for (var i = 1; i < accounts.length; i++) {
                    if (accounts[i].amount > best.amount) {
                        best.id = accounts[i].id;
                        best.amount = accounts[i].amount;
                    }
                }
                return best.id;
            }

            function getIdOfWorstAccount(accounts) {
                if (accounts.length == 0) {
                    return null;
                }
                var worst = {"id": accounts[0].id, "amount": accounts[0].amount};
                for (var i = 1; i < accounts.length; i++) {
                    if (accounts[i].amount < worst.amount) {
                        worst.id = accounts[i].id;
                        worst.amount = accounts[i].amount;
                    }
                }
                return worst.id;
            }

            $scope.getAccountState = function (accountId) {
                if ($scope.accounts.length == 0) {
                    return 0;
                }
                for (var i = 0; i < $scope.accounts.length; i++) {
                    if ($scope.accounts[i].id == accountId) {
                        return $scope.accounts[i].amount;
                    }
                }
                return 0;
            };

            $scope.changeDebitAmount = function () {
                $scope.debitMax = $scope.getAccountState($scope.debit.accountFrom.id);
            };

            $scope.changeTransactionAmount = function () {
                $scope.transactionMax = $scope.getAccountState($scope.transaction.accountFrom.id);
            };

            $scope.doTransaction = function () {
                doTransactionByType("TRANSACTION", $scope.transaction);
            };
            $scope.creditAccount = function () {
                doTransactionByType("CREDIT", $scope.credit);
            };
            $scope.debitAccount = function () {
                doTransactionByType("DEBIT", $scope.debit);
            };
            var doTransactionByType = function (type, data) {
                data.date = getCurrentDate();
                data.type = type;
                transactionRest.save(data);
                $route.reload();
            };
            var getCurrentDate = function () {
                var date = new Date();
                // Date format ---- 'yyyy-MM-dd HH:mm:ss'
                var formattedDate = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                formattedDate += ' ';
                formattedDate += date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
                return formattedDate;
            };

        }])
    .factory('clientRest', function ($resource) {
        return $resource('client/:id');
    })
    .factory('accountRest', function ($resource) {
        return $resource('client/:id/account');
    });