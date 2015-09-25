package com.custom.controller;

import com.custom.exception.BankDAOException;
import com.custom.model.Transaction;
import com.custom.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by olga on 24.09.15.
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void save(@RequestBody Transaction transaction) {
        switch (transaction.getType()) {
            case TRANSACTION:
                transactionService.createTransaction(transaction);
                break;
            case CREDIT:
                transactionService.creditAccount(transaction);
                break;
            case DEBIT:
                transactionService.debitAccount(transaction);
                break;
            default:
                throw new BankDAOException(new UnsupportedOperationException());
        }
    }
}
