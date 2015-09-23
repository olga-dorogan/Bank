package com.custom.dao;

import com.custom.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by olga on 18.09.15.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
