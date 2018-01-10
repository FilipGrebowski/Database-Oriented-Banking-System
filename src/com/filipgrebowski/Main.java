package com.filipgrebowski;

public class Main {

    // This method is just to hide the 'Illegal Reflective Access' warnings
    // that I have been getting when trying to connect to the database.
    public static void disableWarning() {
        System.err.close();
        System.setErr(System.out);
    }

    public static void main(String[] args) {
        disableWarning();

        //Establishing a connection to the database.
        ConnectionManager.getConnection("SkyBankDetails","postgres", "Password");
        Options options = new Options();
    }
}