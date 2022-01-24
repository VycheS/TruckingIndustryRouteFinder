class RandomlyGenerateCRUD extends CRUD {
    constructor(clientId, layerGroupId) {
        super(`/client/${clientId}/layer_group/${layerGroupId}/trucking_industry/generate`);
    }
    sendCommandToServer() {
        return super.readAll();
    }
}