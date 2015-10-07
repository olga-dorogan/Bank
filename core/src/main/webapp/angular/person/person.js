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

    .controller('PersonCtrl', ['$scope', '$route', '$modal', 'clientRest',
        function ($scope, $route, $modal, clientRest) {
            var clients = [];
            clientRest.query(function (data) {
                $scope.clients = data;
                clients = data;
            });
            $scope.newClient = {};
            $scope.addClient = function () {
                if ($scope.addPersonForm.$valid && $scope.isValidNewClient()) {
                    clientRest.save($scope.newClient,
                        function (success) {
                            $route.reload();
                        },
                        function (error) {
                            showAlertWithError('Произошла ошибка на сервере');
                        }
                    );
                } else {
                    if (!isValidPassport()) {
                        showAlertWithError('Паспотрные данные должны быть уникальными');
                    }
                    $scope.addPersonForm.submitted = true;
                }
            };
            $scope.search = '';
            $scope.searchByPassport = function () {
                $scope.clients = clients.filter(function (value) {
                    return value.passport.indexOf($scope.search) == 0;
                });
            };
            $scope.predicate = 'surname';
            $scope.reverse = true;
            $scope.order = function (predicate) {
                $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
                $scope.predicate = predicate;
            };
            $scope.isValidNewClient = function () {
                return isValidName() && isValidPassport();
            };
            $scope.isValidPassport = isValidPassport;
            var isValidName = function () {
                return ($scope.newClient.lastName != undefined) && ($scope.newClient.lastName != '') &&
                    ($scope.newClient.firstName != undefined) && ($scope.newClient.firstName != '');
            };
            var isValidPassport = function () {
                var isFilled = ($scope.newClient.passport != undefined) && ($scope.newClient.passport != '');
                if (!isFilled) {
                    return false;
                }
                var isUnique = true;
                for (var i = 0; i < clients.length; i++) {
                    if ($scope.newClient.passport.indexOf(clients[i].passport) == 0) {
                        isUnique = false;
                        break;
                    }
                }
                return isUnique;
            };
            $scope.passportPattern = (function () {
                var regexp = /^[А-Я]{2}[0-9]{6}$/;
                return {
                    test: function (value) {
                        if ($scope.requirePassport === false) {
                            return true;
                        }
                        return regexp.test(value);
                    }
                };
            })();
            var showAlertWithError = function (msg) {
                var alertData = {
                    boldTextTitle: "Ошибка",
                    mode: 'danger',
                    textAlert: msg
                };
                var modalInstance = $modal.open(
                    {
                        templateUrl: 'angular/templates/alertModal.html',
                        controller: function ($scope, $modalInstance) {
                            $scope.data = alertData;
                            $scope.close = function () {
                                $modalInstance.close();
                            }
                        },
                        backdrop: true,
                        keyboard: true,
                        backdropClick: true,
                        size: 'sm'
                    }
                );
            };
        }])

    .controller('AccountCtrl', [
        '$routeParams', '$route', '$scope', '$modal', 'accountRest', 'clientRest', 'transactionRest',
        function ($routeParams, $route, $scope, $modal, accountRest, clientRest, transactionRest) {
            accountRest.query({id: $routeParams.clientId}, function (accounts) {
                clientRest.get({id: $routeParams.clientId}, function (client) {
                    $scope.accounts = accounts;
                    $scope.client.firstName = client.firstName;
                    $scope.client.lastName = client.lastName;

                    $scope.transaction.accountFrom.id = getIdOfBestAccount($scope.accounts);
                    $scope.transaction.accountTo.id = getIdOfWorstAccount($scope.accounts);
                    $scope.transaction.amount = 0;
                    $scope.transactionMax = $scope.getAccountState($scope.transaction.accountFrom.id);

                    $scope.credit.accountTo.id = getIdOfWorstAccount($scope.accounts);
                    $scope.debit.accountFrom.id = getIdOfBestAccount($scope.accounts);
                    $scope.credit.amount = 0;
                    $scope.debit.amount = 0;
                    $scope.debitMax = $scope.getAccountState($scope.debit.accountFrom.id);

                });
            });
            $scope.reverse = false;
            $scope.client = {};
            $scope.newAccount = {};
            $scope.addAccount = function () {
                if ($scope.addAccountForm.$valid && $scope.isAccountTitleUnique()) {
                    accountRest.save(
                        {id: $routeParams.clientId},
                        $scope.newAccount,
                        function (success) {
                            $route.reload();
                        },
                        function (error) {
                            showAlertWithError('Произошла ошибка на сервере');
                        });
                } else {
                    if (!$scope.isAccountTitleUnique()) {
                        showAlertWithError('Название счета должно быть уникальным');
                    }
                    $scope.addAccountForm.submitted = true;
                }
            };
            $scope.isAccountTitleUnique = function () {
                var res = true;
                for (var i = 0; i < $scope.accounts.length; i++) {
                    if ($scope.accounts[i].title.indexOf($scope.newAccount.title) == 0) {
                        res = false;
                        break;
                    }
                }
                return res;
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
                    if (accounts[i].amount <= worst.amount) {
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

            $scope.changeDebitMax = function () {
                $scope.debitMax = $scope.getAccountState($scope.debit.accountFrom.id);
            };

            $scope.changeTransactionMax = function () {
                $scope.transactionMax = $scope.getAccountState($scope.transaction.accountFrom.id);
            };

            $scope.doTransaction = function () {
                if ($scope.isTransferAccountsValid() && $scope.isTransferAmountValid()) {
                    doTransactionByType("TRANSACTION", $scope.transaction);
                } else {
                    $scope.addTransferForm.submitted = true;
                }
            };
            $scope.isTransferAccountsValid = function () {
                return $scope.transaction.accountFrom.id != $scope.transaction.accountTo.id;
            };
            $scope.isTransferAmountValid = function () {
                return $scope.transaction.amount < $scope.transactionMax;
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
                transactionRest.save(data,
                    function (success) {
                        $route.reload();
                    });
            };
            var getCurrentDate = function () {
                var date = new Date();
                // Date format ---- 'yyyy-MM-dd HH:mm:ss'
                var formattedDate = date.getFullYear() + '-' +
                    (date.getMonth() + 1 - Math.floor((Math.random() * 12))) + '-' + date.getDate();
                formattedDate += ' ';
                formattedDate += date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
                return formattedDate;
            };

            var showAlertWithError = function (msg) {
                var alertData = {
                    boldTextTitle: "Ошибка",
                    mode: 'danger',
                    textAlert: msg
                };
                var modalInstance = $modal.open(
                    {
                        templateUrl: 'angular/templates/alertModal.html',
                        controller: function ($scope, $modalInstance) {
                            $scope.data = alertData;
                            $scope.close = function () {
                                $modalInstance.close();
                            }
                        },
                        backdrop: true,
                        keyboard: true,
                        backdropClick: true,
                        size: 'sm'
                    }
                );
            };

        }])

    .factory('clientRest', function ($resource) {
        return $resource('client/:id');
    })
    .factory('accountRest', function ($resource) {
        return $resource('client/:id/account');
    });