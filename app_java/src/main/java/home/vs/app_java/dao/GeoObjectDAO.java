package home.vs.app_java.dao;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import home.vs.app_java.dto.GeoObjectDTO;


@Component
public class GeoObjectDAO {

    private final JdbcTemplate jdbcTemplate;

    public GeoObjectDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GeoObjectDTO> getAll() {
        return jdbcTemplate.query("SELECT * FROM geo_object", new BeanPropertyRowMapper<>(GeoObjectDTO.class));
    }

    public GeoObjectDTO get(int id) {
        return jdbcTemplate.query("SELECT * FROM geo_object WHERE id=?", new Object[]{id}, new int[]{Types.INTEGER}, new BeanPropertyRowMapper<>(GeoObjectDTO.class))
            .stream().findAny().orElse(null);
    }

    public void save(GeoObjectDTO geoObject) { //TODO здесь нужно исправить что с координатами, чтобы передовать весь массив, возможно придётся в начале в цикле вытащить их потом при помощи временного файла положить в запрос
        jdbcTemplate.update("INSERT INTO geo_object(id, layer_id, name, type, coordinate, description, addjson) VALUES(?, ?, ?, ?, ROW(?, ?), ?, ?)",
            geoObject.getId(), geoObject.getLayerId(), geoObject.getName(), geoObject.getType(), geoObject.getCoordinate()[0].getLatitude(), geoObject.getCoordinate()[0].getLongitude(), geoObject.getDescription(), geoObject.getJson());
    }

    public void update(int id, GeoObjectDTO updatedGeoObject) { //TODO здесь нужно исправить что с координатами, чтобы передовать весь массив, возможно придётся в начале в цикле вытащить их потом при помощи временного файла положить в запрос
        jdbcTemplate.update("UPDATE geo_object SET name=?, coordinate=ROW(?, ?), description=?, addjson=? WHERE id=?",
            updatedGeoObject.getName(), updatedGeoObject.getCoordinate()[0].getLatitude(), updatedGeoObject.getCoordinate()[0].getLongitude(), updatedGeoObject.getDescription(), updatedGeoObject.getJson(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM geo_object WHERE id=?", id);
    }

}
