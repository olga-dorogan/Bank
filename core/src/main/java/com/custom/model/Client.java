package com.custom.model;

import java.util.List;

/**
 * Created by olga on 17.09.15.
 */
public class Client {
    private int id;
    private String name;
    private String surname;
    private List<Account> accounts;

    public Client() {

    }

    public Client(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Client(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (id != client.id) return false;
        if (name != null ? !name.equals(client.name) : client.name != null) return false;
        if (surname != null ? !surname.equals(client.surname) : client.surname != null) return false;
        return !(accounts != null ? !accounts.equals(client.accounts) : client.accounts != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (accounts != null ? accounts.hashCode() : 0);
        return result;
    }
}
