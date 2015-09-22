package com.custom.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by olga on 18.09.15.
 */
public class Transaction {
    private Integer id;
    private Integer idAccountFrom;
    private Integer idAccountTo;
    private Date date;
    private BigDecimal amount;

    public Transaction(Integer id, Integer idAccountFrom, Integer idAccountTo, Date date, BigDecimal amount) {
        this.id = id;
        this.idAccountFrom = idAccountFrom;
        this.idAccountTo = idAccountTo;
        this.date = date;
        this.amount = amount;
    }

    public Transaction(Integer idAccountFrom, Integer idAccountTo, Date date, BigDecimal amount) {
        this.idAccountFrom = idAccountFrom;
        this.idAccountTo = idAccountTo;
        this.date = date;
        this.amount = amount;
    }

    public Transaction(Integer id, Date date, BigDecimal amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    public Transaction() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdAccountFrom() {
        return idAccountFrom;
    }

    public void setIdAccountFrom(Integer idAccountFrom) {
        this.idAccountFrom = idAccountFrom;
    }

    public Integer getIdAccountTo() {
        return idAccountTo;
    }

    public void setIdAccountTo(Integer idAccountTo) {
        this.idAccountTo = idAccountTo;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", idAccountFrom=" + idAccountFrom +
                ", idAccountTo=" + idAccountTo +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (idAccountFrom != null ? !idAccountFrom.equals(that.idAccountFrom) : that.idAccountFrom != null)
            return false;
        if (idAccountTo != null ? !idAccountTo.equals(that.idAccountTo) : that.idAccountTo != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return !(amount != null ? !amount.equals(that.amount) : that.amount != null);

    }

    @Override
    public int hashCode() {
        int result = idAccountFrom != null ? idAccountFrom.hashCode() : 0;
        result = 31 * result + (idAccountTo != null ? idAccountTo.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
