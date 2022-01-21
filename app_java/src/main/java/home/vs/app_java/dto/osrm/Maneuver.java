package home.vs.app_java.dto.osrm;

import java.util.List;

public class Maneuver {
    private Integer bearing_after;
    private Integer bearing_before;
    private List<Double> location;
    private String type;
    // public Maneuver(Integer bearing_after, Integer bearing_before, List<Double> location, String type) {
    //     this.bearing_after = bearing_after;
    //     this.bearing_before = bearing_before;
    //     this.location = location;
    //     this.type = type;
    // }
    public Integer getBearing_after() {
        return bearing_after;
    }
    public void setBearing_after(Integer bearing_after) {
        this.bearing_after = bearing_after;
    }
    public Integer getBearing_before() {
        return bearing_before;
    }
    public void setBearing_before(Integer bearing_before) {
        this.bearing_before = bearing_before;
    }
    public List<Double> getLocation() {
        return location;
    }
    public void setLocation(List<Double> location) {
        this.location = location;
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
        result = prime * result + ((bearing_after == null) ? 0 : bearing_after.hashCode());
        result = prime * result + ((bearing_before == null) ? 0 : bearing_before.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
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
        Maneuver other = (Maneuver) obj;
        if (bearing_after == null) {
            if (other.bearing_after != null)
                return false;
        } else if (!bearing_after.equals(other.bearing_after))
            return false;
        if (bearing_before == null) {
            if (other.bearing_before != null)
                return false;
        } else if (!bearing_before.equals(other.bearing_before))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
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
        return "Maneuver [bearing_after=" + bearing_after + ", bearing_before=" + bearing_before + ", location="
                + location + ", type=" + type + "]";
    }
}
