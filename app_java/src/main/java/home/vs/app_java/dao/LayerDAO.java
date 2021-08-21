package home.vs.app_java.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

import home.vs.app_java.dto.LayerDTO;
import home.vs.app_java.mappers.LayerMapper;


@Component
public class LayerDAO {

    private final JdbcTemplate jdbcTemplate;

    public LayerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LayerDTO> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM layer", new LayerMapper());
    }

    public LayerDTO get(UUID id) {
        return this.jdbcTemplate.query(
            "SELECT * FROM layer WHERE id=?",
            new Object[]{id},
            new int[]{Types.JAVA_OBJECT},
            new LayerMapper()
        ).stream().findAny().orElse(null);
    }

    public void save(LayerDTO layer) {
        String sql = "INSERT INTO layer(id, layer_group_id, type_obj, name, description, addjson) "
            + "VALUES(?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(
            sql,
            layer.getId(),
            layer.getLayerGroupId(),
            layer.getTypeObj(),
            layer.getName(),
            layer.getDescription(),
            layer.getJson()
        );
    }

    public void update(UUID id, LayerDTO updatedLayer) {
        String sql = "UPDATE layer SET layer_group_id=?, type_obj=?, name=?, description=?, addjson=? WHERE id=?";
        this.jdbcTemplate.update(
            sql,
            updatedLayer.getLayerGroupId(),
            updatedLayer.getTypeObj(),
            updatedLayer.getName(),
            updatedLayer.getDescription(),
            updatedLayer.getJson(),
            id
        );
    }

    public void delete(UUID id) {
        this.jdbcTemplate.update("DELETE FROM layer WHERE id=?", id);
    }
    
}
