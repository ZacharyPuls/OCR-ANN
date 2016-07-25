package com.zachpuls.ocrann;

import java.sql.*;

/**
 * Created by zachpuls on 7/24/2016.
 */

// Quick-and-dirty DAO implementation for local storage.
public class Database {

    private static Connection jdbcConnection;

    static {
        try {
            jdbcConnection = DriverManager.getConnection("jdbc:sqlite:ocr-ann.database");
            Statement statement = jdbcConnection.createStatement();

            if (!statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='ocr_data';").next()) {
                statement.executeUpdate(
                        "CREATE TABLE ocr_data(" +
                                "ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                                "CHARACTER CHAR NOT NULL" +
                                "H30 REAL NOT NULL" +
                                "H50 REAL NOT NULL" +
                                "H80 REAL NOT NULL" +
                                "V30 REAL NOT NULL" +
                                "V50 REAL NOT NULL" +
                                "V80 REAL NOT NULL" +
                                "HSYMMETRY REAL NOT NULL" +
                                "VSYMMETRY REAL NOT NULL" +
                                "WHT REAL NOT NULL" +
                                "SUM REAL NOT NULL" +
                        ")");
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTableEntry(char character, double h30, double h50, double h80,
                                     double v30, double v50, double v80, double hSymmetry,
                                     double vSymmetry, double wht, double sum) {
        try {
            Statement statement = jdbcConnection.createStatement();

            statement.executeUpdate(
                    "INSERT INTO ocr_data(CHARACTER,H30,H50,H80,V30,V50,V80,HSYMMETRY,VSYMMETRY,WHT,SUM)" +
                            "VALUES(" + character + "," + h30 + "," + h50 + "," + h80 + "," +
                            v30 + "," + v50 + "," + v80 + "," + hSymmetry + "," + vSymmetry + "," +
                            wht + "," + sum + ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
