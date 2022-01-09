class GeoObjectCRUD extends CRUD {
    constructor(clientId, layerGroupId, layerId) {
        super(`/client/${clientId}/layer_group/${layerGroupId}/layer/${layerId}/geo_objects`);
    }

    create(geoObject) {
        return super.create(geoObject.getAllStrJson());
    }

    read(id) {
        return super.read(id);
    }

    readAll() {
        return super.readAll();
    }

    update(id, geoObject) {
        return super.update(id, geoObject.getAllStrJson());
    }

    delete(id) {
        return super.delete(id);
    }


}