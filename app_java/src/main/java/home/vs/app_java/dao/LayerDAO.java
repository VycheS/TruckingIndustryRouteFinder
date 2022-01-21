package home.vs.app_java.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

import home.vs.app_java.dto.LayerDTO;
import home.vs.app_java.mappers.LayerMapper;


@Component
public final class LayerDAO {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_SELECT_DEPENDENT_TABLES = "SELECT layer_group.*\n"
                                                            + "FROM user_layer_group\n"
                                                            + "JOIN layer_group ON\n"
                                                                + "user_layer_group.layer_group_id = layer_group.id\n"
                                                                + "AND layer_group.id = ?\n"
                                                            + "JOIN client ON\n"
                                                                + "user_layer_group.client_id = client.id\n"
                                                                + "AND client.id = ?";

    @Autowired
    public LayerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LayerDTO> getAll(int layerGroupId, int clientId) {
        final String sql = "WITH selected_lg AS (\n"
                            +  SQL_SELECT_DEPENDENT_TABLES
                        +  ")\n"
                        +  "SELECT layer.*\n"
                        +  "FROM layer\n"
                        +  "JOIN selected_lg ON\n"
                            +  "layer.layer_group_id = selected_lg.id";
        return this.jdbcTemplate.query(sql, new Object[]{layerGroupId, clientId}, new int[]{Types.INTEGER, Types.INTEGER}, new LayerMapper());
    }

    public LayerDTO get(UUID id, int layerGroupId, int clientId) {
        final String sql = "WITH selected_lg AS (\n"
                        +SQL_SELECT_DEPENDENT_TABLES
                    +")\n"
                    +"SELECT layer.*\n"
                    +"FROM layer\n"
                    +"JOIN selected_lg ON\n"
                        +"layer.layer_group_id = selected_lg.id\n"
                        +"AND layer.id = ?";
        return this.jdbcTemplate
                .query(
                    sql,
                    new Object[]{layerGroupId, clientId, id},
                    new int[]{Types.INTEGER, Types.INTEGER, Types.OTHER},
                    new LayerMapper()
                )
                .stream()
                .findAny()
                .orElse(null);
    }

    public UUID save(LayerDTO layer, int layerGroupId, int clientId) {
        if (this.entityIdExists(layerGroupId, clientId)) {
            final String sql = "INSERT INTO layer(layer_group_id, type, name, description, json_data)\n"
                            +  "VALUES(?, ?::geo_obj_type, ?, ?, ?::jsonb) RETURNING id";
            layer.setLayerGroupId(layerGroupId);
            return this.jdbcTemplate.queryForObject(sql
                , UUID.class
                , layer.getLayerGroupId()
                , layer.getTypeObj()
                , layer.getName()
                , layer.getDescription()
                , layer.getStrJson()
            );
            // return this.jdbcTemplate.update(
            //         sql,
            //         new Object[]{layer.getLayerGroupId(), layer.getTypeObj(), layer.getName(), layer.getDescription(), layer.getStrJson()},
            //         new int[]{Types.INTEGER, Types.OTHER, Types.VARCHAR, Types.VARCHAR, Types.OTHER}
            //     ) > 0;
                
        }
        return null;
    }

    public boolean update(LayerDTO updatedLayer, UUID id, int layerGroupId, int clientId) {
        if (this.entityIdExists(layerGroupId, clientId)) {
            final String sql = "UPDATE layer\n"
                            +  "SET layer_group_id = ?, type = ?, name = ?, description = ?, json_data = ?::jsonb\n"
                            +  "WHERE id=?";
            updatedLayer.setId(id);
            updatedLayer.setLayerGroupId(layerGroupId);
            return this.jdbcTemplate.update(sql,
                new Object[]{updatedLayer.getLayerGroupId(), updatedLayer.getTypeObj(), updatedLayer.getName(), updatedLayer.getDescription(), updatedLayer.getStrJson(), updatedLayer.getId()},
                new int[]{Types.INTEGER, Types.OTHER, Types.VARCHAR, Types.VARCHAR, Types.OTHER, Types.OTHER}
            ) > 0;
        }
        return false;
    }

    public boolean delete(UUID id, int layerGroupId, int clientId) {
        if (this.entityIdExists(layerGroupId, clientId)) {
            final String sql = "DELETE FROM layer\n"
                            +  "WHERE id = ?";
            return this.jdbcTemplate.update(sql, id) > 0;
        }
        return false;
    }

    private boolean entityIdExists(int layerGroupId, int clientId) {
        final String SQL_EXIST_DEPENDENT_TABLES =  "SELECT EXISTS("
                                                        + SQL_SELECT_DEPENDENT_TABLES
                                                + ")";
        return this.jdbcTemplate.queryForObject(SQL_EXIST_DEPENDENT_TABLES, Boolean.class, layerGroupId, clientId);
    }
    
}
