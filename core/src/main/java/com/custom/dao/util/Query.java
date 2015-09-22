package com.custom.dao.util;

/**
 * Created by olga on 22.09.15.
 */
public class Query {
    public static final class Client {
        public final static String
                FIND_ALL = "SELECT id, name, surname FROM client",
                FIND_BY_ID = "SELECT name, surname FROM client WHERE id = ?",
                INSERT = "INSERT INTO client(name, surname) VALUES (?, ?)";

        public final static int
                FIND_ALL_ID = 1,
                FIND_ALL_NAME = 2,
                FIND_ALL_SURNAME = 3;

        public final static int
                FIND_BY_ID_NAME = 1,
                FIND_BY_ID_SURNAME = 2;
    }

    public static final class Account {
        public final static String
                FIND_ACCOUNTS_FOR_CLIENT = "SELECT account.id, account.title, account.amount " +
                "FROM client INNER JOIN account ON (client.id = account.client_id) WHERE client.id = ?",
                FIND_BY_ID = "SELECT id, title, amount FROM account WHERE id = ?",
                INSERT_ACCOUNT = "INSERT INTO account(client_id, title, amount) VALUES(?, ?, ?)",
                UPDATE_ACCOUNT = "UPDATE account SET amount=? WHERE id= ?";
        public final static int
                FIND_ALL_ID = 1,
                FIND_ALL_TITLE = 2,
                FIND_ALL_AMOUNT = 3;
        public final static int
                FIND_BY_ID_ID = 1,
                FIND_BY_ID_TITLE = 2,
                FIND_BY_ID_AMOUNT = 3;
    }

    public static final class Transaction {
        public final static String
                FIND_ALL = "SELECT id, id_account_from, id_account_to, date, amount FROM transaction",
                INSERT = "INSERT INTO transaction(id_account_from, id_account_to, date, amount) VALUES(?,?,?,?)",
                FIND_ALL_FULL = "SELECT " +
                        "   trans_ac_from.id as id, trans_ac_from.date, trans_ac_from.account_from_title, \n" +
                        "   trans_ac_from.client_from_id,\n" +
                        "   trans_ac_from.client_from_name, trans_ac_from.client_from_surname,\n" +
                        "   account.title as account_to_title, \n" +
                        "   client.id as client_to_id,\n" +
                        "   client.name as client_to_name, client.surname as client_to_surname,\n" +
                        "   trans_ac_from.amount \n" +
                        "FROM\n" +
                        "   (SELECT \n" +
                        "       transaction.id as id, transaction.date as date, \n" +
                        "       client.id as client_from_id,\n" +
                        "       client.name as client_from_name, client.surname as client_from_surname,\n" +
                        "       account.title as account_from_title, transaction.id_account_to, transaction.amount \n" +
                        "    FROM transaction LEFT JOIN account ON(id_account_from = account.id) \n" +
                        "                     LEFT JOIN client ON(account.client_id = client.id)\n" +
                        "   ) AS trans_ac_from\n" +
                        "   LEFT JOIN account ON(trans_ac_from.id_account_to = account.id)\n" +
                        "   LEFT JOIN client ON(account.client_id = client.id)";
        public final static int
                FIND_BY_ID_ID = 1,
                FIND_BY_ID_ACCOUNT_FROM = 2,
                FIND_BY_ID_ACCOUNT_TO = 3,
                FIND_BY_ID_DATE = 4,
                FIND_BY_ID_AMOUNT = 5;
        public static final int
                FIND_ALL_FULL_ID = 1,
                FIND_ALL_FULL_DATE = 2,
                FIND_ALL_FULL_AC_FROM_TITLE = 3,
                FIND_ALL_FULL_CLIENT_FROM_ID = 4,
                FIND_ALL_FULL_CLIENT_FROM_NAME = 5,
                FIND_ALL_FULL_CLIENT_FROM_SURNAME = 6,
                FIND_ALL_FULL_AC_TO_TITLE = 7,
                FIND_ALL_FULL_CLIENT_TO_ID = 8,
                FIND_ALL_FULL_CLIENT_TO_NAME = 9,
                FIND_ALL_FULL_CLIENT_TO_SURNAME = 10,
                FIND_ALL_FULL_AMOUNT = 11;
    }

    private Query() {

    }
}
