package com.custom.controller;

import com.custom.config.IoC;
import com.custom.exception.BankDAOException;
import com.custom.model.Transaction;
import com.custom.service.TransactionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by olga on 18.09.15.
 */
@WebServlet(name = "transaction", urlPatterns = {"/transaction"})
public class TransactionServlet extends HttpServlet {
    private TransactionService transactionService;

    public TransactionServlet() {
        super();
        transactionService = new IoC().getTransactionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Transaction> transactions = transactionService.findAll();
        Gson gson = new GsonBuilder().create();
        String transactionsJson = gson.toJson(transactions);
        PrintWriter out = resp.getWriter();
        out.print(transactionsJson);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Transaction transaction = gson.fromJson(req.getReader(), Transaction.class);
        try {
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
            resp.setStatus(HttpServletResponse.SC_CREATED);
            return;
        } catch (BankDAOException e) {
            PrintWriter out = resp.getWriter();
            out.print(e.getMessage());
            out.close();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }
}
