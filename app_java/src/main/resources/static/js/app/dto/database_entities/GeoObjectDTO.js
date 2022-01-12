class GeoObjectDTO extends DTO {
    constructor(id, layerId, name, type, forwardArrowDirection, coordinate, description, strJson) {
        super();
        this.id = id;
        this.layerId = layerId;
        this.name = name;
        this.type = type;
        this.forwardArrowDirection = forwardArrowDirection;
        this.coordinate = coordinate;
        this.description = description;
        this.strJson = strJson;
    }
    
    getAllStrJson() {
        return super.getAllStrJson(new Map([
            ["id", this.id],
            ["layerId", this.layerId],
            ["name", this.name],
            ["type", this.type],
            ["forwardArrowDirection", this.forwardArrowDirection],
            ["coordinate", this.coordinate.getAllStrJson],
            ["description", this.description],
            ["strJson", this.strJson]
        ]));
    }
}