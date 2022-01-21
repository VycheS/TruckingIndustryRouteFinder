package home.vs.app_java.dto.osrm;

import java.util.List;

public final class Step {
    private List<Intersection> intersection;
    private String driving_side;
    private Geometry geometry;
    private String mode;
    private Maneuver maneuver;
    private Double weight;
    private Double duration;
    private String name;
    private Double distance;
    // public Step(List<Intersection> intersection, String driving_side, Geometry geometry, String mode, Maneuver maneuver,
    //         Double weight, Double duration, String name, Double distance) {
    //     this.intersection = intersection;
    //     this.driving_side = driving_side;
    //     this.geometry = geometry;
    //     this.mode = mode;
    //     this.maneuver = maneuver;
    //     this.weight = weight;
    //     this.duration = duration;
    //     this.name = name;
    //     this.distance = distance;
    // }
    public List<Intersection> getIntersection() {
        return intersection;
    }
    public void setIntersection(List<Intersection> intersection) {
        this.intersection = intersection;
    }
    public String getDriving_side() {
        return driving_side;
    }
    public void setDriving_side(String driving_side) {
        this.driving_side = driving_side;
    }
    public Geometry getGeometry() {
        return geometry;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public Maneuver getManeuver() {
        return maneuver;
    }
    public void setManeuver(Maneuver maneuver) {
        this.maneuver = maneuver;
    }
    public Double getWeight() {
        return weight;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public Double getDuration() {
        return duration;
    }
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getDistance() {
        return distance;
    }
    public void setDistance(Double distance) {
        this.distance = distance;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((distance == null) ? 0 : distance.hashCode());
        result = prime * result + ((driving_side == null) ? 0 : driving_side.hashCode());
        result = prime * result + ((duration == null) ? 0 : duration.hashCode());
        result = prime * result + ((geometry == null) ? 0 : geometry.hashCode());
        result = prime * result + ((intersection == null) ? 0 : intersection.hashCode());
        result = prime * result + ((maneuver == null) ? 0 : maneuver.hashCode());
        result = prime * result + ((mode == null) ? 0 : mode.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
        Step other = (Step) obj;
        if (distance == null) {
            if (other.distance != null)
                return false;
        } else if (!distance.equals(other.distance))
            return false;
        if (driving_side == null) {
            if (other.driving_side != null)
                return false;
        } else if (!driving_side.equals(other.driving_side))
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
        if (intersection == null) {
            if (other.intersection != null)
                return false;
        } else if (!intersection.equals(other.intersection))
            return false;
        if (maneuver == null) {
            if (other.maneuver != null)
                return false;
        } else if (!maneuver.equals(other.maneuver))
            return false;
        if (mode == null) {
            if (other.mode != null)
                return false;
        } else if (!mode.equals(other.mode))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (weight == null) {
            if (other.weight != null)
                return false;
        } else if (!weight.equals(other.weight))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Step [distance=" + distance + ", driving_side=" + driving_side + ", duration=" + duration
                + ", geometry=" + geometry + ", intersection=" + intersection + ", maneuver=" + maneuver + ", mode="
                + mode + ", name=" + name + ", weight=" + weight + "]";
    } 
}
