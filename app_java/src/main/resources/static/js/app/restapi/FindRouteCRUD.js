class FindRouteCRUD extends CRUD {
    constructor(clientId, layerGroupId) {
        super(`/client/${clientId}/layer_group/${layerGroupId}/trucking_industry/find`);
    }
    sendCommandToServer() {
        return super.readAll();
    }
}