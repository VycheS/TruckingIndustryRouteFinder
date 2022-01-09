class LayerGroupCRUD extends CRUD {
    constructor(clientId) {
        super(`/client/${clientId}/layer_groups`);
    }

    create(layerGroup) {
        return super.create(layerGroup.getAllStrJson());
    }

    read(id) {
        return super.read(id);
    }

    readAll() {
        return super.readAll();
    }

    update(id, layerGroup) {
        return super.update(id, layerGroup.getAllStrJson());
    }

    delete() {
        return super.delete(id);
    }


}