package home.vs.app_java.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeoObjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer layerId;
    private String name;
    private String type;
    private List<CoordinateDTO> coordinate = new ArrayList<>();
    private String description;
    private String json;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLayerId() {
        return layerId;
    }

    public void setLayerId(Integer layerId) {
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

    public List<CoordinateDTO> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(List<CoordinateDTO> coordinate) {
        this.coordinate = coordinate;
    }

    public void setCoordinate(CoordinateDTO coordinate) {
        this.coordinate = Arrays.asList(coordinate);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
