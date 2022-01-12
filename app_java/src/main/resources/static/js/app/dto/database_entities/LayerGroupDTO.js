class LayerGroupDTO extends DTO {
    constructor(id, name) {
        super();
        this.id = id;
        this.name = name;
    }

    getAllStrJson() {
        return super.getAllStrJson(new Map([
            ["id", this.id],
            ["name", this.name]
        ]));
    }
}