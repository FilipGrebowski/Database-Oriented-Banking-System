package com.filipgrebowski;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Transactions {

    private double amount;
    private double balance;
    private Scanner scanner;

    public Transactions() {
        scanner = new Scanner(System.in);
        balance = 0;
    }

    public void makeTransaction() {
        try {
            // Obtaining user input for the desired transaction.
            // Either deposit or withdrawal.
            int choice = scanner.nextInt();
            if (choice == 1 || choice == 2) {
                System.out.println("Please enter amount: ");
                amount = scanner.nextDouble();

                // Creating a sql statement that gets the current balance of the
                // account that has been selected by the user.
                Statement statement = ConnectionManager.connection.createStatement();
                statement.execute("SELECT balance FROM Accounts WHERE id = " + choice);
                ResultSet result = statement.getResultSet();
                System.out.println("\n");
                while (result.next()) {
                    balance = result.getDouble("balance");

                    // Depending on user selection, the correct arithmetic is performed
                    // on the balance.
                    if (Options.getUserInput() == 1) {
                        balance += amount;
                    }
                    else if (Options.getUserInput() == 2) {
                        while (amount > balance) {
                            System.out.println("Not enough funds in the bank account. Please try again.");
                            amount = scanner.nextDouble();
                        }
                        balance -= amount;
                    }
                    System.out.println("The new balance for the account is: £" + balance);
                }

                // An sql update statement which updates the balance in the correct bank account.
                statement.execute("UPDATE Accounts SET balance = " + balance + " WHERE id = " + choice);

                // Depending again on user selection, the correct transaction is inserted.
                // The below sql Insert queries take values for 'deposited' and 'withdrawn'.
                // These values are bit values, which are either 1 or 0, representing true or
                // false respectively.
                if (Options.getUserInput() == 1) {
                    statement.execute("INSERT INTO Transactions (id, deposited, withdrawn, amount, date, balance) " +
                            "VALUES ('" + choice + "', '1', '0', " + amount + ", now(), " + balance + ")");
                }
                else if (Options.getUserInput() == 2) {
                    statement.execute("INSERT INTO Transactions (id, deposited, withdrawn, amount, date, balance) " +
                            "VALUES ('" + choice + "', '0', '1', " + amount + ", now(), " + balance + ")");
                }
                statement.close();
            }
            else {
                System.out.println("Bank account does not exist, please try again.");
                makeTransaction(); // Using recursion to make the method call to itself again.
            }
        }
        // Catching any sql exceptions.
        catch (SQLException e) {
            System.out.println("Unable to get data from database.");
            e.printStackTrace();
        }
        Options.again();
    }

    public void transfer() {
        // An array list to hold both current balances of the bank accounts.
        ArrayList<Double> array = new ArrayList<>();

        // Getting user input.
        System.out.println("Which account would you like to transfer money from? 1 or 2?");
        int pickFirstAccount = scanner.nextInt();
        System.out.println("To what account?");
        int pickSecondAccount = scanner.nextInt();
        System.out.println("Please enter amount: ");
        amount = scanner.nextDouble();

        try {
            // Storing user queries in an array.
            String[] queries = {
                    "SELECT balance FROM Accounts WHERE id = " + pickFirstAccount,
                    "SELECT balance FROM Accounts WHERE id = " + pickSecondAccount
            };
            Statement statement = ConnectionManager.connection.createStatement();

            // Executing user queries one by one, and adding the balance values to the array.
            for (String query : queries) {
                statement.execute(query);
                ResultSet result = statement.getResultSet();
                while (result.next()) {
                    double value = result.getDouble("balance");
                    array.add(value);
                }
            }

            // Assigning balance valeus from array to appropriate variables.
            double accountOneBalance = array.get(0);
            double accountTwoBalance = array.get(1);

            // Depending on user selection, the right arithmetic is performed.
            if (pickFirstAccount == 1) {
                while (amount > accountOneBalance) {
                    System.out.println("Not enough funds in the bank account. Please try again.");
                    amount = scanner.nextDouble();
                }
                accountOneBalance -= amount;
                accountTwoBalance += amount;
            }

            if (pickFirstAccount == 2) {
                while (amount > accountTwoBalance) {
                    System.out.println("Not enough funds in the bank account. Please try again.");
                    amount = scanner.nextDouble();
                }
                accountTwoBalance += amount;
                accountOneBalance -= amount;
            }

            // Another array storing user queries.
            String[] insertQuery = {
                    "UPDATE Accounts SET balance = " + accountOneBalance + " WHERE id = " + pickFirstAccount,
                    "UPDATE Accounts SET balance = " + accountTwoBalance + " WHERE id = " + pickSecondAccount,
                    "INSERT INTO Transactions (id, deposited, withdrawn, amount, date, balance) " +
                            "VALUES ('" + pickFirstAccount + "', '0', '1', " + amount + ", now(), " + accountOneBalance + ")",
                    "INSERT INTO Transactions (id, deposited, withdrawn, amount, date, balance) " +
                            "VALUES ('" + pickSecondAccount + "', '1', '0', " + amount + ", now(), " + accountTwoBalance + ")"
            };

            // This time, they are all added to a batch.
            for (String query : insertQuery) {
                statement.addBatch(query);
            }

            // Executing the whole batch (all at once for better performance).
            statement.executeBatch();
            System.out.println("The new balance for account one is: £" + accountOneBalance);
            System.out.println("The new balance for account two is: £" + accountTwoBalance);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        Options.again();
    }
}