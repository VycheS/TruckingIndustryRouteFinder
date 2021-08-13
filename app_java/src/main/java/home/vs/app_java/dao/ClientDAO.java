package home.vs.app_java.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import home.vs.app_java.dto.ClientDTO;

import java.sql.Types;
import java.util.List;

@Component
public class ClientDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClientDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ClientDTO> getAll() {
        return jdbcTemplate.query("SELECT * FROM client", new BeanPropertyRowMapper<>(ClientDTO.class));
    }

    public ClientDTO get(int id) {
        return jdbcTemplate.query("SELECT * FROM client WHERE id=?", new Object[]{id}, new int[]{Types.INTEGER}, new BeanPropertyRowMapper<>(ClientDTO.class))
                .stream().findAny().orElse(null);
    }

    public void save(ClientDTO client) {
        jdbcTemplate.update("INSERT INTO client(surname, name, patronymic, password, email, numberphone, role, addjson) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
            client.getSurname(), client.getName(), client.getPatronymic(), client.getPassword(), client.getEmail(), client.getNumberphone(), client.getRole(), client.getJson());
    }

    public void update(int id, ClientDTO updatedClient) {
        jdbcTemplate.update("UPDATE client SET surname=?, name=?, patronymic=?, password=?, email=?, numberphone=?, role=?, addjson=? WHERE id=?",
            updatedClient.getSurname(), updatedClient.getName(), updatedClient.getPatronymic(), updatedClient.getPassword(), updatedClient.getEmail(), updatedClient.getNumberphone(), updatedClient.getRole(), updatedClient.getJson(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM client WHERE id=?", id);
    }

    public Integer getCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM client", Integer.class);
    }
}