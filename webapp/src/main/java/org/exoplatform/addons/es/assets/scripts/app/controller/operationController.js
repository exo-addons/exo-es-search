/**
 * Created by TClement on 12/15/15.
 */

define('operationController', ['SHARED/jquery', 'indexingManagementApi', 'indexingOperationResource', 'statController'],
    function($, indexingManagementApi, indexingOperationResource) {

        //Service
        var myIndexingManagementApi = new indexingManagementApi();
        //Controller
        var myStatController = new statController();

        var operationController = function operationController() {
            var self = this;

            self.init = function() {

                //Init the Operation list
                self.updateOperationList();
                initUiListener();

            }

            self.updateOperationList = function() {
                updateOperationTable();
            }

        }

        // Listener

        function initUiListener() {
            addDeleteOperationUiListener();
        }

        function addDeleteOperationUiListener() {
            //Deleting queue operation Event
            $(document).on('click.btn-operation-delete', '.btn-operation-delete', function () {

                //Get operation Id
                var jOperation = $(this);
                var jOperationId = jOperation.attr('data-operationId');

                //Trigger the deleting operation
                deleteOperation(jOperationId);
            });
        }


        // Action

        function updateOperationTable() {
            myIndexingManagementApi.getOperations(null, 0, 10, false, fillOperationTable);
        }

        function deleteOperation(indexingOperationId) {
            myIndexingManagementApi.deleteOperation(indexingOperationId, broadcastDeletedOperation);
        }

        //Callback function

        function fillOperationTable(json) {

            //Loop on operations to add one line per Operation in the table
            var html = "";
            for(var i = 0; i < json.resources.length; i++) {

                html += "<tr>" +
                "    <th scope='row'>" + json.resources[i].id + "</th>" +
                "    <td>" + json.resources[i].entityType + "</td>" +
                "    <td>" + json.resources[i].entityId + "</td>" +
                "    <td>" + json.resources[i].operation + "</td>" +
                "    <td>" + json.resources[i].timestamp + "</td>" +
                "    <td>" +
                "        <button type='button'" +
                "                data-operationId=''" +
                "                class='btn-operation-delete btn btn-primary btn-mini'>" +
                "            Delete" +
                "        </button>" +
                "    </td>" +
                "</tr>";
            }

            //Update the table
            $('#indexingOperationTable tbody').html(html);

        }

        function broadcastDeletedOperation() {
            //Here I call all UI component that need to be refresh after sending a deleting operation
            updateOperationTable();
            myStatController.updateStatNbOperation();
        }

        return operationController;
    }
);
