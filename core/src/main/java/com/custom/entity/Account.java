package com.custom.entity;

import java.math.BigDecimal;

/**
 * Created by olga on 17.09.15.
 */
public class Account {
    private Integer id;
    private Integer clientId;
    private String title;
    private BigDecimal amount;

    public Account() {

    }

    public Account(Integer id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public Account(Integer id, String title, BigDecimal amount) {
        this.id = id;
        this.title = title;
        this.amount = amount;
    }

    public Account(Integer id) {
        this.id = id;
    }

    public Account(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
                ", clientId=" + clientId +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                '}';
    }
}
