package home.vs.app_java.dto.trucking_industry;

import home.vs.app_java.dto.CoordinateDTO;

public final class DeliveryPoint {
    private int id;
    private String name;
    private CoordinateDTO coordinate;
    public DeliveryPoint(int id, String name, CoordinateDTO coordinate) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public CoordinateDTO getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(CoordinateDTO coordinate) {
        this.coordinate = coordinate;
    }
    @Override
    public String toString() {
        return "DeliveryPoint [coordinate=" + coordinate + ", id=" + id + ", name=" + name + "]";
    }
}
