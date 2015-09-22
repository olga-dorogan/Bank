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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (clientId != null ? !clientId.equals(account.clientId) : account.clientId != null) return false;
        if (title != null ? !title.equals(account.title) : account.title != null) return false;
        return !(amount != null ? !amount.equals(account.amount) : account.amount != null);

    }

    @Override
    public int hashCode() {
        int result = clientId != null ? clientId.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
