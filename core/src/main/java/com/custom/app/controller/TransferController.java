package com.custom.app.controller;

import com.custom.app.exception.BankDAOException;
import com.custom.app.dto.Transfer;
import com.custom.app.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by olga on 24.09.15.
 */
@RestController
@RequestMapping("/transaction")
public class TransferController {
    @Autowired
    private TransferService transferService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Transfer> findAll() {
        return transferService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void save(@RequestBody Transfer transfer) {
        switch (transfer.getType()) {
            case TRANSACTION:
                transferService.createTransfer(transfer);
                break;
            case CREDIT:
                transferService.creditAccount(transfer);
                break;
            case DEBIT:
                transferService.debitAccount(transfer);
                break;
            default:
                throw new BankDAOException(new UnsupportedOperationException());
        }
    }
}
