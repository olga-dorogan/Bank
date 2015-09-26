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
                '}';
    }

}
