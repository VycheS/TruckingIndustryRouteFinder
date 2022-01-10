package home.vs.app_java.dto;

import java.io.Serializable;
import java.util.UUID;

public class LayerDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
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

    
}
