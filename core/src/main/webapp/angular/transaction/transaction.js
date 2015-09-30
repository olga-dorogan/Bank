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