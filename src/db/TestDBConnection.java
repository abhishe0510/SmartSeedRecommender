package db;

import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✅ Connected to MySQL successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

