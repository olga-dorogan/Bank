package com.custom.app.dto;

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

    public Account(String title, BigDecimal amount, Client client) {
        this.title = title;
        this.amount = amount;
        this.client = client;
    }

    public Account(int id, String title, BigDecimal amount, Client client) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.client = client;
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
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", client=" + client +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (title != null ? !title.equals(account.title) : account.title != null) return false;
        if (amount != null ? !(amount.compareTo(account.amount) == 0) : account.amount != null) return false;
        return !(client != null ? !client.equals(account.client) : account.client != null);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        return result;
    }
}
