package home.vs.app_java.dto;

public class TwoFKeys {
    public TwoFKeys(int clientId, int layerGroupId){
        this.clientId = clientId;
        this.layerGroupId = layerGroupId;
    }

    private int clientId;
    private int layerGroupId;

    public int getClientId() {
        return clientId;
    }
    public int getLayerGroupId() {
        return layerGroupId;
    }

    public void setLayerGroupId(int layerGroupId) {
        this.layerGroupId = layerGroupId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result * this.getClientId();
        result = prime * result * this.getLayerGroupId();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        TwoFKeys other = (TwoFKeys)obj;
        return (this.getClientId() == other.getClientId()) &&
            (this.getLayerGroupId() == other.getLayerGroupId());
    }
}