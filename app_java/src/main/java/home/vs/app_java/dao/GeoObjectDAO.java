package home.vs.app_java.dao;

import java.sql.Types;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import home.vs.app_java.dto.GeoObjectDTO;
import home.vs.app_java.mappers.GeoObjectMapper;
import home.vs.app_java.dto.CoordinateDTO;
import home.vs.app_java.dto.ExtendedCoordinateDTO;


@Component
public class GeoObjectDAO {

    private final JdbcTemplate jdbcTemplate;
    
    private static final String SQL_SELECTED_LAYER = "WITH selected_layer AS (\n"
                                                        +"WITH selected_lg AS (\n"
                                                            +"SELECT layer_group.*\n"
                                                            +"FROM user_layer_group\n"
                                                            +"JOIN layer_group ON\n"
                                                                +"user_layer_group.layer_group_id = layer_group.id\n"
                                                                +"AND layer_group.id = ?\n"
                                                            +"JOIN client ON\n"
                                                                +"user_layer_group.client_id = client.id\n"
                                                                +"AND client.id = ?\n"
                                                        +")\n"
                                                        +"SELECT layer.*\n"
                                                        +"FROM layer\n"
                                                        +"JOIN selected_lg ON\n"
                                                            +"layer.layer_group_id = selected_lg.id\n"
                                                            +"AND layer.id = ?\n"
                                                    +")\n";
    private static final String SQL_FROM_GEO_OBJ_AND_SELECTED_LAYER = "FROM geo_object\n"
                                                                    + "JOIN selected_layer ON\n"
                                                                        + "geo_object.layer_id = selected_layer.id\n";

    public GeoObjectDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GeoObjectDTO> getAll(UUID layerId, int layerGroupId, int clientId) {
        final String sqlSelectAllWithoutCoordinates = "SELECT geo_object.id, geo_object.name, geo_object.layer_id, geo_object.type, geo_object.forward_arrow_direction, geo_object.description, geo_object.json_data\n";
        final String sqlSelectIdAndCoordinates = "SELECT geo_object.id, geo_object.coordinate\n";
        final String sqlGetGeoObject = SQL_SELECTED_LAYER
                                    +  sqlSelectAllWithoutCoordinates
                                    +  SQL_FROM_GEO_OBJ_AND_SELECTED_LAYER;

        final Object[] arrObjects = new Object[]{layerGroupId, clientId, layerId};
        final int[] arrTypes = new int[]{Types.INTEGER, Types.INTEGER, Types.OTHER};
        
        List<GeoObjectDTO> geoObjects = this.jdbcTemplate.query(sqlGetGeoObject
                , arrObjects
                , arrTypes
                , new GeoObjectMapper());
            
        if (!geoObjects.isEmpty()) {
            final String sqlGetCoordinates = "WITH selected_geo_object AS (\n"
                                                +SQL_SELECTED_LAYER
                                                +sqlSelectIdAndCoordinates
                                                +SQL_FROM_GEO_OBJ_AND_SELECTED_LAYER
                                            +")\n"
                                            +"SELECT id, (unnest(coordinate)).*\n"
                                            +"FROM selected_geo_object";
            List<ExtendedCoordinateDTO> extendedCoordinates = this.jdbcTemplate.query(sqlGetCoordinates
                    , arrObjects
                    , arrTypes
                    , new BeanPropertyRowMapper<>(ExtendedCoordinateDTO.class)
                );

            Map<Integer, List<CoordinateDTO>> mapListsCoordinates = new HashMap<>();
            for (ExtendedCoordinateDTO extendedCoordinate : extendedCoordinates) {
                if(!mapListsCoordinates.containsKey(extendedCoordinate.getId())) {
                    mapListsCoordinates.put(extendedCoordinate.getId(), new LinkedList<>());
                }
                CoordinateDTO coordinate = new CoordinateDTO(extendedCoordinate.getLatitude()
                                                        , extendedCoordinate.getLongitude());
                mapListsCoordinates.get(extendedCoordinate.getId()).add(coordinate);
                
            }

            for (GeoObjectDTO geoObject : geoObjects) {
                List<CoordinateDTO> coordinates = mapListsCoordinates.get(geoObject.getId());
                geoObject.setCoordinates(coordinates);
            }    
        }
        return geoObjects;
    }

