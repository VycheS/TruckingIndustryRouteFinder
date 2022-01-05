package home.vs.app_java.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GeoObjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private UUID layerId;
    private String name;
    private String type;
    private List<CoordinateDTO> coordinates = new ArrayList<>();
    private String description;
    private String strJson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getLayerId() {
        return layerId;
    }

    public void setLayerId(UUID layerId) {
        this.layerId = layerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CoordinateDTO> getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(List<CoordinateDTO> coordinates) {
        this.coordinates = coordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStrJson() {
        return this.strJson;
    }

    public void setStrJson(String json) {
        this.strJson = json;
    }
}
