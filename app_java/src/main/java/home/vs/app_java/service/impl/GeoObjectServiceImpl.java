package home.vs.app_java.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import home.vs.app_java.dao.FakeDB;
import home.vs.app_java.dto.GeoObjectDTO;
import home.vs.app_java.dto.LayerDTO;
import home.vs.app_java.service.GeoObjectService;

@Service
public class GeoObjectServiceImpl implements GeoObjectService {

    @Override
    public boolean create(GeoObjectDTO geoObject, UUID layerId, int layerGroupId, int clientId) {
        if (this.layerExist(layerId, layerGroupId, clientId)) {
            final int id = FakeDB.GeoObject.ID_HOLDER.incrementAndGet();
            geoObject.setId(id);
            geoObject.setLayerId(layerId);
            FakeDB.GeoObject.MAP.put(id, geoObject);
            return true;
        }
        return false;
        
    }

    @Override
    public List<GeoObjectDTO> readAll(UUID layerId, int layerGroupId, int clientId) {
        List<GeoObjectDTO> geoObjects = new ArrayList<>();
        if (layerExist(layerId, layerGroupId, clientId)) {
            for (Map.Entry<Integer,GeoObjectDTO> entry: FakeDB.GeoObject.MAP.entrySet()) {
                GeoObjectDTO geoObject = entry.getValue();
                if (geoObject.getLayerId().equals(layerId)) {
                    geoObjects.add(geoObject);
                }
            }
        }
        return geoObjects;
    }

    @Override
    public GeoObjectDTO read(int id, UUID layerId, int layerGroupId, int clientId) {
        if (layerExist(layerId, layerGroupId, clientId) && FakeDB.GeoObject.MAP.containsKey(id)) {
            GeoObjectDTO geoObject = FakeDB.GeoObject.MAP.get(id);
            if (geoObject.getLayerId().equals(layerId)) {
                return geoObject;
            }
        }
        return null;
    }

    @Override
    public boolean update(GeoObjectDTO geoObject, int id, UUID layerId, int layerGroupId, int clientId) {
        if (layerExist(layerId, layerGroupId, clientId) && FakeDB.GeoObject.MAP.containsKey(id)) {
            GeoObjectDTO oldGeoObject = FakeDB.GeoObject.MAP.get(id);
            if (oldGeoObject.getLayerId().equals(layerId)) {
                geoObject.setId(id);
                geoObject.setLayerId(layerId);
                FakeDB.GeoObject.MAP.put(id, geoObject);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int id, UUID layerId, int layerGroupId, int clientId) {
        if (layerExist(layerId, layerGroupId, clientId) && FakeDB.GeoObject.MAP.containsKey(id)) {
            FakeDB.GeoObject.MAP.remove(id);
            return true;
        }
        return false;
    }

    private boolean layerExist(UUID layerId, int layerGroupId, int clientId) {
        final LayerServiceImpl layerServ = new LayerServiceImpl();
        LayerDTO layer = layerServ.read(layerId, layerGroupId, clientId);
        return layer != null;
    }
    
}
