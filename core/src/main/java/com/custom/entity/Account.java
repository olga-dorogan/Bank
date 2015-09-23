package com.custom.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "accountFrom")
    private List<Transaction> transactionsOut = new ArrayList<>();

    @OneToMany(mappedBy = "accountTo")
    private List<Transaction> transactionsIn = new ArrayList<>();

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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public List<Transaction> getTransactionsOut() {
        return transactionsOut;
    }

    public void setTransactionsOut(List<Transaction> transactionsOut) {
        this.transactionsOut = transactionsOut;
    }

    public List<Transaction> getTransactionsIn() {
        return transactionsIn;
    }

    public void setTransactionsIn(List<Transaction> transactionsIn) {
        this.transactionsIn = transactionsIn;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (title != null ? !title.equals(account.title) : account.title != null) return false;
        return !(amount != null ? !amount.equals(account.amount) : account.amount != null);

    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
