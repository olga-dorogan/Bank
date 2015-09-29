package com.custom.app.entity;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
@Entity
@Table(
        name = "account",
        uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "title"})
)
@Check(constraints = "amount >= 0")
public class Account extends AbstractEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "accountFrom")
    private List<Transfer> transfersOut = new ArrayList<>();

    @OneToMany(mappedBy = "accountTo")
    private List<Transfer> transfersIn = new ArrayList<>();

    public Account() {

    }

    public Account(BigDecimal amount) {
        this.amount = amount;
    }
    public Account(String title) {
        this.title = title;
    }

    public Account(String title, BigDecimal amount) {
        this.title = title;
        this.amount = amount;
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


    public List<Transfer> getTransfersOut() {
        return transfersOut;
    }

    public void setTransfersOut(List<Transfer> transfersOut) {
        this.transfersOut = transfersOut;
    }

    public List<Transfer> getTransfersIn() {
        return transfersIn;
    }

    public void setTransfersIn(List<Transfer> transfersIn) {
        this.transfersIn = transfersIn;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                '}';
    }
}
