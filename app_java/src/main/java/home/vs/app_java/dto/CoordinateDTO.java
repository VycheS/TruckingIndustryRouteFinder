package home.vs.app_java.dto;

import java.io.Serializable;

public class CoordinateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private double latitude; // широта
    private double longitude; // долгота

    public CoordinateDTO(double latitude, double longitude) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}