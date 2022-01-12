//import LayerGeoObj;

class LayerManager {
    constructor(map, layersStorage = {}) {
        this._map = map;
        //хранит все слои
        this._layersStorage = layersStorage;
        this._mapOfLayers = new Map();
        this._mapLayerGeoObjectManager = new MapLayerGeoObjectManager(map);
    }

    add(name, type) {
        // if (name in this._layersStorage) {
        if (this._mapOfLayers.has(name)) {
            return false;
        } else {
            if ((typeof (name) == 'string') && (type == 'point' || type == 'line' || type == 'broken_line')) {
                // this._layersStorage[name] = new LayerGeoObj(this._map, name);
                this._mapOfLayers.set(name, new LayerDTO(null, type, name, null, null, new Array));
                return true;
            } else {
                console.log(new Error(`Нет возможности создать слой с такими параметрами (${name}, ${type})`));
            }
        }
    }

    on(name) {
        // if (name in this._layersStorage) {
        if (this._mapOfLayers.has(name)) {
            this._mapLayerGeoObjectManager.setLayer(this._mapOfLayers.get(name));
            this._mapLayerGeoObjectManager.on();
            // this._layersStorage[name].on();
        } else {
            console.log(new Error(`Нет такого слоя или не задан вообще ни один`));
        }
    }

    off(name) {
        // if (name in this._layersStorage) {
        if (this._mapOfLayers.has(name)) {
            this._mapLayerGeoObjectManager.setLayer(this._mapOfLayers.get(name));
            this._mapLayerGeoObjectManager.off();
            // this._layersStorage[name].off();
        } else {
            console.log(new Error(`Нет такого слоя или не задан вообще ни один`));
        }
    }

    getLayer(name){
        // if (name in this._layersStorage) {
        if (this._mapOfLayers.has(name)) {
            this._mapLayerGeoObjectManager.setLayer(this._mapOfLayers.get(name));
            return this._mapLayerGeoObjectManager;
            // return this._layersStorage[name];
        } else {
            console.error(`Возвращаемый слой:${name} отсутсвует`);
        }
    }
}