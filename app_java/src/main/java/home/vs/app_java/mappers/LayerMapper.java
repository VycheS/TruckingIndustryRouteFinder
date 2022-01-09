package home.vs.app_java.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import home.vs.app_java.dto.LayerDTO;

public class LayerMapper implements RowMapper<LayerDTO> {
    @Override
    public LayerDTO mapRow(ResultSet rs, int i) throws SQLException {
        LayerDTO layer = new LayerDTO();

        String strUUID = rs.getString("id");
        UUID uuid = UUID.fromString(strUUID);

        layer.setId(uuid);
        layer.setLayerGroupId(rs.getInt("layer_group_id"));
        layer.setTypeObj(rs.getString("type"));
        layer.setName(rs.getString("name"));
        layer.setForwardArrowDirection(rs.getBoolean("forward_arrow_direction"));
        layer.setDescription(rs.getString("description"));
        layer.setStrJson(rs.getString("json_data"));

        return layer;
    }
}
