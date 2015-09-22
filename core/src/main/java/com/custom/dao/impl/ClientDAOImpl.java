package com.custom.dao.impl;

import com.custom.dao.ClientDAO;
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
    final static String SQL_FIND_ALL = "SELECT id, name, surname FROM client";
    final static String SQL_FIND_BY_ID = "SELECT name, surname FROM client WHERE id = ?";
    final static String SQL_INSERT = "INSERT INTO client(name, surname) VALUES (?, ?)";

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Client> findAll() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_FIND_ALL)) {
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(new Client(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            return clients;
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }

    @Override
    public Client findById(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prStmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            prStmt.setInt(1, id);
            try (ResultSet rs = prStmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(rs.getString(1), rs.getString(2));
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
             PreparedStatement prStmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
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
