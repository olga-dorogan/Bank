angular.module('bankApp.transaction', ['ngRoute'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/transaction', {
            templateUrl: 'angular/transaction/transaction.html',
            controller: 'TransactionCtrl'
        });
    }])

    .controller('TransactionCtrl', ['$scope', 'transactionRest', function ($scope, transactionRest) {
        transactionRest.query(function (data) {
            console.log(JSON.stringify(data));
            $scope.transactions = convertTransactions(data);
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
                        transactionClientSide.clientName = transFromServer[i].clientTo.surname + ' ' + transFromServer[i].clientTo.name;
                        transactionClientSide.accountTitle = transFromServer[i].accountTo.title;
                        transactionClientSide.type = 'Зачисление';
                        transactions.push(JSON.parse(JSON.stringify(transactionClientSide)));
                        break;
                    case 'DEBIT':
                        transactionClientSide.clientId = transFromServer[i].clientFrom.id;
                        transactionClientSide.clientName = transFromServer[i].clientFrom.surname + ' ' + transFromServer[i].clientFrom.name;
                        transactionClientSide.accountTitle = transFromServer[i].accountFrom.title;
                        transactionClientSide.type = 'Списание';
                        transactions.push(JSON.parse(JSON.stringify(transactionClientSide)));
                        break;
                    case 'TRANSACTION':
                        transactionClientSide.clientId = transFromServer[i].clientTo.id;
                        transactionClientSide.clientName = transFromServer[i].clientTo.surname + ' ' + transFromServer[i].clientTo.name;
                        transactionClientSide.accountTitle = transFromServer[i].accountTo.title;
                        transactionClientSide.type = 'Зачисление; перевод';
                        transactions.push(JSON.parse(JSON.stringify(transactionClientSide)));
                        transactionClientSide.clientId = transFromServer[i].clientFrom.id;
                        transactionClientSide.clientName = transFromServer[i].clientFrom.surname + ' ' + transFromServer[i].clientFrom.name;
                        transactionClientSide.accountTitle = transFromServer[i].accountFrom.title;
                        transactionClientSide.type = 'Списание; перевод';
                        transactions.push(JSON.parse(JSON.stringify(transactionClientSide)));
                        break;
                }
            }
            return transactions;
        };
    }]);