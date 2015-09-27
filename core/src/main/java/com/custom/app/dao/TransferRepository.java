package com.custom.app.dao;

import com.custom.app.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by olga on 18.09.15.
 */
public interface TransferRepository extends JpaRepository<Transfer, Integer> {
}
