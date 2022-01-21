package home.vs.app_java.dto.osrm;

import java.util.List;

public class Route {
    private Geometry geometry;
    private List<Leg> legs;
    private String weight_name;
    private String weight;
    private String duration;
    private String distance;
    // public Route(Geometry geometry, List<Leg> legs, String weight_name, String weight, String duration,
    //         String distance) {
    //     this.geometry = geometry;
    //     this.legs = legs;
    //     this.weight_name = weight_name;
    //     this.weight = weight;
    //     this.duration = duration;
    //     this.distance = distance;
    // }
    public Geometry getGeometry() {
        return geometry;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    public List<Leg> getLegs() {
        return legs;
    }
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }
    public String getWeight_name() {
        return weight_name;
    }
    public void setWeight_name(String weight_name) {
        this.weight_name = weight_name;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((distance == null) ? 0 : distance.hashCode());
        result = prime * result + ((duration == null) ? 0 : duration.hashCode());
        result = prime * result + ((geometry == null) ? 0 : geometry.hashCode());
        result = prime * result + ((legs == null) ? 0 : legs.hashCode());
        result = prime * result + ((weight == null) ? 0 : weight.hashCode());
        result = prime * result + ((weight_name == null) ? 0 : weight_name.hashCode());
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
        Route other = (Route) obj;
        if (distance == null) {
            if (other.distance != null)
                return false;
        } else if (!distance.equals(other.distance))
            return false;
        if (duration == null) {
            if (other.duration != null)
                return false;
        } else if (!duration.equals(other.duration))
            return false;
        if (geometry == null) {
            if (other.geometry != null)
                return false;
        } else if (!geometry.equals(other.geometry))
            return false;
        if (legs == null) {
            if (other.legs != null)
                return false;
        } else if (!legs.equals(other.legs))
            return false;
        if (weight == null) {
            if (other.weight != null)
                return false;
        } else if (!weight.equals(other.weight))
            return false;
        if (weight_name == null) {
            if (other.weight_name != null)
                return false;
        } else if (!weight_name.equals(other.weight_name))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Route [distance=" + distance + ", duration=" + duration + ", geometry=" + geometry + ", legs=" + legs
                + ", weight=" + weight + ", weight_name=" + weight_name + "]";
    }
}
