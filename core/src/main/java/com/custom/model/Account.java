package com.custom.model;

import java.math.BigDecimal;

/**
 * Created by olga on 17.09.15.
 */
public class Account {
    private int id;
    private String title;
    private BigDecimal amount;
    private Client client;

    public Account() {

    }

    public Account(int id) {
        this.id = id;
    }

    public Account(int id, String title, BigDecimal amount) {
        this.id = id;
        this.title = title;
        this.amount = amount;
    }

    public Account(String title) {
        this.title = title;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        return !(amount != null ? !amount.equals(account.amount) : account.amount != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }

}
