package home.vs.app_java.dto.osrm;

import java.util.List;

public final class Geometry {
    private List<List<Double>> coordinates;
    private String type;
    // public Geometry(List<List<Double>> coordinates, String type) {
    //     this.coordinates = coordinates;
    //     this.type = type;
    // }
    public List<List<Double>> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Geometry other = (Geometry) obj;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Geometry [coordinates=" + coordinates + ", type=" + type + "]";
    }    
}
