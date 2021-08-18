package home.vs.app_java.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import home.vs.app_java.dto.GeoObjectDTO;
import home.vs.app_java.mappers.GeoObjectMapper;
import home.vs.app_java.dto.CoordinateDTO;


@Component
public class GeoObjectDAO {

    private final JdbcTemplate jdbcTemplate;

    public GeoObjectDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GeoObjectDTO> getAll() {
        return jdbcTemplate.query("SELECT * FROM geo_object", new GeoObjectMapper());
    }

    public GeoObjectDTO get(int id) {
        return jdbcTemplate.query("SELECT * FROM geo_object WHERE id=?", new Object[]{id}, new int[]{Types.INTEGER}, new GeoObjectMapper())
            .stream().findAny().orElse(null);
    }

    public void save(GeoObjectDTO geoObject) {
        List<String> coordinateTemplate = new ArrayList<>(geoObject.getCoordinate().size());
        List<Double> listDoubleCoordinates = new ArrayList<>(geoObject.getCoordinate().size() * 2);

        for (CoordinateDTO coordinate : geoObject.getCoordinate()) {
            listDoubleCoordinates.add(coordinate.getLatitude());
            listDoubleCoordinates.add(coordinate.getLongitude());

            coordinateTemplate.add("ROW(?, ?)");
        }
        String inParamsCoordinate = "{" + String.join(",", coordinateTemplate) + "}";

        String sql = "INSERT INTO geo_object(id, layer_id, name, type, description, addjson, coordinate) "
            + "VALUES(?, ?, ?, ?, ?, ?, " + inParamsCoordinate + ")";

        jdbcTemplate.update(
            sql,
            geoObject.getId(),
            geoObject.getLayerId(),
            geoObject.getName(),
            geoObject.getDescription(),
            geoObject.getJson(),
            listDoubleCoordinates.toArray());
    }

    public void update(int id, GeoObjectDTO updatedGeoObject) {
        List<String> coordinateTemplate = new ArrayList<>(updatedGeoObject.getCoordinate().size());
        List<Double> listDoubleCoordinates = new ArrayList<>();

        for (CoordinateDTO coordinate : updatedGeoObject.getCoordinate()) {
            listDoubleCoordinates.add(coordinate.getLatitude());
            listDoubleCoordinates.add(coordinate.getLongitude());

            coordinateTemplate.add("ROW(?, ?)");
        }
        String inParamsCoordinate = "{" + String.join(",", coordinateTemplate) + "}";

        String sql = 
            "UPDATE geo_object "
            + "SET name=?, description=?, addjson=?, coordinate=" + inParamsCoordinate + " "
            + "WHERE id=?";
        
        jdbcTemplate.update(
            sql,
            updatedGeoObject.getName(),
            updatedGeoObject.getDescription(),
            updatedGeoObject.getJson(),
            listDoubleCoordinates.toArray(),
            id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM geo_object WHERE id=?", id);
    }

}
