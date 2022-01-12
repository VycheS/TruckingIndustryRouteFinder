class GeoObjectCRUD extends CRUD {
    constructor(clientId, layerGroupId, layerId) {
        super(`/client/${clientId}/layer_group/${layerGroupId}/layer/${layerId}/geo_objects`);
    }

    create(geoObject) {
        return super.create(JSON.stringify(geoObject));
    }

    read(id) {
        return super.read(id);
    }

    readAll() {
        return super.readAll();
    }

    update(id, geoObject) {
        return super.update(id, JSON.stringify(geoObject));
    }

    delete(id) {
        return super.delete(id);
    }


}