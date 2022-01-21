package home.vs.app_java.dto.osrm;

import java.util.List;

public final class Intersection {
    private Integer out;
    private List<Boolean> entry;
    private List<Integer> bearings;
    private List<Double> location;
    private Integer ind;
    // public Intersection(Integer out, List<Boolean> entry, List<Integer> bearings, List<Double> location, Integer ind) {
    //     this.out = out;
    //     this.entry = entry;
    //     this.bearings = bearings;
    //     this.location = location;
    //     this.ind = ind;
    // }
    public Integer getOut() {
        return out;
    }
    public void setOut(Integer out) {
        this.out = out;
    }
    public List<Boolean> getEntry() {
        return entry;
    }
    public void setEntry(List<Boolean> entry) {
        this.entry = entry;
    }
    public List<Integer> getBearings() {
        return bearings;
    }
    public void setBearings(List<Integer> bearings) {
        this.bearings = bearings;
    }
    public List<Double> getLocation() {
        return location;
    }
    public void setLocation(List<Double> location) {
        this.location = location;
    }
    public Integer getInd() {
        return ind;
    }
    public void setInd(Integer ind) {
        this.ind = ind;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bearings == null) ? 0 : bearings.hashCode());
        result = prime * result + ((entry == null) ? 0 : entry.hashCode());
        result = prime * result + ((ind == null) ? 0 : ind.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((out == null) ? 0 : out.hashCode());
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
        Intersection other = (Intersection) obj;
        if (bearings == null) {
            if (other.bearings != null)
                return false;
        } else if (!bearings.equals(other.bearings))
            return false;
        if (entry == null) {
            if (other.entry != null)
                return false;
        } else if (!entry.equals(other.entry))
            return false;
        if (ind == null) {
            if (other.ind != null)
                return false;
        } else if (!ind.equals(other.ind))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (out == null) {
            if (other.out != null)
                return false;
        } else if (!out.equals(other.out))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Intersection [bearings=" + bearings + ", entry=" + entry + ", ind=" + ind + ", location=" + location
                + ", out=" + out + "]";
    }
}
