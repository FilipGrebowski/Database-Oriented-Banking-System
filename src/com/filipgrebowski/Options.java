package com.filipgrebowski;

import java.util.Scanner;

public class Options {

    public static int userInput;

    public Options() {
        displayOptions();
    }

    // Display the available options for the account holder.
    public static void displayOptions() {

        AccountStatement options = new AccountStatement();
        Transactions transaction = new Transactions();

        System.out.println("\n");
        System.out.println("1. Deposit funds.");
        System.out.println("2. Withdrawal funds.");
        System.out.println("3. Transfer funds.");
        System.out.println("4. View Account Details.");
        System.out.println("5. Print detailed statement.");
        System.out.println("6. Print transaction statement.");
        System.out.println("7. Quit.");

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n");
        System.out.print("Please enter which service you would like: ");
        userInput = scanner.nextInt();

        switch (userInput) {
            case 1:
                System.out.println("\n");
                System.out.println("Which account would you like to deposit money into?");
                System.out.println("Type 1 for account one, or 2 for account two.");
                transaction.makeTransaction();
                break;
            case 2:
                System.out.println("\n");
                System.out.println("Which account would you like to withdrawal money from?");
                System.out.println("Type 1 for account one, or 2 for account two.");
                transaction.makeTransaction();
                break;
            case 3:
                transaction.transfer();
                break;
            case 4:
                options.printAccountDetails();
                break;
            case 5:
                System.out.println("\n");
                System.out.println("Detailed Statement");
                System.out.println("+-----------------+");
                System.out.println("Would you like to view detailed statement for account 1 or 2?");
                options.printStatement();
                break;
            case 6:
                System.out.println("\n");
                System.out.println("Transaction Statement");
                System.out.println("+--------------------+");
                System.out.println("Would you like to view transaction statement for account 1 or 2?");
                options.printStatement();
                break;
            case 7:
                System.out.println("Thank you for using our service.");
            default:
                scanner.close();
                break;
        }
    }

    // A method that is called after every user operation to allow them to have the
    // option to select another service.
    public static void again() {
        System.out.println("\n");
        System.out.println("Would you like to use another service? (y) for yes, (n) for no.");
        Scanner input = new Scanner(System.in);
        String letter = input.nextLine();
        if (letter.equals("y")) {
            displayOptions();
        }
        else if (letter.equals("n")) {
            input.close();
        }
    }

    // Returning the user input as a static object so it can be user in
    // other classes.
    public static int getUserInput() {
        return userInput;
    }
}