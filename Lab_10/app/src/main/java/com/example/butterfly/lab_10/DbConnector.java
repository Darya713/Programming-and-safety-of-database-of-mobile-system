package com.example.butterfly.lab_10;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DbConnector implements Runnable {

    private String type;
    private String body;
    private static ArrayList<String> result;

    static {
        result = new ArrayList<>();
    }

    public DbConnector(String type, String body) {
        this.type = type;
        this.body = body;
    }

    @Override
    public void run() {
        try {
            Connection connection;
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://195.50.2.82;" +
                            "databasename=JDBCTEST;" +
                            "user=jdbc;" +
                            "password=jdbctest"
            );
            Log.d("LAB_10", "Connection OK");
            Statement statement = connection.createStatement();
            switch (type) {
                case "SELECT":
                    ResultSet resultSet = statement.executeQuery("SELECT КодОбласти, Область FROM dbo.Area");
                    while (resultSet.next()) {
                        result.add(resultSet.getString(1) + " " + resultSet.getString(2));
                    }
                    resultSet.close();
                    break;
                default:
                    statement.execute(body);
                    break;
            }
            connection.close();
            Log.d("LAB_10", "Connection Closed");
        }
        catch (ClassNotFoundException e) {
            Log.d("LAB_10", e.getMessage());
        }
        catch (Exception e) {
            Log.d("LAB_10", e.getMessage());
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static ArrayList<String> getResult() {
        return result;
    }

    public static void setResult(ArrayList<String> result) {
        DbConnector.result = result;
    }

    public static void clearArrayList() {
        result.clear();
    }
}
