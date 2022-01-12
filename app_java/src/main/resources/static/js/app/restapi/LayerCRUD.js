class LayerCRUD extends CRUD {
    constructor(clientId, layerGroupId) {
        super(`/client/${clientId}/layer_group/${layerGroupId}/layers`);
    }

    create(layer) {
        return super.create(JSON.stringify(layer));
    }

    read(id) {
        return super.read(id);
    }

    readAll() {
        return super.readAll();
    }

    update(id, layer) {
        return super.update(id, JSON.stringify(layer));
    }

    delete(id) {
        return super.delete(id);
    }


}