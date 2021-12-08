package home.vs.app_java.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import home.vs.app_java.dto.ClientDTO;

public class ClientMapper implements RowMapper<ClientDTO> {
    @Override
    public ClientDTO mapRow(ResultSet rs, int i) throws SQLException {
        ClientDTO client = new ClientDTO();

        client.setId(rs.getInt("id"));
        client.setName(rs.getString("name"));
        client.setSurname(rs.getString("surname"));
        client.setPatronymic(rs.getString("patronymic"));
        client.setPassword(rs.getString("password"));
        client.setEmail(rs.getString("email"));
        client.setNumberphone(rs.getString("numberphone"));
        client.setRole(rs.getString("role"));
        client.setStrJson(rs.getString("json_data"));

        return client;
    }
}
