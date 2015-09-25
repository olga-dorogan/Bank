package com.custom.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by olga on 18.09.15.
 */
public class Transaction {

    public enum TYPE {TRANSACTION, CREDIT, DEBIT}

    private int id;
    private Account accountFrom;
    private Account accountTo;
    private Client clientFrom;
    private Client clientTo;
    private Date date;
    private BigDecimal amount;
    private TYPE type;

    public Transaction() {

    }

    public Transaction(int id, Account accountFrom, Account accountTo, Date date, BigDecimal amount) {
        this.id = id;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.date = date;
        this.amount = amount;
    }

    public Transaction(int id, Date date, BigDecimal amount) {

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (id != that.id) return false;
        if (accountFrom != null ? !accountFrom.equals(that.accountFrom) : that.accountFrom != null) return false;
        if (accountTo != null ? !accountTo.equals(that.accountTo) : that.accountTo != null) return false;
        if (clientFrom != null ? !clientFrom.equals(that.clientFrom) : that.clientFrom != null) return false;
        if (clientTo != null ? !clientTo.equals(that.clientTo) : that.clientTo != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (accountFrom != null ? accountFrom.hashCode() : 0);
        result = 31 * result + (accountTo != null ? accountTo.hashCode() : 0);
        result = 31 * result + (clientFrom != null ? clientFrom.hashCode() : 0);
        result = 31 * result + (clientTo != null ? clientTo.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
