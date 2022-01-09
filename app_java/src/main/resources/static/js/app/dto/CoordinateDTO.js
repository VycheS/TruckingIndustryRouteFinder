class CoordinateDTO extends DTO {
    constructor(latitude, longitude ) {
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