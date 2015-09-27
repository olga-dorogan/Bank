package com.custom.app.entity;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by olga on 18.09.15.
 */
@Entity
@Table(
        name = "transfer",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "account_from", "account_to"})
)
@Check(constraints = "account_from != account_to")
public class Transfer extends AbstractEntity {

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "amount", nullable = false)
    @Check(constraints = "amount >= 0")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from", nullable = true)
    private Account accountFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to", nullable = true)
    private Account accountTo;


    public Transfer() {

    }

    public Transfer(Date date, BigDecimal amount) {
        this.date = date;
        this.amount = amount;
    }

    public Transfer(Account accountFrom, Account accountTo, Date date, BigDecimal amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.date = date;
        this.amount = amount;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getId() +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}
