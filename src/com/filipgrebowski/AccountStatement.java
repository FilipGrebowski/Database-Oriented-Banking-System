package com.filipgrebowski;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

// IMPORTANT:
// I am always going to print two accounts on one screen when viewing the details,
// just to allow an easier understanding of changes in account balance etc.

public class AccountStatement {

    private Statement statement;
    private Scanner scanner;

    public AccountStatement() {
        scanner = new Scanner(System.in);
    }

    // Prints the account details of two separate user accounts.
    public void printAccountDetails() {
        try {
            statement = ConnectionManager.connection.createStatement();
            statement.execute("SELECT * FROM Accounts ORDER BY id ASC");
            ResultSet results = statement.getResultSet();
            System.out.println("\n");
            while (results.next()) {

                System.out.println("+--------------------------+");
                System.out.println("| Account ID: " + results.getInt("id") + "\n" +
                        "| Account Number: " + results.getInt("accountNumber") + "\n" +
                        "| Sort Code: " + results.getInt("sortCode")+ "\n" +
                        "| Account Balance: £" + results.getString("balance"));
                System.out.println("+--------------------------+");

            }
            statement.close();
        }
        catch (SQLException e) {
            System.out.println("Unable to get data from database.");
            e.printStackTrace();
        }
        Options.again();
    }


    public void printStatement() {
        try {
            Transactions transactions = new Transactions();
            int account = scanner.nextInt();

            statement = ConnectionManager.connection.createStatement();

            // Getting details from both the Accounts and Transactions tables in the database.
            statement.execute("SELECT Transactions.id, Transactions.deposited, Transactions.withdrawn, " +
                    "Transactions.date, Transactions.amount, Transactions.balance " +
                    "FROM Transactions INNER JOIN Accounts " +
                    "ON Transactions.id = Accounts.id WHERE Accounts.id = " + account);

            ResultSet results = statement.getResultSet();

            // Prints the column name for each column, depending on the user choice that has
            // been selected.
            if (Options.getUserInput() == 5) {
                ResultSetMetaData columns = results.getMetaData();
                System.out.println(columns.getColumnName(1) + "     " + columns.getColumnName(4) + "        " +
                        columns.getColumnName(5) + "     " + columns.getColumnName(6));
                System.out.println("+--------------------------------------+");
            }
            if (Options.getUserInput() == 6) {
                ResultSetMetaData columns = results.getMetaData();
                System.out.println("   " + columns.getColumnName(4) + "        " +
                        columns.getColumnName(5));
                System.out.println("+-----------------------+");
            }

            // Increments through the result set, and depending on user option, which is either a
            // detailed or transaction statement, the statements get printed.
            // Depending on the bit values of deposited and withdrawn, a negative or positive sign
            // is added to be able to determine if money has been payed out on in.
            while (results.next()) {

                int isDeposit = results.getInt("deposited");
                int isWithdrawal = results.getInt("withdrawn");


                if (Options.getUserInput() == 5) {
                    if (isDeposit == 1 && isWithdrawal == 0) {
                        System.out.println(results.getInt("id") + "   " + results.getDate("date") + "    + £" +
                                results.getDouble("amount") + "     £" + results.getDouble("balance"));
                    }
                    if (isDeposit == 0 && isWithdrawal == 1) {
                        System.out.println(results.getInt("id") + "   " + results.getDate("date") + "    - £" +
                                results.getDouble("amount") + "     £" + results.getDouble("balance"));
                    }
                }
                if (Options.getUserInput() == 6) {
                    if (isDeposit == 1 && isWithdrawal == 0) {
                        System.out.println(results.getDate("date") + "    + £" +
                                results.getDouble("amount"));
                    }
                    if (isDeposit == 0 && isWithdrawal == 1) {
                        System.out.println(results.getDate("date") + "    - £" +
                                results.getDouble("amount"));
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        Options.again();
    }
}