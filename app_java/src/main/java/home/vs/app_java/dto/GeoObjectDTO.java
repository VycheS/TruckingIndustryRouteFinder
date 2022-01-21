package home.vs.app_java.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GeoObjectDTO {

    private Integer id;
    private UUID layerId;
    private String name;
    private String type;
    private Boolean forwardArrowDirection;
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

    public Boolean getForwardArrowDirection() {
        return forwardArrowDirection;
    }
    
    public void setForwardArrowDirection(Boolean forwardArrowDirection) {
        this.forwardArrowDirection = forwardArrowDirection;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((forwardArrowDirection == null) ? 0 : forwardArrowDirection.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((layerId == null) ? 0 : layerId.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((strJson == null) ? 0 : strJson.hashCode());
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
        GeoObjectDTO other = (GeoObjectDTO) obj;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (forwardArrowDirection == null) {
            if (other.forwardArrowDirection != null)
                return false;
        } else if (!forwardArrowDirection.equals(other.forwardArrowDirection))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (layerId == null) {
            if (other.layerId != null)
                return false;
        } else if (!layerId.equals(other.layerId))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (strJson == null) {
            if (other.strJson != null)
                return false;
        } else if (!strJson.equals(other.strJson))
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
        return "GeoObjectDTO [coordinates=" + coordinates + ", description=" + description + ", forwardArrowDirection="
                + forwardArrowDirection + ", id=" + id + ", layerId=" + layerId + ", name=" + name + ", strJson="
                + strJson + ", type=" + type + "]";
    }
}
