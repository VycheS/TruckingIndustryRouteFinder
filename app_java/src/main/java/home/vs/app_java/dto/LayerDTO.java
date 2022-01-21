package home.vs.app_java.dto;

import java.util.UUID;

public class LayerDTO {
    private UUID id;
    private int layerGroupId;
    private String typeObj;
    private String name;
    private String description;
    private String strJson;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public int getLayerGroupId() {
        return layerGroupId;
    }
    public void setLayerGroupId(int layerGroupId) {
        this.layerGroupId = layerGroupId;
    }
    public String getTypeObj() {
        return typeObj;
    }
    public void setTypeObj(String typeObj) {
        this.typeObj = typeObj;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + layerGroupId;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((strJson == null) ? 0 : strJson.hashCode());
        result = prime * result + ((typeObj == null) ? 0 : typeObj.hashCode());
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
        LayerDTO other = (LayerDTO) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (layerGroupId != other.layerGroupId)
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
        if (typeObj == null) {
            if (other.typeObj != null)
                return false;
        } else if (!typeObj.equals(other.typeObj))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "LayerDTO [description=" + description + ", id=" + id + ", layerGroupId=" + layerGroupId + ", name="
                + name + ", strJson=" + strJson + ", typeObj=" + typeObj + "]";
    }

    
}
