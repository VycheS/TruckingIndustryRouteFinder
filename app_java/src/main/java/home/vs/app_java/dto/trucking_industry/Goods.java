package home.vs.app_java.dto.trucking_industry;

import home.vs.app_java.dto.CoordinateDTO;

public final class Goods {
    private int id;
    private String name;
    private int weight;
    private Integer deliveryPointId;
    private CoordinateDTO coordinate;
    public Goods(int id, String name, int weight, Integer deliveryPointId, CoordinateDTO coordinate) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.deliveryPointId = deliveryPointId;
        this.coordinate = coordinate;
    }
    public Goods(int id, String name, int weight, CoordinateDTO coordinate) {
        this(id, name, weight, null, coordinate);
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
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public int getDeliveryPointId() {
        return deliveryPointId;
    }
    public void setDeliveryPointId(int deliveryPointId) {
        this.deliveryPointId = deliveryPointId;
    }
    public CoordinateDTO getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(CoordinateDTO coordinate) {
        this.coordinate = coordinate;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
        result = prime * result + deliveryPointId;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + weight;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Goods other = (Goods) obj;
        if (coordinate == null) {
            if (other.coordinate != null)
                return false;
        } else if (!coordinate.equals(other.coordinate))
            return false;
        if (deliveryPointId != other.deliveryPointId)
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (weight != other.weight)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Goods [coordinate=" + coordinate + ", deliveryPointId=" + deliveryPointId + ", id=" + id + ", name="
                + name + ", weight=" + weight + "]";
    }
}
