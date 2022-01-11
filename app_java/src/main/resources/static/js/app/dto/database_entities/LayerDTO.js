class LayerDTO extends DTO {
    constructor(id, layerGroupId, type, name, description, strJson) {
        this.id = id;
        this.layerGroupId = layerGroupId;
        this.type = type;
        this.name = name;
        this.description = description;
        this.strJson = strJson;
    }

    getAllStrJson() {
        return super.getAllStrJson(new Map([
            ["id", this.id],
            ["layerGroupId", this.layerGroupId],
            ["type", this.type],
            ["name", this.name],
            ["description", this.description],
            ["strJson", this.strJson]
        ]));
    }
}