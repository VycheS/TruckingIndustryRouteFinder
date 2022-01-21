package home.vs.app_java.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.vs.app_java.dao.LayerDAO;
import home.vs.app_java.dto.LayerDTO;
import home.vs.app_java.service.LayerService;

@Service
public class LayerServiceImpl implements LayerService {
    @Autowired
    private LayerDAO layerDAO;

    @Override
    public UUID create(LayerDTO layer, int layerGroupId, int clientId) {
        return this.layerDAO.save(layer, layerGroupId, clientId);
        
    }

    @Override
    public List<LayerDTO> readAll(int layerGroupId, int clientId) {
        return this.layerDAO.getAll(layerGroupId, clientId);
    }

    @Override
    public LayerDTO read(UUID id, int layerGroupId, int clientId) {
        return this.layerDAO.get(id, layerGroupId, clientId);
    }

    @Override
    public boolean update(LayerDTO layer, UUID id, int layerGroupId, int clientId) {
        return this.layerDAO.update(layer, id, layerGroupId, clientId);
    }

    @Override
    public boolean delete(UUID id, int layerGroupId, int clientId) {
        return this.layerDAO.delete(id, layerGroupId, clientId);
    }
    
}
