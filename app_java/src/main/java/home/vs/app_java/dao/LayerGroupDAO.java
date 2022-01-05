package home.vs.app_java.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

import home.vs.app_java.dto.LayerGroupDTO;

@Component
public class LayerGroupDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LayerGroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LayerGroupDTO> getAll(int clientId) {
        final String sql = "SELECT layer_group.*\n"
                    +"FROM user_layer_group\n"
                    +"JOIN layer_group ON\n"
                        +"layer_group.id = user_layer_group.layer_group_id\n"
                    +"JOIN client ON\n"
                        +"client.id = user_layer_group.client_id\n"
                        +"AND client.id = ?";
        return jdbcTemplate.query(sql, new Object[]{clientId}, new int[]{Types.INTEGER}, new BeanPropertyRowMapper<>(LayerGroupDTO.class));
    }

    public LayerGroupDTO get(int id, int clientId) {
        final String sql = "SELECT layer_group.*\n"
                    +"FROM user_layer_group\n"
                    +"JOIN layer_group ON\n"
                        +"layer_group.id = user_layer_group.layer_group_id\n"
                        +"AND layer_group.id = ?\n"
                    +"JOIN client ON\n"
                        +"client.id = user_layer_group.client_id\n"
                        +"AND client.id = ?";
        return jdbcTemplate.query(sql, new Object[]{id, clientId}, new int[]{Types.INTEGER, Types.INTEGER}, new BeanPropertyRowMapper<>(LayerGroupDTO.class))
                .stream().findAny().orElse(null);
    }

    public boolean save(LayerGroupDTO layerGroup, int clientId) {
        final String sql = "SELECT EXISTS(\n"
                            +"SELECT *\n"
                            +"FROM client\n"
                            +"WHERE id = ?\n"
                    +")";
        boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, clientId);
        if (exists) {
            final String sql1 = "INSERT INTO layer_group(name)\n"
                        + "VALUES(?) RETURNING id\n";
            final String sql2 = "INSERT INTO user_layer_group(client_id, layer_group_id)\n"
                        + "VALUES(?, ?)";
            final int newLayerGroupId = jdbcTemplate.queryForObject(sql1, Integer.class, layerGroup.getName());
            jdbcTemplate.update(sql2, clientId, newLayerGroupId);
            return true;
        }
        return false;
    }

    public boolean update(LayerGroupDTO layerGroup, int id, int clientId) {
        final String sql = "UPDATE layer_group SET name=?\n"
                    +"FROM client, user_layer_group\n"
                    +"WHERE client.id = user_layer_group.client_id\n"
                        +"AND layer_group.id = user_layer_group.layer_group_id\n"
                        +"AND client.id = user_layer_group.client_id\n"
                        +"AND layer_group.id = ?\n"
                        +"AND client.id = ?";
        return jdbcTemplate.update(sql, layerGroup.getName(), id, clientId) > 0;
    }

    public boolean delete(int id, int clientId) {
        String sql = "DELETE FROM user_layer_group\n"
                    +"USING layer_group, client\n"
                    +"WHERE client.id = user_layer_group.client_id\n"
                        +"AND layer_group.id = user_layer_group.layer_group_id\n"
                        +"AND client.id = user_layer_group.client_id\n"
                        +"AND layer_group.id = ?\n"
                        +"AND client.id = ?";
        if (jdbcTemplate.update(sql, id, clientId) > 0) {
            sql = "DELETE FROM layer_group\n"
                + "WHERE layer_group.id = ?";
            return jdbcTemplate.update(sql, id) > 0;
        }
        return false;
    }
    
}
