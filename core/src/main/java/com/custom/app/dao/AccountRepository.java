package com.custom.app.dao;

import com.custom.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by olga on 17.09.15.
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {
}
