package com.custom.dao;

import com.custom.entity.Client;

import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
public interface ClientDAO {
    List<Client> findAll();

    Client findById(int id);

    void create(Client client);
}
