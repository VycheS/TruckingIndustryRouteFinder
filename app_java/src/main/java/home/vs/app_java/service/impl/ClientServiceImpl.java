package home.vs.app_java.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import home.vs.app_java.dao.ClientDAO;
import home.vs.app_java.dto.ClientDTO;
import home.vs.app_java.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDAO clientDAO;

    @Override
    public boolean create(ClientDTO client) {
        this.clientDAO.save(client);
        return true;
    }

    @Override
    public List<ClientDTO> readAll() {
        return this.clientDAO.getAll();
    }

    @Override
    public ClientDTO read(int id) {
        return this.clientDAO.get(id);
    }

    @Override
    public boolean update(ClientDTO client, int id) {
        if (this.clientDAO.get(id) != null) {
            this.clientDAO.update(id, client);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if (this.clientDAO.get(id) != null) {
            this.clientDAO.delete(id);
            return true;
        }
        return false;
    }
    
}
