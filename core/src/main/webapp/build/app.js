// Declare app level module which depends on views
angular.module('bankApp', [
    'ngRoute',
    'bankApp.person',
    'bankApp.transaction',
    'ui.bootstrap'
])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.otherwise({redirectTo: '/person'});
    }])
    .factory('transactionRest', function ($resource) {
        return $resource('transaction');
    });
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

    .controller('PersonCtrl', ['$scope', '$route', '$modal', 'clientRest', function ($scope, $route, $modal, clientRest) {
        var clients = [];
        clientRest.query(function (data) {
            $scope.clients = data;
            clients = data;
        });
        $scope.newClient = {};
        $scope.addClient = function () {
            if ($scope.isValidNewClient()) {
                clientRest.save($scope.newClient,
                    function (success) {
                        $route.reload();
                    },
                    function (error) {
                        showAlertWithError('Произошла ошибка на сервере');
                    }
                );
            } else {
                showAlertWithError(
                    'Фамилия, имя, номер и серия паспорта должны быть заполнены.' +
                    'Возможно, клиент с указанными паспортными данными уже существует.');
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
        '$routeParams', '$route', '$scope', 'accountRest', 'clientRest', 'transactionRest',
        function ($routeParams, $route, $scope, accountRest, clientRest, transactionRest) {
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
                    console.log("Form is valid");
                    accountRest.save({id: $routeParams.clientId}, $scope.newAccount);
                    $route.reload();
                } else {
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

        }])
    .factory('clientRest', function ($resource) {
        return $resource('client/:id');
    })
    .factory('accountRest', function ($resource) {
        return $resource('client/:id/account');
    });
angular.module('bankApp.transaction', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/transaction', {
            templateUrl: 'angular/transaction/transaction.html',
            controller: 'TransactionCtrl'
        });
    }])

    .controller('TransactionCtrl', ['$scope', 'transactionRest', function ($scope, transactionRest) {
        transactionRest.query(function (data) {
            $scope.transactions = convertTransactions(data);
            allTransactions = $scope.transactions;
            $scope.interval.startDate =  getMinDate();
            $scope.interval.endDate =  new Date();
        });


        var getTransactionType = function (transaction) {
            if (transaction.accountFrom == null) {
                return 'CREDIT';
            }
            if (transaction.accountTo == null) {
                return 'DEBIT';
            }
            return 'TRANSACTION';
        };

        var allTransactions = [];
        $scope.search = '';
        $scope.searchByName = function () {
            $scope.transactions = allTransactions.filter(function (value) {
                return value.clientName.indexOf($scope.search) == 0;
            });
        };
        $scope.intervalChanged = function () {
            var scopedTransactions = [];
            for (var i = 0; i < $scope.transactions.length; i++) {
                if (isTransactionInsideInterval($scope.transactions[i])) {
                    scopedTransactions.push($scope.transactions[i]);
                }
            }
            $scope.transactions = scopedTransactions;
            console.log("Date interval was changed");
        };
        $scope.interval = {};

        var getMinDate = function () {
            if ($scope.transactions.length == 0) {
                return new Date();
            }
            var minDate = $scope.transactions[0].date;
            for (var i = 1; i < $scope.transactions.length; i++) {
                if ($scope.transactions[i].date < minDate) {
                    minDate = $scope.transactions[i].date;
                }
            }
            return minDate;
        };

        var isTransactionInsideInterval = function (transaction) {
            return (transaction.date >= $scope.interval.startDate) &&
                (transaction.endDate <= $scope.interval.endDate);
        };

        var convertTransactions = function (transFromServer) {
            var transactions = [];
            var transactionClientSide = {};
            for (var i = 0; i < transFromServer.length; i++) {
                console.log(JSON.stringify(transFromServer[i]));
                transactionClientSide.date = transFromServer[i].date;
                transactionClientSide.amount = transFromServer[i].amount;
                switch (getTransactionType(transFromServer[i])) {
                    case 'CREDIT':
                        transactionClientSide.clientId = transFromServer[i].clientTo.id;
                        transactionClientSide.clientName = transFromServer[i].clientTo.lastName + ' ' + transFromServer[i].clientTo.firstName;
                        transactionClientSide.accountTitle = transFromServer[i].accountTo.title;
                        transactionClientSide.type = 'Зачисление';
                        transactions.push(JSON.parse(JSON.stringify(transactionClientSide)));
                        break;
                    case 'DEBIT':
                        transactionClientSide.clientId = transFromServer[i].clientFrom.id;
                        transactionClientSide.clientName = transFromServer[i].clientFrom.lastName + ' ' + transFromServer[i].clientFrom.firstName;
                        transactionClientSide.accountTitle = transFromServer[i].accountFrom.title;
                        transactionClientSide.type = 'Списание';
                        transactions.push(JSON.parse(JSON.stringify(transactionClientSide)));
                        break;
                    case 'TRANSACTION':
                        transactionClientSide.clientId = transFromServer[i].clientTo.id;
                        transactionClientSide.clientName = transFromServer[i].clientTo.lastName + ' ' + transFromServer[i].clientTo.firstName;
                        transactionClientSide.accountTitle = transFromServer[i].accountTo.title;
                        transactionClientSide.type = 'Зачисление; перевод';
                        transactions.push(JSON.parse(JSON.stringify(transactionClientSide)));
                        transactionClientSide.clientId = transFromServer[i].clientFrom.id;
                        transactionClientSide.clientName = transFromServer[i].clientFrom.lastName + ' ' + transFromServer[i].clientFrom.firstName;
                        transactionClientSide.accountTitle = transFromServer[i].accountFrom.title;
                        transactionClientSide.type = 'Списание; перевод';
                        transactions.push(JSON.parse(JSON.stringify(transactionClientSide)));
                        break;
                }
            }
            return transactions;
        };
    }])
    .controller('DatepickerCtrl', ['$scope', function DatepickerCtrl($scope) {
        $scope.today = function () {
            $scope.dt = new Date();
        };
        $scope.today();

        $scope.clear = function () {
            $scope.dt = null;
        };

        $scope.disabled = function (date, mode) {
            return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.toggleMin = function () {
            $scope.minDate = ( $scope.minDate ) ? null : new Date();
        };
        $scope.toggleMin();

        $scope.open = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.opened = true;
        };

        $scope.dateOptions = {
            'year-format': "'yy'",
            'starting-day': 1
        };

        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate'];
        $scope.format = $scope.formats[0];
    }]);