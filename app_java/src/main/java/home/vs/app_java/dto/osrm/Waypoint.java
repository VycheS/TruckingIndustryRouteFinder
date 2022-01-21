package home.vs.app_java.dto.osrm;

import java.util.List;

public class Waypoint {
    private String name;
    private List<Double> location;
    // public Waypoint(String name, List<Double> location) {
    //     this.name = name;
    //     this.location = location;
    // }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Double> getLocation() {
        return location;
    }
    public void setLocation(List<Double> location) {
        this.location = location;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
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
        Waypoint other = (Waypoint) obj;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
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
        return "Waypoint [location=" + location + ", name=" + name + "]";
    } 
}
