package com.custom.dao;

import com.custom.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by olga on 17.09.15.
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
