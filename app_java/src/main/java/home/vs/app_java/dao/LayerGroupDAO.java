package home.vs.app_java.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

import home.vs.app_java.dto.LayerGroupDTO;

@Component
public class LayerGroupDAO {
    private final JdbcTemplate jdbcTemplate;

    public LayerGroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LayerGroupDTO> getAll() {
        String sql = "SELECT * FROM layer_group";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LayerGroupDTO.class));
    }

    public LayerGroupDTO get(int id) {
        String sql = "SELECT * FROM client WHERE id=?";
        return jdbcTemplate.query(sql, new Object[]{id}, new int[]{Types.INTEGER}, new BeanPropertyRowMapper<>(LayerGroupDTO.class))
            .stream().findAny().orElse(null);
    }

    public void save(LayerGroupDTO layerGroup) {
        String sql = "INSERT INTO layer_group(id, name) VALUES(?, ?)";
        jdbcTemplate.update(sql, layerGroup.getId(), layerGroup.getName());
    }

    public void update(int id, LayerGroupDTO layerGroup) {
        String sql = "UPDATE layer_group SET name=? WHERE id=?";
        jdbcTemplate.update(sql, layerGroup.getName(), layerGroup.getId());
    }

    public void delete(int id) {
        String sql = "DELETE FROM layer_group WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public Integer getCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM layer_group", Integer.class);
    }
    
}
