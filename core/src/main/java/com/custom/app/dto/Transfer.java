package com.custom.app.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by olga on 18.09.15.
 */
public class Transfer {

    public enum TYPE {TRANSACTION, CREDIT, DEBIT}

    private int id;
    private Account accountFrom;
    private Account accountTo;
    private Client clientFrom;
    private Client clientTo;
    private Date date;
    private BigDecimal amount;
    private TYPE type;

    public Transfer() {

    }

    public Transfer(int id, Account accountFrom, Account accountTo, Date date, BigDecimal amount) {
        this.id = id;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.date = date;
        this.amount = amount;
    }

    public Transfer(int id, Date date, BigDecimal amount) {

        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }


    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public Client getClientFrom() {
        return clientFrom;
    }

    public void setClientFrom(Client clientFrom) {
        this.clientFrom = clientFrom;
    }

    public Client getClientTo() {
        return clientTo;
    }

    public void setClientTo(Client clientTo) {
        this.clientTo = clientTo;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", clientFrom=" + clientFrom +
                ", clientTo=" + clientTo +
                ", date=" + date +
                ", amount=" + amount +
                ", type=" + type +
                '}';
    }
}
