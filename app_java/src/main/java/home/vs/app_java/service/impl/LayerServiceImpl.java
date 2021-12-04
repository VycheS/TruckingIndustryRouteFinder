package home.vs.app_java.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import home.vs.app_java.dao.FakeDB;
import home.vs.app_java.dto.LayerDTO;
import home.vs.app_java.dto.TwoFKeys;
import home.vs.app_java.service.LayerService;

@Service
public class LayerServiceImpl implements LayerService {

    @Override
    public boolean create(LayerDTO layer, int layerGroupId, int clientId) {
        if (FakeDB.UserLayerGroup.SET.contains(new TwoFKeys(clientId, layerGroupId))) {
            final UUID uuid = UUID.randomUUID();
            layer.setId(uuid);
            layer.setLayerGroupId(layerGroupId);
            FakeDB.Layer.MAP.put(uuid, layer);
            return true;
        }
        return false;
        
    }

    @Override
    public List<LayerDTO> readAll(int layerGroupId, int clientId) {
        Map<UUID, LayerDTO> layersMap = this.selectWhere(layerGroupId, clientId);
        return new ArrayList<>(layersMap.values());
    }

    @Override
    public LayerDTO read(UUID id, int layerGroupId, int clientId) {
        Map<UUID, LayerDTO> layersMap = this.selectWhere(layerGroupId, clientId);
        if (!layersMap.isEmpty()) {
            return layersMap.get(id);
        }
        return null;
    }

    @Override
    public boolean update(LayerDTO layer, UUID id, int layerGroupId, int clientId) {
        Map<UUID, LayerDTO> layersMap = this.selectWhere(layerGroupId, clientId);
        if (!layersMap.isEmpty() && layersMap.containsKey(id)) {
            layer.setId(id);
            layer.setLayerGroupId(layerGroupId);
            FakeDB.Layer.MAP.put(id, layer);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(UUID id, int layerGroupId, int clientId) {
        Map<UUID, LayerDTO> layersMap = this.selectWhere(layerGroupId, clientId);
        if (!layersMap.isEmpty() && layersMap.containsKey(id)) {
            FakeDB.Layer.MAP.remove(id);
            return true;
        }
        return false;
    }

    private Map<UUID, LayerDTO> selectWhere(int layerGroupId, int clientId) {
        Map<UUID, LayerDTO> layers = new HashMap<>();
        if (FakeDB.UserLayerGroup.SET.contains(new TwoFKeys(clientId, layerGroupId))) {
            for(Map.Entry<UUID, LayerDTO> entry : FakeDB.Layer.MAP.entrySet()) {
                if (entry.getValue().getLayerGroupId() == layerGroupId) {
                    layers.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return layers;
    }
    
}
