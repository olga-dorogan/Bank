package com.custom.app.entity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
@Entity
@Table(
        name = "client",
        uniqueConstraints = @UniqueConstraint(columnNames = "passport")
)
public class Client extends AbstractEntity {

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "passport", nullable = false)
    @Pattern(regexp = "[А-Я]{2}[0-9]{6}")
    private String passport;


    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE}, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    public Client() {

    }

    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(int id, String firstName, String lastName) {
        setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(int id, String firstName, String lastName, String passport) {
        this(id, firstName, lastName);
        this.passport = passport;
    }

    public Client(String firstName, String lastName, String passport) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }


    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}


