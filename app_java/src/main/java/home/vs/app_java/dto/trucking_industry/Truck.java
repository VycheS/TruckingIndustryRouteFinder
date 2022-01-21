package home.vs.app_java.dto.trucking_industry;

import home.vs.app_java.dto.CoordinateDTO;

public final class Truck {
    private int id;
    private String name;
    private int carrying;
    private CoordinateDTO coordinate;
    public Truck(int id, String name, int carrying, CoordinateDTO coordinate) {
        this.id = id;
        this.name = name;
        this.carrying = carrying;
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
    public int getCarrying() {
        return carrying;
    }
    public void setCarrying(int carrying) {
        this.carrying = carrying;
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
        result = prime * result + carrying;
        result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Truck other = (Truck) obj;
        if (carrying != other.carrying)
            return false;
        if (coordinate == null) {
            if (other.coordinate != null)
                return false;
        } else if (!coordinate.equals(other.coordinate))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Truck [carrying=" + carrying + ", coordinate=" + coordinate + ", id=" + id + ", name=" + name + "]";
    }
}