    public GeoObjectDTO get(int id, UUID layerId, int layerGroupId, int clientId) {
        final String sqlSelectAllWithoutCoordinates = "SELECT geo_object.id, geo_object.name, geo_object.layer_id, geo_object.type, geo_object.forward_arrow_direction, geo_object.description, geo_object.json_data\n";
        final String sqlSelectCoordinates = "SELECT geo_object.coordinate\n";
        final String sqlGetGeoObject = SQL_SELECTED_LAYER
                                    +  sqlSelectAllWithoutCoordinates
                                    +  SQL_FROM_GEO_OBJ_AND_SELECTED_LAYER
                                        +  "AND geo_object.id = ?\n";

        final Object[] arrObjects = new Object[]{layerGroupId, clientId, layerId, id};
        final int[] arrTypes = new int[]{Types.INTEGER, Types.INTEGER, Types.OTHER, Types.INTEGER};
        
        GeoObjectDTO geoObject = this.jdbcTemplate.query(sqlGetGeoObject
                , arrObjects
                , arrTypes
                , new GeoObjectMapper()
            ).stream().findAny().orElse(null);
            
        if (geoObject != null) {
            final String sqlGetCoordinates = "WITH selected_geo_object AS (\n"
                                                +SQL_SELECTED_LAYER
                                                +sqlSelectCoordinates
                                                +SQL_FROM_GEO_OBJ_AND_SELECTED_LAYER
                                                    +"AND geo_object.id = ?\n"
                                            +")\n"
                                            +"SELECT (unnest(coordinate)).*\n"
                                            +"FROM selected_geo_object";
            List<CoordinateDTO> coordinates = this.jdbcTemplate.query(sqlGetCoordinates
                    , arrObjects
                    , arrTypes
                    , new BeanPropertyRowMapper<>(CoordinateDTO.class)
                );
            geoObject.setCoordinates(coordinates);      
        }
        return geoObject;
    }
    
    public boolean save(GeoObjectDTO geoObject, UUID layerId, int layerGroupId, int clientId) {
        if (this.entityIdExists(layerId, layerGroupId, clientId)){
            geoObject.setLayerId(layerId);
            List<String> coordinateTemplate = new ArrayList<>(geoObject.getCoordinates().size());

            List<Object> listObjects = new ArrayList<>(7 + (geoObject.getCoordinates().size() * 2));
            listObjects.addAll(Arrays.asList(geoObject.getLayerId(), geoObject.getName(), geoObject.getType(), geoObject.getForwardArrowDirection(), geoObject.getDescription(), geoObject.getStrJson()));

            for (CoordinateDTO coordinate : geoObject.getCoordinates()) {
                listObjects.add(coordinate.getLatitude());
                listObjects.add(coordinate.getLongitude());

                coordinateTemplate.add("ROW(?, ?)::coord");
            }

            final String inParamsCoordinate = "ARRAY[ " + String.join(", ", coordinateTemplate) + " ]";
            final String sql = "INSERT INTO geo_object(layer_id, name, type, forward_arrow_direction, description, json_data, coordinate)\n"
                            +  "VALUES(?, ?, ?::geo_obj_type, ?, ?, ?, " + inParamsCoordinate + ")";

            return this.jdbcTemplate.update(sql, listObjects.toArray()) > 0;
        }
        return false;
    }

    public boolean update(GeoObjectDTO updatedGeoObject, int id, UUID layerId, int layerGroupId, int clientId) {
        if (this.get(id, layerId, layerGroupId, clientId) != null) {
            updatedGeoObject.setId(id);
            updatedGeoObject.setLayerId(layerId);
            List<String> coordinateTemplate = new ArrayList<>(updatedGeoObject.getCoordinates().size());

            List<Object> listObjects = new ArrayList<>(6 + (updatedGeoObject.getCoordinates().size() * 2));
            listObjects.addAll(Arrays.asList(updatedGeoObject.getName(), updatedGeoObject.getType(), updatedGeoObject.getForwardArrowDirection(), updatedGeoObject.getDescription(), updatedGeoObject.getStrJson()));

            for (CoordinateDTO coordinate : updatedGeoObject.getCoordinates()) {
                listObjects.add(coordinate.getLatitude());
                listObjects.add(coordinate.getLongitude());

                coordinateTemplate.add("ROW(?, ?)::coord");
            }
            listObjects.add(id);

            final String inParamsCoordinate = "ARRAY[ " + String.join(", ", coordinateTemplate) + " ]";

            String sql = "UPDATE geo_object\n"
                        +"SET name=?, type=?::geo_obj_type, forward_arrow_direction=?, description=?, json_data=?, coordinate=" + inParamsCoordinate + "\n"
                        +"WHERE id=?\n";

            return this.jdbcTemplate.update(sql, listObjects.toArray()) > 0;
        }
        return false;
    }
    
    public boolean delete(int id, UUID layerId, int layerGroupId, int clientId) {
        if (this.get(id, layerId, layerGroupId, clientId) != null) {
            return jdbcTemplate.update("DELETE FROM geo_object WHERE id=?", id) > 0;
        }
        return false;
    }
    
    private boolean entityIdExists(UUID layerId, int layerGroupId, int clientId) {
        final String SQL_EXIST_DEPENDENT_TABLES = SQL_SELECTED_LAYER 
                                                + "SELECT EXISTS("
                                                        + "SELECT * FROM selected_layer"
                                                + ")";
        return this.jdbcTemplate.queryForObject(SQL_EXIST_DEPENDENT_TABLES, Boolean.class, layerGroupId, clientId, layerId);
    }

}
