//import LayerGeoObj;

class LayerManager {
    constructor(map, mapOfLayers = new Map) {
        this._map = map;
        //хранит все слои
        this._mapOfLayers = mapOfLayers;
        this._mapLayerGeoObjectManager = new MapLayerGeoObjectManager(map);
    }

    add(name, type) {
        if (this._mapOfLayers.has(name)) {
            return false;
        } else {
            if ((typeof (name) == 'string') && (type == 'point' || type == 'line' || type == 'broken_line')) {
                this._mapOfLayers.set(name, new LayerDTO(null, type, name, null, null, new Array));
                return true;
            } else {
                console.log(new Error(`Нет возможности создать слой с такими параметрами (${name}, ${type})`));
            }
        }
    }

    on(name) {
        if (this._mapOfLayers.has(name)) {
            this._mapLayerGeoObjectManager.setLayer(this._mapOfLayers.get(name));
            this._mapLayerGeoObjectManager.on();
        } else {
            console.log(new Error(`Нет такого слоя или не задан вообще ни один`));
        }
    }

    off(name) {
        if (this._mapOfLayers.has(name)) {
            this._mapLayerGeoObjectManager.setLayer(this._mapOfLayers.get(name));
            this._mapLayerGeoObjectManager.off();
        } else {
            console.log(new Error(`Нет такого слоя или не задан вообще ни один`));
        }
    }

    getLayer(name){
        if (this._mapOfLayers.has(name)) {
            this._mapLayerGeoObjectManager.setLayer(this._mapOfLayers.get(name));
            return this._mapLayerGeoObjectManager;
        } else {
            console.error(`Возвращаемый слой:${name} отсутсвует`);
        }
    }
}