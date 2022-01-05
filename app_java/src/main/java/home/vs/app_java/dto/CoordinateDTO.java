package home.vs.app_java.dto;

import java.io.Serializable;

public class CoordinateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double latitude; // широта
    private Double longitude; // долгота

    public CoordinateDTO(Double latitude, Double longitude) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public CoordinateDTO() {}

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}