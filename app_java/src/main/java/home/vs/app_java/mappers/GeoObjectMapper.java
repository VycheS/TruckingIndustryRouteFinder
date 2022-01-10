package home.vs.app_java.mappers;

import org.springframework.jdbc.core.RowMapper;

import home.vs.app_java.dto.GeoObjectDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class GeoObjectMapper implements RowMapper<GeoObjectDTO> {
    @Override
    public GeoObjectDTO mapRow(ResultSet rs, int i) throws SQLException {
        GeoObjectDTO geoObject = new GeoObjectDTO();
        
        String strUUID = rs.getString("layer_id");
        UUID uuid = UUID.fromString(strUUID);

        geoObject.setId(rs.getInt("id"));
        geoObject.setLayerId(uuid);
        geoObject.setName(rs.getString("name"));
        geoObject.setType(rs.getString("type"));
        if (rs.getObject("forward_arrow_direction") == null) {
            geoObject.setForwardArrowDirection(null);
        } else {
            geoObject.setForwardArrowDirection(rs.getBoolean("forward_arrow_direction"));
        }
        geoObject.setDescription(rs.getString("description"));
        geoObject.setStrJson(rs.getString("json_data"));

        return geoObject;
    }
}