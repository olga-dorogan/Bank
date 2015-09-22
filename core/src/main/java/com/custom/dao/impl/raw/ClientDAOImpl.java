package com.custom.dao.impl.raw;

import com.custom.dao.ClientDAO;
import com.custom.dao.util.Query;
import com.custom.entity.Client;
import com.custom.exception.BankDAOException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
public class ClientDAOImpl implements ClientDAO {
    @Autowired
    private DataSource dataSource;

    @Override
    public List<Client> findAll() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(Query.Client.FIND_ALL)) {
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt(Query.Client.FIND_ALL_ID),
                        rs.getString(Query.Client.FIND_ALL_NAME),
                        rs.getString(Query.Client.FIND_ALL_SURNAME)));
            }
            return clients;
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }

    @Override
    public Client findById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prStmt = conn.prepareStatement(Query.Client.FIND_BY_ID)) {
            prStmt.setInt(1, id);
            try (ResultSet rs = prStmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getString(Query.Client.FIND_BY_ID_NAME),
                            rs.getString(Query.Client.FIND_BY_ID_SURNAME));
                }
                throw new BankDAOException(String.format("Client with id == %d doesn't exist", id));
            }
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }

    @Override
    public void create(Client client) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prStmt = conn.prepareStatement(Query.Client.INSERT, Statement.RETURN_GENERATED_KEYS)) {
            prStmt.setString(1, client.getName());
            prStmt.setString(2, client.getSurname());
            if (prStmt.executeUpdate() != 1) {
                throw new BankDAOException("Error with creation " + client);
            }
            try (ResultSet rsGeneratedKeys = prStmt.getGeneratedKeys()) {
                if (rsGeneratedKeys.next()) {
                    client.setId(rsGeneratedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new BankDAOException(e.getMessage(), e);
        }
    }


}
