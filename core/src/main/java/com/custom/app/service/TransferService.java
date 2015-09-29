package com.custom.app.service;

import com.custom.app.dto.Transfer;

import java.util.List;

/**
 * Created by olga on 18.09.15.
 */
public interface TransferService {
    List<Transfer> findAll();

    void createTransfer(Transfer transfer);

    void creditAccount(Transfer transfer);

    void debitAccount(Transfer transfer);
}
