package home.vs.app_java.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.vs.app_java.dao.GeoObjectDAO;
import home.vs.app_java.dto.GeoObjectDTO;
import home.vs.app_java.service.GeoObjectService;

@Service
public class GeoObjectServiceImpl implements GeoObjectService {
    @Autowired
    private GeoObjectDAO geoObjectDAO;

    @Override
    public boolean create(GeoObjectDTO geoObject, UUID layerId, int layerGroupId, int clientId) {
        return this.geoObjectDAO.save(geoObject, layerId, layerGroupId, clientId);
        
    }

    @Override
    public List<GeoObjectDTO> readAll(UUID layerId, int layerGroupId, int clientId) {
        return this.geoObjectDAO.getAll(layerId, layerGroupId, clientId);
    }

    @Override
    public GeoObjectDTO read(int id, UUID layerId, int layerGroupId, int clientId) {
        return this.geoObjectDAO.get(id, layerId, layerGroupId, clientId);
    }

    @Override
    public boolean update(GeoObjectDTO geoObject, int id, UUID layerId, int layerGroupId, int clientId) {
        return this.geoObjectDAO.update(geoObject, id, layerId, layerGroupId, clientId);
    }

    @Override
    public boolean delete(int id, UUID layerId, int layerGroupId, int clientId) {
        return this.geoObjectDAO.delete(id, layerId, layerGroupId, clientId);
    }
    
}
