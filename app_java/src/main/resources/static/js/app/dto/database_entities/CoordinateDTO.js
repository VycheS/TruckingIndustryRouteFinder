class CoordinateDTO extends DTO {
    constructor(latitude, longitude ) {
        super();
        this.latitude = latitude; //широта
        this.longitude = longitude; //долгота
    }

    getAllStrJson() {
        return super.getAllStrJson(new Map([
            ["latitude", this.latitude],
            ["longitude", this.longitude]
        ]));
    }
}