package com.custom.app.dao;

import com.custom.app.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by olga on 17.09.15.
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
