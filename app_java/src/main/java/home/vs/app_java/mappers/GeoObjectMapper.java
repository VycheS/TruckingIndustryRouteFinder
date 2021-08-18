package home.vs.app_java.mappers;

import org.springframework.jdbc.core.RowMapper;

import home.vs.app_java.dto.GeoObjectDTO;
import home.vs.app_java.dto.CoordinateDTO;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;


public class GeoObjectMapper implements RowMapper<GeoObjectDTO> {
    @Override
    public GeoObjectDTO mapRow(ResultSet rs, int i) throws SQLException {
        GeoObjectDTO geoObject = new GeoObjectDTO();
        geoObject.setId(rs.getInt("id"));
        geoObject.setLayerId(rs.getInt("layer_id"));
        geoObject.setName(rs.getString("name"));
        geoObject.setType(rs.getString("type"));

        Array arrSqlCoord = rs.getArray("coordinate");
        Struct[] arrStructSqlCoord = (Struct[])arrSqlCoord.getArray();

        List<CoordinateDTO> coordinates = new ArrayList<>(arrStructSqlCoord.length);

        for(int index = 0; index < arrStructSqlCoord.length; index++) { 
            Object[] objCoord = arrStructSqlCoord[index].getAttributes();
            coordinates.add(new CoordinateDTO((Double)objCoord[0], (Double)objCoord[1]));
        }

        geoObject.setCoordinate(coordinates);
        geoObject.setDescription(rs.getString("description"));
        geoObject.setJson(rs.getString("addjson"));

        return geoObject;
    }
}