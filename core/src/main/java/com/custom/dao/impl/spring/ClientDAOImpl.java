package com.custom.dao.impl.spring;

import com.custom.dao.ClientDAO;
import com.custom.dao.util.Query;
import com.custom.entity.Client;
import com.custom.exception.BankDAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by olga on 22.09.15.
 */
public class ClientDAOImpl implements ClientDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public List<Client> findAll() {
        return jdbcTemplate.query(Query.Client.FIND_ALL, new ClientMapper());
    }

    @Override
    public Client findById(int id) {
        return jdbcTemplate.queryForObject(
                Query.Client.FIND_BY_ID,
                new Object[]{id},
                (rs, i) -> {
                    Client client = new Client(
                            rs.getString(Query.Client.FIND_BY_ID_NAME),
                            rs.getString(Query.Client.FIND_BY_ID_SURNAME));
                    return client;
                });
    }

    @Override
    public void create(Client client) {
        int updatedCnt = jdbcTemplate.update(Query.Client.INSERT, client.getName(), client.getSurname());
        if (updatedCnt != 1) {
            throw new BankDAOException(client.toString() + "is not saved");
        }
    }

    private static class ClientMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet resultSet, int i) throws SQLException {
            Client client = new Client();
            client.setId(resultSet.getInt(Query.Client.FIND_ALL_ID));
            if (resultSet.wasNull()) {
                client.setId(null);
            }
            client.setName(resultSet.getString(Query.Client.FIND_ALL_NAME));
            client.setSurname(resultSet.getString(Query.Client.FIND_ALL_SURNAME));
            return client;
        }
    }
}
