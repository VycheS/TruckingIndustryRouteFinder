class LayerGroupCRUD extends CRUD {
    constructor(clientId) {
        super(`/client/${clientId}/layer_groups`);
    }

    create(layerGroup) {
        return super.create(JSON.stringify(layerGroup));
    }

    read(id) {
        return super.read(id);
    }

    readAll() {
        return super.readAll();
    }

    update(id, layerGroup) {
        return super.update(id, JSON.stringify(layerGroup));
    }

    delete() {
        return super.delete(id);
    }


}