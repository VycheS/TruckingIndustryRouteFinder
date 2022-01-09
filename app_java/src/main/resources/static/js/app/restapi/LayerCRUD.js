class LayerCRUD extends CRUD {
    constructor(clientId, layerGroupId) {
        super(`/client/${clientId}/layer_group/${layerGroupId}/layers`);
    }

    create(layer) {
        return super.create(layer.getAllStrJson());
    }

    read(id) {
        return super.read(id);
    }

    readAll() {
        return super.readAll();
    }

    update(id, layer) {
        return super.update(id, layer.getAllStrJson());
    }

    delete(id) {
        return super.delete(id);
    }


}