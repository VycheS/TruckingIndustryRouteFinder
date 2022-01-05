package home.vs.app_java.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.vs.app_java.dao.LayerGroupDAO;
import home.vs.app_java.dto.LayerGroupDTO;
import home.vs.app_java.service.LayerGroupService;

@Service
public class LayerGroupServiceImpl implements LayerGroupService {

    @Autowired
    private LayerGroupDAO layerGroupDAO;
    

    @Override
    public boolean create(LayerGroupDTO layerGroup, int clientId) {
        return layerGroupDAO.save(layerGroup, clientId);
        
    }

    @Override
    public List<LayerGroupDTO> readAll(int clientId) {
        return layerGroupDAO.getAll(clientId);
    }

    @Override
    public LayerGroupDTO read(int id, int clientId) {
        return layerGroupDAO.get(id, clientId);
    }

    @Override
    public boolean update(LayerGroupDTO layerGroup, int id, int clientId) {
        return layerGroupDAO.update(layerGroup, id, clientId);
    }

    @Override
    public boolean delete(int id, int clientId) {
        return layerGroupDAO.delete(id, clientId);
    }
    
}
