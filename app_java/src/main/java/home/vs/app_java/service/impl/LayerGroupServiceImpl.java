package home.vs.app_java.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import home.vs.app_java.dao.FakeDB;
import home.vs.app_java.dto.LayerGroupDTO;
import home.vs.app_java.service.LayerGroupService;

import home.vs.app_java.dto.TwoFKeys;

@Service
public class LayerGroupServiceImpl implements LayerGroupService {

    @Override
    public boolean create(LayerGroupDTO group, int clientId) {
        if (FakeDB.Client.MAP.containsKey(clientId)) {
            final int groupId = FakeDB.LayerGroup.ID_HOLDER.incrementAndGet();
            group.setId(groupId);
            FakeDB.LayerGroup.MAP.put(groupId, group);
            FakeDB.UserLayerGroup.SET.add(new TwoFKeys(clientId, groupId));
            return true;
        }
        return false;
        
    }

    @Override
    public List<LayerGroupDTO> readAll(int clientId) {
        Iterator<TwoFKeys> iter = FakeDB.UserLayerGroup.SET.iterator();
        List<LayerGroupDTO> groups = new ArrayList<>();
        while (iter.hasNext()) {
            TwoFKeys twoFKeys = iter.next();
            if (twoFKeys.getClientId() == clientId) {
                int groupId = twoFKeys.getLayerGroupId();
                LayerGroupDTO group  = FakeDB.LayerGroup.MAP.get(groupId);
                groups.add(group);
            }
        }
        return groups;
    }

    @Override
    public LayerGroupDTO read(int id, int clientId) {
        List<LayerGroupDTO> groups = this.readAll(clientId);
        Iterator<LayerGroupDTO> iter = groups.iterator();
        while (iter.hasNext()) {
            LayerGroupDTO group = iter.next();
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    @Override
    public boolean update(LayerGroupDTO group, int id, int clientId) {
        if (this.read(id, clientId) != null) {
            group.setId(id);
            FakeDB.LayerGroup.MAP.put(id, group);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id, int clientId) {
        if (this.read(id, clientId) != null) {
            FakeDB.LayerGroup.MAP.remove(id);
            Iterator<TwoFKeys> iter = FakeDB.UserLayerGroup.SET.iterator();
            while (iter.hasNext()) {
                TwoFKeys twoFKeys = iter.next();
                if ((twoFKeys.getLayerGroupId() == id) && (twoFKeys.getClientId() == clientId)) {
                    FakeDB.UserLayerGroup.SET.remove(twoFKeys);
                    return true;
                }
            }
        }
        return false;
    }
    
}
