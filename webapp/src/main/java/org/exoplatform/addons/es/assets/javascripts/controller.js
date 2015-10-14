/**
 * Created by TClement on 10/5/15.
 */
'use strict';

var indexingManagementController = angular.module('indexingManagementController', []);

indexingManagementController.controller('IndexingStatCtrl', ['$scope', '$interval', 'Stat',
    function($scope, $interval, Stat) {

        Stat.getNbConnectors().success(function(data) {
            $scope.nbConnectors = data;
        });

        //Loads and populates the stats
        this.loadStats = function (){

            Stat.getNbIndexingOperation().success(function(data) {
                $scope.nbOperations = data;
            });

            Stat.getNbIndexingError().success(function(data) {
                $scope.nbErrors = data;
            });

        }
        //Put in interval, first trigger after 1 seconds
        $interval(function(){
            this.loadStats();
        }.bind(this), 1000);

        //invoke initialy
        this.loadStats();

    }
]);

indexingManagementController.controller('ConnectorListCtrl', ['$scope', 'Connector',
    function($scope, Connector) {

        Connector.getConnectors().success(function(data) {
            $scope.connectors = data;
        });

        $scope.reindexConnector = function(connectorType) {
            Connector.reindexConnector(connectorType).then(function(response) {
                console.log("Reindex request response = "+response)
            });
        };

        $scope.disableIndexConnector = function(connectorType) {
            Connector.disableIndexConnector(connectorType).then(function(response) {
                console.log("Disable request response = "+response)
            });
        };

        $scope.enableIndexConnector = function(connectorType) {
            Connector.enableIndexConnector(connectorType).then(function(response) {
                console.log("Disable request response = "+response)
            });
        };

    }
]);

indexingManagementController.controller('ErrorListCtrl', ['$scope', '$interval', 'Error',
    function($scope, $interval, Error) {

        //Loads and populates the Error list
        this.loadErrorList = function (){

            Error.getErrors().success(function(data) {
                $scope.errors = data;
            });

        }
        //Put in interval, first trigger after 5 seconds
        $interval(function(){
            this.loadErrorList();
        }.bind(this), 5000);

        //invoke initialy
        this.loadErrorList();

        $scope.addToQueue = function(id) {
            Error.addToQueue(id).then(function(response) {
                console.log("AddToQueue request response = "+response)
            });
        };

    }
]);

indexingManagementController.controller('OperationListCtrl', ['$scope', '$interval', 'Operation',
    function($scope, $interval, Operation) {

        //Loads and populates the operation list
        this.loadOperationList = function (){

            Operation.getOperations().success(function(data) {
                $scope.operations = data;
            });

        }
        //Put in interval, first trigger after 5 seconds
        $interval(function(){
            this.loadOperationList();
        }.bind(this), 5000);

        //invoke initialy
        this.loadOperationList();

        $scope.deleteOperation = function(id) {
            Operation.deleteOperation(id).then(function(response) {
                console.log("deleteOperation request response = "+response)
            });
        };

    }
]);
