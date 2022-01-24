package home.vs.app_java.dto.trucking_industry;

import java.util.List;

import home.vs.app_java.dto.CoordinateDTO;

public final class TruckRoute {
    private int truckId;
    private List<CoordinateDTO> coordinates;
    private double length;
    public TruckRoute(int truckId, List<CoordinateDTO> coordinates, double length) {
        this.truckId = truckId;
        this.coordinates = coordinates;
        this.length = length;
    }
    public int getTruckId() {
        return truckId;
    }
    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }
    public List<CoordinateDTO> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(List<CoordinateDTO> coordinates) {
        this.coordinates = coordinates;
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        long temp;
        temp = Double.doubleToLongBits(length);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + truckId;
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
        TruckRoute other = (TruckRoute) obj;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        if (Double.doubleToLongBits(length) != Double.doubleToLongBits(other.length))
            return false;
        if (truckId != other.truckId)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "TruckRoute [coordinates=" + coordinates + ", length=" + length + ", truckId=" + truckId + "]";
    }
}
