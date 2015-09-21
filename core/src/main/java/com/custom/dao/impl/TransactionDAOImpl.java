package com.custom.dao.impl;

import com.custom.config.DatasourceFactory;
import com.custom.dao.TransactionDAO;
import com.custom.entity.Account;
import com.custom.entity.Client;
import com.custom.entity.Transaction;
import com.custom.entity.composite.TransactionFull;
import com.custom.exception.BankDAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olga on 18.09.15.
 */
public class TransactionDAOImpl implements TransactionDAO {
    private enum TYPE {TRANSACTION, CREDIT, DEBIT}

    private final static String SQL_FIND_ALL =
            "SELECT id, id_account_from, id_account_to, date, amount FROM transaction";
    private final static String SQL_INSERT =
            "INSERT INTO transaction(id_account_from, id_account_to, date, amount) VALUES(?,?,?,?)";
    private final static String SQL_FIND_ALL_FULL =
            "SELECT " +
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
    private static final int COL_TR_ID = 1,
            COL_TR_DATE = 2,
            COL_TR_AC_FROM_TITLE = 3,
            COL_TR_CLIENT_FROM_ID = 4,
            COL_TR_CLIENT_FROM_NAME = 5,
            COL_TR_CLIENT_FROM_SURNAME = 6,
            COL_TR_AC_TO_TITLE = 7,
            COL_TR_CLIENT_TO_ID = 8,
            COL_TR_CLIENT_TO_NAME = 9,
            COL_TR_CLIENT_TO_SURNAME = 10,
            COL_TR_AMOUNT = 11;

    @Override
    public List<Transaction> findAll() {
        try (Connection conn = DatasourceFactory.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_FIND_ALL)) {
            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                transactions.add(new Transaction(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getTimestamp(4), rs.getBigDecimal(5)));
            }
            return transactions;
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }

    @Override
    public List<TransactionFull> findAllFull() {
        try (Connection conn = DatasourceFactory.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_FIND_ALL_FULL)) {
            List<TransactionFull> transactions = new ArrayList<>();
            Transaction transaction;
            Client clientFrom, clientTo;
            Account accountFrom, accountTo;
            String accountTitle;
            Integer clientId;
            while (rs.next()) {
                transaction = new Transaction(rs.getInt(COL_TR_ID), rs.getTimestamp(COL_TR_DATE), rs.getBigDecimal(COL_TR_AMOUNT));
                accountTitle = rs.getString(COL_TR_AC_FROM_TITLE);
                if (accountTitle == null) {
                    accountFrom = null;
                } else {
                    accountFrom = new Account(accountTitle);
                }

                clientId = rs.getInt(COL_TR_CLIENT_FROM_ID);
                if (rs.wasNull()) {
                    clientFrom = null;
                } else {
                    clientFrom = new Client(
                            clientId,
                            rs.getString(COL_TR_CLIENT_FROM_NAME),
                            rs.getString(COL_TR_CLIENT_FROM_SURNAME));
                }
                accountTitle = rs.getString(COL_TR_AC_TO_TITLE);
                if (accountTitle == null) {
                    accountTo = null;
                } else {
                    accountTo = new Account(accountTitle);
                }
                clientId = rs.getInt(COL_TR_CLIENT_TO_ID);
                if (rs.wasNull()) {
                    clientTo = null;
                } else {
                    clientTo = new Client(
                            clientId,
                            rs.getString(COL_TR_CLIENT_TO_NAME),
                            rs.getString(COL_TR_CLIENT_TO_SURNAME));
                }
                TransactionFull transactionFull = new TransactionFull(transaction, clientFrom, clientTo, accountFrom, accountTo);
                transactions.add(transactionFull);
            }
            return transactions;
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }

    @Override
    public void createTransaction(Connection conn, Transaction transaction) {
        create(conn, transaction, TYPE.TRANSACTION);
    }

    @Override
    public void createCredit(Connection conn, Transaction transaction) {
        create(conn, transaction, TYPE.CREDIT);
    }

    @Override
    public void createDebit(Connection conn, Transaction transaction) {
        create(conn, transaction, TYPE.DEBIT);
    }

    private void create(Connection conn, Transaction transaction, TYPE type) {
        try (PreparedStatement prStmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            switch (type) {
                case CREDIT:
                    prStmt.setNull(1, Types.INTEGER);
                    prStmt.setInt(2, transaction.getIdAccountTo());
                    break;
                case DEBIT:
                    prStmt.setInt(1, transaction.getIdAccountFrom());
                    prStmt.setNull(2, Types.INTEGER);
                    break;
                case TRANSACTION:
                    prStmt.setInt(1, transaction.getIdAccountFrom());
                    prStmt.setInt(2, transaction.getIdAccountTo());
                    break;
            }
            prStmt.setTimestamp(3, new Timestamp(transaction.getDate().getTime()));
            prStmt.setBigDecimal(4, transaction.getAmount());
            if (prStmt.executeUpdate() != 1) {
                throw new BankDAOException("Error with creation " + transaction);
            }
            try (ResultSet rsGeneratedKeys = prStmt.getGeneratedKeys()) {
                if (rsGeneratedKeys.next()) {
                    transaction.setId(rsGeneratedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new BankDAOException(e);
        }
    }
}
