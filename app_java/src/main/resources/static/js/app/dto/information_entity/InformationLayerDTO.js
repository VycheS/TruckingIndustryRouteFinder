class InformationLayerDTO extends LayerDTO {
    constructor(id, type, name, description, strJson) {
        super(id, null, type, name, description, strJson);
        this.geoObjects = new Map();
    }
}