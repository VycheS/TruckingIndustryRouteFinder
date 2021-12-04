package home.vs.app_java.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import home.vs.app_java.dao.FakeDB;
import home.vs.app_java.dto.ClientDTO;
import home.vs.app_java.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public boolean create(ClientDTO client) {
        final int clientId = FakeDB.Client.ID_HOLDER.incrementAndGet();
        client.setId(clientId);
        FakeDB.Client.MAP.put(clientId, client);
        return true;
    }

    @Override
    public List<ClientDTO> readAll() {
        return new ArrayList<>(FakeDB.Client.MAP.values());
    }

    @Override
    public ClientDTO read(int id) {
        return FakeDB.Client.MAP.get(id);
    }

    @Override
    public boolean update(ClientDTO client, int id) {
        if (FakeDB.Client.MAP.containsKey(id)) {
            client.setId(id);
            FakeDB.Client.MAP.put(id, client);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return FakeDB.Client.MAP.remove(id) != null;
    }
    
}
